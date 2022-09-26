package com.example.flink14.analysis.attendance;


import com.example.flink14.analysis.constant.JdbcConnectorOptions;
import com.example.flink14.analysis.utils.FlinkStreamUtil;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * flink table api
 * 月报统计自定义项
 * F_N、F_I 转行处理
 */
public class MonthAttendStatistic2 {

    public static void main(String[] args) throws Exception {
        TableEnvironment tableEnvironment = buildTableEnvironment();

        // 1、创建sourcetable
        Map<String, String> sourceColumnMap = new LinkedHashMap<>();
        sourceColumnMap.put("id", "int");
        sourceColumnMap.put("title", "varchar");
        sourceColumnMap.put("author", "varchar");
        Schema sourceSchema = buildSchema(sourceColumnMap, "sourceTable");
        String connector = "jdbc";
        String url = "jdbc:mysql://localhost:3306/test_sql?serverTimezone=UTC";
        String username = "root";
        String password = "root";
        String tableName = "a";
        TableDescriptor sourceTable = buildMysqlCdcTable("mysql-cdc", url, username, password, tableName, sourceSchema);

        tableEnvironment.createTemporaryTable("sourceTable", sourceTable);

        // 2、创建 sink table
        Map<String, String> sinkColumnMap = new LinkedHashMap<>();
        sinkColumnMap.put("id", "int");
        sinkColumnMap.put("title", "varchar");
        sinkColumnMap.put("author", "varchar");
        Schema sinkSchema = buildSchema(sinkColumnMap, "sinkTable");
        String sinkTableName = "a_sink";
        TableDescriptor sinkTable = buildJdbcTable(connector, url, username, password, sinkTableName, sinkSchema);

        tableEnvironment.createTemporaryTable("sinkTable", sinkTable);

        //3、Table API执行查询(可以执行多次查询，中间表可以注册到catalog也可以不注册)
        Table resultTable = tableEnvironment.from("sourceTable");

        //注册表到catalog(可选的)
        tableEnvironment.createTemporaryView("tmpTable", resultTable);
        Table tmpResultTable = tableEnvironment.sqlQuery("select id,title,author from tmpTable");


        TableResult tableResult = tmpResultTable.executeInsert("sinkTable");
        tmpResultTable.printSchema();
        tableResult.print();

        //tableEnvironment.execute("sync-flink-cdc");
    }


    public static TableEnvironment buildTableEnvironment() {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()//Flink1.14开始就删除了其他的执⾏器了，只保留了BlinkPlanner
                .inStreamingMode() //默认就是StreamingMode
                //.inBatchMode()
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //env.enableCheckpointing(2000L, CheckpointingMode.EXACTLY_ONCE);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        //tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);

        return tableEnv;
    }

    /**
     * 创建 TableSchema
     *
     * @param columnMap
     * @return
     */
    public static Schema buildSchema(Map<String, String> columnMap, String tableType) {
        Schema.Builder sourceBuilder = Schema.newBuilder();

        columnMap.entrySet().forEach(column -> {
            DataType dataType = convertDataType(column.getValue());

            if ("id".equals(column.getKey()) && "sinkTable".equals(tableType)) {
                sourceBuilder.column(column.getKey(), dataType.notNull());
                sourceBuilder.primaryKey("id");
            } else {
                sourceBuilder.column(column.getKey(), dataType);
            }
        });

        return sourceBuilder.build();
    }

    public static DataType convertDataType(String cloumnType) {
        DataType dataType = null;

        if (cloumnType.startsWith("varchar")) {
            return DataTypes.STRING();
        } else if (cloumnType.startsWith("int")) {
            return DataTypes.INT();
        } else if (cloumnType.startsWith("bigint")) {
            return DataTypes.BIGINT();
        }

        return dataType;
    }


    /**
     * "jdbc:mysql://localhost:3306/test_sql?serverTimezone=UTC"
     *
     * @param connector
     * @param url
     * @param username
     * @param password
     * @param tableName
     * @param sourceSchema
     * @return
     */
    public static TableDescriptor buildMysqlCdcTable(String connector, String url, String username, String password,
                                                     String tableName, Schema sourceSchema) {
        return TableDescriptor.forConnector(connector)
                .option("hostname", "localhost")
                .option("port", "3306")
                .option("username", "root")
                .option("password", "root")
                .option("database-name", "test_sql")
                .option("table-name", "a")
                .option("scan.incremental.snapshot.enabled", "false")
                .schema(sourceSchema)
                .build();
    }

    public static TableDescriptor buildJdbcTable(String connector, String url, String username, String password,
                                                 String tableName, Schema sourceSchema) {
        return TableDescriptor.forConnector(connector)
                .option(JdbcConnectorOptions.url, url)
                .option(JdbcConnectorOptions.username, username)
                .option(JdbcConnectorOptions.password, password)
                .option(JdbcConnectorOptions.tablename, tableName)
                .option("driver", "com.mysql.cj.jdbc.Driver")

                .schema(sourceSchema)
                .build();
    }

    /**
     * 查询统计自定义项的含义
     * scheme.id scheme_id,item.DBCODE item_code,item.NAME item_name
     *
     * @param tableEnvironment
     * @return
     */
    public static Map<String, Map<String, String>> queryStatisticSchema(StreamTableEnvironment tableEnvironment,
                                                                        String tenantid) {
        Map<String, Map<String, String>> metas = new HashMap<>();

        Map<String, String> tsSchemeMap = new LinkedHashMap<>();
        tsSchemeMap.put("ID", "char");
        tsSchemeMap.put("TENANTID", "varchar");
        tsSchemeMap.put("ITEM_SCHEME_ID", "varchar");

        Map<String, String> tsItemMainMap = new LinkedHashMap<>();
        tsItemMainMap.put("ID", "varchar");
        tsItemMainMap.put("TENANTID", "varchar");

        Map<String, String> tsItemMap = new LinkedHashMap<>();
        tsItemMap.put("ID", "varchar");
        tsItemMap.put("DBCODE", "varchar");
        tsItemMap.put("NAME", "varchar");
        tsItemMap.put("MAIN_ID", "varchar");
        tsItemMap.put("ITEMTYPE", "varchar");
        tsItemMap.put("USERDEFFLAG", "varchar");
        tsItemMap.put("TENANTID", "varchar");

        Schema tsSchemeSchema = FlinkStreamUtil.buildSchema(tsSchemeMap, "sourceTable");
        Schema tsItemMainSchema = FlinkStreamUtil.buildSchema(tsItemMainMap, "sourceTable");
        Schema tsItemSchema = FlinkStreamUtil.buildSchema(tsItemMap, "sourceTable");

        TableDescriptor schemeTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306"
                        + "/hrattenddb"
                        + "?serverTimezone=UTC",
                "root", "root", "ts_scheme", tsSchemeSchema);
        TableDescriptor itemMainTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306"
                        + "/hrattenddb?serverTimezone=UTC",
                "root", "root", "ts_item_main", tsItemMainSchema);
        TableDescriptor itemTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306/hrattenddb"
                        + "?serverTimezone=UTC",
                "root", "root", "ts_item", tsItemSchema);

        tableEnvironment.createTemporaryTable("ts_scheme", schemeTableDescriptor);
        tableEnvironment.createTemporaryTable("ts_item_main", itemMainTableDescriptor);
        tableEnvironment.createTemporaryTable("ts_item", itemTableDescriptor);

//        tableEnvironment.sqlQuery("select ID from ts_scheme").execute().print();
//        tableEnvironment.sqlQuery("select ID from ts_item_main").execute().print();
//        tableEnvironment.sqlQuery("select ID from ts_item").execute().print();

        TableResult tableResult = tableEnvironment.sqlQuery("select scheme.ID scheme_id,item.DBCODE item_code,item.NAME "
                + "item_name \n"
                + "       from ts_scheme scheme \n"
                + "       inner join ts_item_main item_main on scheme.TENANTID = item_main.TENANTID \n"
                + "       and scheme.ITEM_SCHEME_ID = item_main.ID \n"
                + "       inner join ts_item item on scheme.TENANTID = item.TENANTID \n"
                + "       and item.MAIN_ID = item_main.ID and item.ITEMTYPE = '1' and item.USERDEFFLAG = '1' \n"
                + "       where scheme.TENANTID = '" + tenantid + "'").execute();

        CloseableIterator<Row> collect = tableResult.collect();

        while (collect.hasNext()) {
            Row row = collect.next();
            String schemeId = row.getField("scheme_id").toString();
            String itemCode = row.getField("item_code").toString();
            String itemName = row.getField("item_name").toString();

            Map<String, String> colMap = metas.get(schemeId);

            if (colMap == null) {
                colMap = new HashMap<>();
            }

            colMap.put(itemCode, itemName);
            metas.put(schemeId, colMap);
        }

        return metas;
    }


}

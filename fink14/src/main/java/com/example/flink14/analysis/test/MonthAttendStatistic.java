package com.example.flink14.analysis.test;


import com.example.flink14.analysis.constant.JdbcConnectorOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;
import org.apache.flink.util.Collector;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.apache.flink.table.api.Expressions.row;

/**
 * flink table api
 * 月报统计自定义项
 * F_N、F_I 转行处理
 */
@Slf4j
public class MonthAttendStatistic {

    private static Map<String, Map<String, String>> tableSchemas = new HashMap();

    static {
        Map<String, String> schemeSchema = new HashMap<>();
        schemeSchema.put("", "");
        Map<String, String> tsItemMainSchema = new HashMap<>();
        Map<String, String> tsItemSchema = new HashMap<>();

    }

    public static void main(String[] args) throws Exception {
        // 1、创建表环境
        StreamTableEnvironment tableEnvironment = FlinkStreamUtil.buildTableEnvironment();
        // 2、查询source表元数据
        Map<String, String> sourceColumnMap = queryTableSchema(tableEnvironment, "hrattenddb", "ts_monthstat");

//        sourceColumnMap.clear();
//        sourceColumnMap.put("ID","varchar");
//        sourceColumnMap.put("TENANTID","varchar");

        // 3、注册source table

        Schema sourceSchema = FlinkStreamUtil.buildSchema(sourceColumnMap, "sourceTable");

        String connector = "jdbc";
        String url = "jdbc:mysql://localhost:3306/hrattenddb?serverTimezone=UTC";
        String username = "root";
        String password = "root";
        TableDescriptor sourceTable = FlinkStreamUtil.buildMysqlCdcTable("mysql-cdc", "localhost",
                "3306", "root", "root",
                "hrattenddb", "ts_monthstat", sourceSchema);

//        TableDescriptor sourceTable = FlinkStreamUtil.buildJdbcTable(connector, url, username, password, "hrattenddb"
//                + ".ts_monthstat", sourceSchema);

        tableEnvironment.createTemporaryTable("sourceTable", sourceTable);

        //5、Table API执行查询(可以执行多次查询，中间表可以注册到catalog也可以不注册)
        Table resultTable = tableEnvironment.from("sourceTable").select("ID,SCHEME_ID,TENANTID").limit(1);

        CloseableIterator<Row> results = resultTable.execute().collect();

        while (results.hasNext()) {
            Row row = results.next();

            Object schemeIdObj = row.getField("SCHEME_ID");

            if (Objects.isNull(schemeIdObj)) {
                log.error("{} SCHEME_ID 为空", row);
                return;
            }

            String tenantid = row.getField("TENANTID").toString();

            System.out.println("tenantid :" + tenantid);
        }

        // Table 转 DataStream
        // todo error
        // doesn't support consuming update and delete changes which is produced by node TableSourceScan
        DataStream<Row> rowDataStream = tableEnvironment.toDataStream(resultTable);

        // 对结果集处理
        rowDataStream.flatMap((Row row, Collector<String> collector) -> {
            Object schemeIdObj = row.getField("SCHEME_ID");

            if (Objects.isNull(schemeIdObj)) {
                log.error("{} SCHEME_ID 为空", row);
                return;
            }

            String tenantid = row.getField("TENANTID").toString();

            // 获取自定义字段以及含义
            Map<String, Map<String, String>> schemaMap = queryStatisticSchema(tableEnvironment, tenantid);

            String schemeId = schemeIdObj.toString();
            Map<String, String> schemeMap = schemaMap.get(schemeId);

            schemeMap.forEach((key, value) -> {
                CustomMonthAttendModel customMonthAttendModel = new CustomMonthAttendModel();
                String itemCode = key.toUpperCase();
                String itemName = value;

                Object fValue = row.getField(itemCode);

                if (!Objects.nonNull(value)) {
                    setItemValue(customMonthAttendModel, fValue, itemCode);

                    customMonthAttendModel.setMonthstatId(Optional.of(row.getField("ID")).get().toString());
                    customMonthAttendModel.setTenantid(Optional.of(row.getField("TENANTID")).get().toString());
                    customMonthAttendModel.setYtenantId(Optional.of(row.getField("ytenant_id")).get().toString());
                    customMonthAttendModel.setBuId(Optional.ofNullable(row.getField("BU_ID")).orElse("").toString());
                    customMonthAttendModel.setOrgId(Optional.ofNullable(row.getField("ORG_ID")).orElse("").toString());
                    customMonthAttendModel.setDeptId(Optional.ofNullable(row.getField("DEPT_ID")).orElse("").toString());
                    customMonthAttendModel.setStaffId(Optional.of(row.getField("STAFF_ID")).get().toString());
                    customMonthAttendModel.setStaffCode(Optional.ofNullable(row.getField("staff_code")).orElse("").toString());
                    customMonthAttendModel.setTsyear(Optional.of(row.getField("TSYEAR")).get().toString());
                    customMonthAttendModel.setTsmonth(Optional.of(row.getField("TSMONTH")).get().toString());
                    customMonthAttendModel.setSchemeId(Optional.of(row.getField("SCHEME_ID")).get().toString());
                    customMonthAttendModel.setItemCode(itemCode);
                    customMonthAttendModel.setItemName(itemName);
                    customMonthAttendModel.setCusType(1);


                }
            });
        });


        // 4、创建 sink table
        Map<String, String> sinkColumnMap = new LinkedHashMap<>();
        sinkColumnMap.put("id", "int");
        sinkColumnMap.put("title", "varchar");
        sinkColumnMap.put("author", "varchar");
        Schema sinkSchema = FlinkStreamUtil.buildSchema(sinkColumnMap, "sinkTable");
        String sinkTableName = "a_sink";
        TableDescriptor sinkTable = FlinkStreamUtil.buildJdbcTable(connector, url, username, password, sinkTableName, sinkSchema);

        tableEnvironment.createTemporaryTable("sinkTable", sinkTable);


        // 7、遍历source表结果集 注册表 写入sink表
        CloseableIterator<Row> collect = resultTable.execute().collect();

        while (collect.hasNext()) {
            List<ApiExpression> rows = new ArrayList<>();
            Row next = collect.next();
            Object id = next.getField("id");
            Object title = next.getField("title");
            Object author = next.getField("author");

            ApiExpression row = row(id, title, author);
            rows.add(row);

            Table tmpTable = tableEnvironment.fromValues(
                    DataTypes.ROW(
                            DataTypes.FIELD("id", DataTypes.INT()),
                            DataTypes.FIELD("title", DataTypes.STRING()),
                            DataTypes.FIELD("author", DataTypes.STRING())
                    ),
                    rows
            );


            //注册表到catalog(可选的)
            tableEnvironment.createTemporaryView("tmpTable", tmpTable);
            Table tmpResultTable = tableEnvironment.from("tmpTable");

            TableResult tableResult = tmpTable.executeInsert("sinkTable");
            //tmpResultTable.printSchema();
            //tableResult.print();
        }

        //tableEnvironment.execute("sync-flink-cdc");
    }

    /**
     * 设置月报统计自定义项值
     *
     * @param customMonthAttendModel
     * @param value
     * @param itemCode
     */
    private static void setItemValue(CustomMonthAttendModel customMonthAttendModel, Object value,
                                     String itemCode) {
        if (itemCode.startsWith("F_N")) {
            customMonthAttendModel.setNItemValue((BigDecimal) value);
        } else if (itemCode.startsWith("F_I")) {
            customMonthAttendModel.setIItemValue((Integer) value);
        } else if (itemCode.startsWith("F_V")) {
            customMonthAttendModel.setVItemValue((String) value);
        } else if (itemCode.startsWith("F_D")) {
            if (value instanceof LocalDateTime) {
                Date dateValue = Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
                customMonthAttendModel.setDItemValue(dateValue);
            } else if (value instanceof java.sql.Date) {
                Date dateValue = new Date(((java.sql.Date) value).getTime());
                customMonthAttendModel.setDItemValue(dateValue);
            } else if (value instanceof Date) {
                customMonthAttendModel.setDItemValue((Date) value);
            }
        } else if (itemCode.startsWith("F_B")) {
            customMonthAttendModel.setBItemValue((String) value);
        }
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
        tsSchemeMap.put("ID", "varchar");

        Map<String, String> tsItemMainMap = new LinkedHashMap<>();
        tsItemMainMap.put("ID", "varchar");

        Map<String, String> tsItemMap = new LinkedHashMap<>();
        tsItemMap.put("ID", "varchar");
        tsItemMap.put("DBCODE", "varchar");
        tsItemMap.put("NAME", "varchar");

        Schema tsSchemeSchema = FlinkStreamUtil.buildSchema(tsSchemeMap, "sourceTable");
        Schema tsItemMainSchema = FlinkStreamUtil.buildSchema(tsItemMainMap, "sourceTable");
        Schema tsItemSchema = FlinkStreamUtil.buildSchema(tsItemMap, "sourceTable");

        TableDescriptor schemeTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306/hrattenddb"
                        + "?serverTimezone=UTC",
                "", "", "ts_scheme", tsSchemeSchema);
        TableDescriptor itemMainTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306"
                        + "/hrattenddb?serverTimezone=UTC",
                "root", "root", "ts_item_main", tsItemMainSchema);
        TableDescriptor itemTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306/hrattenddb"
                        + "?serverTimezone=UTC",
                "root", "root", "ts_item", tsItemSchema);

        tableEnvironment.createTemporaryTable("ts_scheme", schemeTableDescriptor);
        tableEnvironment.createTemporaryTable("ts_item_main", itemMainTableDescriptor);
        tableEnvironment.createTemporaryTable("ts_item", itemTableDescriptor);

        TableResult tableResult = tableEnvironment.sqlQuery("select scheme.id scheme_id,item.DBCODE item_code,item.NAME "
                + "item_name \n"
                + "       from ts_scheme scheme \n"
                + "       inner join ts_item_main item_main on scheme.tenantid = item_main.tenantid \n"
                + "       and scheme.ITEM_SCHEME_ID = item_main.id \n"
                + "       inner join ts_item item on scheme.tenantid = item.tenantid \n"
                + "       and item.MAIN_ID = item_main.id and item.ITEMTYPE = 1 and item.USERDEFFLAG = 1 \n"
                + "       where scheme.tenantid = '" + tenantid + "'").execute();

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


    public static Map<String, String> queryTableSchema(TableEnvironment tableEnvironment, String dbName, String tableName) {
        // 查询表元数据
        Map<String, String> schemaColumnMap = new LinkedHashMap<>();
        schemaColumnMap.put("TABLE_SCHEMA", "varchar");
        schemaColumnMap.put("TABLE_NAME", "varchar");
        schemaColumnMap.put("COLUMN_NAME", "varchar");
        schemaColumnMap.put("DATA_TYPE", "varchar");
        schemaColumnMap.put("CHARACTER_MAXIMUM_LENGTH", "bigint");

        Schema tableSchema = FlinkStreamUtil.buildSchema(schemaColumnMap, "sourceTable");
        TableDescriptor schemaTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306"
                        + "/information_schema?serverTimezone=UTC", "root",
                "root", "COLUMNS", tableSchema);

        tableEnvironment.createTemporaryTable("schemaTable", schemaTableDescriptor);

        CloseableIterator<Row> schemaTable = tableEnvironment.from("schemaTable").select("TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,"
                + "DATA_TYPE").filter("TABLE_SCHEMA = '" + dbName + "'").where("TABLE_NAME = '" + tableName + "'").execute().collect();
        tableEnvironment.from("schemaTable").select("TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE").filter("TABLE_SCHEMA = '"
                + dbName + "'").where("TABLE_NAME = '" + tableName + "'").execute().print();

        Map<String, String> schemaMap = new HashMap<>();

        while (schemaTable.hasNext()) {
            Row next = schemaTable.next();
            String columnName = next.getField("COLUMN_NAME").toString();
            String dataType = next.getField("DATA_TYPE").toString();
            schemaMap.put(columnName, dataType);
        }

        return schemaMap;
    }


    public static TableEnvironment buildTableEnvironment() {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()//Flink1.14开始就删除了其他的执⾏器了，只保留了BlinkPlanner
                .inStreamingMode() //默认就是StreamingMode
                //.inBatchMode()
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getCheckpointConfig().setCheckpointInterval(11);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);

        return tableEnv;
    }

    /**
     * 创建 TableSchema
     *
     * @param columnMap
     * @return
     */
    public static Schema buildSchema(Map<String, String> columnMap) {
        Schema.Builder sourceBuilder = Schema.newBuilder();

        columnMap.entrySet().forEach(column -> {
            DataType dataType = convertDataType(column.getValue());
            sourceBuilder.column(column.getKey(), dataType);
        });

        return sourceBuilder.build();
    }

    public static DataType convertDataType(String cloumnType) {
        DataType dataType = null;

        if (cloumnType.startsWith("varchar")) {
            return DataTypes.STRING();
        } else if (cloumnType.startsWith("int")) {
            return DataTypes.INT();
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
    public static TableDescriptor buildTable(String connector, String url, String username, String password,
                                             String tableName, Schema sourceSchema) {
        return TableDescriptor.forConnector(connector)
                .option(JdbcConnectorOptions.url, url)
                .option(JdbcConnectorOptions.username, username)
                .option(JdbcConnectorOptions.password, password)
                .option(JdbcConnectorOptions.tablename, tableName)
                .schema(sourceSchema)
                .build();
    }


}

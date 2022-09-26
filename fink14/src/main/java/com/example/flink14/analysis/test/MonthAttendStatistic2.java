package com.example.flink14.analysis.test;


import com.example.flink14.analysis.constant.JdbcConnectorOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.types.DataType;

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


}

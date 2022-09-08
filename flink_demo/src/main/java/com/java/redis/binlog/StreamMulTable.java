package com.java.redis.binlog;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class StreamMulTable {

    public static void main(String[] args) throws Exception {
        EnvironmentSettings fsSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, fsSettings);

        tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);


        // 数据源表
        String sourceaDDL =
                "CREATE TABLE source_a (\n" +
                        " id INT NOT NULL,\n" +
                        " title STRING,\n" +
                        " author STRING\n" +
                        ") WITH (\n" +
                        " 'connector' = 'mysql-cdc',\n" +
                        " 'hostname' = 'localhost',\n" +
                        " 'port' = '3306',\n" +
                        " 'username' = 'root',\n" +
                        " 'password' = 'root',\n" +
                        " 'database-name' = 'test_sql',\n" +
                        " 'table-name' = 'a'\n" +
                        ")";

        tableEnv.executeSql(sourceaDDL);


        // 数据源表
        String sourcebDDL =
                "CREATE TABLE source_b (\n" +
                        " count1 INT,\n" +
                        " author STRING\n" +
                        ") WITH (\n" +
                        " 'connector' = 'mysql-cdc',\n" +
                        " 'hostname' = 'localhost',\n" +
                        " 'port' = '3306',\n" +
                        " 'username' = 'root',\n" +
                        " 'password' = 'root',\n" +
                        " 'database-name' = 'test_sql',\n" +
                        " 'table-name' = 'b'\n" +
                        ")";

        tableEnv.executeSql(sourcebDDL);

        String transformSQL =
                "insert into test_cdc_sink select source_a.id,source_a.title,source_a.author,source_b.count1 from source_a left "
                        + "join source_b  on source_a.author = source_b.author  ";



//        String transformSQL =
//                "select source_a.id,source_a.title,source_a.author,source_b.count1 from source_a left "
//                        + "join source_b on source_a.author = source_b.author  ";

        String url = "jdbc:mysql://127.0.0.1:3306/test_sql";
        String userName = "root";
        String password = "root";
        String mysqlSinkTable = "a_sink";

        String sinkDDL =
                "CREATE TABLE test_cdc_sink (\n" +
                        " id INT NOT NULL,\n" +
                        " title STRING,\n" +
                        " author STRING,\n" +
                        " count1 INT,\n" +
                        " PRIMARY KEY (id) NOT ENFORCED \n " +
                        ") WITH (\n" +
                        " 'connector' = 'jdbc',\n" +
                        " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                        " 'url' = '" + url + "',\n" +
                        " 'username' = '" + userName + "',\n" +
                        " 'password' = '" + password + "',\n" +
                        " 'table-name' = '" + mysqlSinkTable + "'\n" +
                        ")";

        tableEnv.executeSql(sinkDDL);
        TableResult tableResult = tableEnv.executeSql(transformSQL);

        tableResult.print();

        env.execute("sync-flink-cdc");
    }
}

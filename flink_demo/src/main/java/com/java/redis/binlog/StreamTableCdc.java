package com.java.redis.binlog;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class StreamTableCdc {

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
        String sourceDDL =
                "CREATE TABLE mysql_binlog (\n" +
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

        String url = "jdbc:mysql://127.0.0.1:3306/test_sql";
        String userName = "root";
        String password = "root";
        String mysqlSinkTable = "a_sink";
        // 输出目标表
        String sinkDDL =
                "CREATE TABLE test_cdc_sink (\n" +
                        " id INT NOT NULL,\n" +
                        " title STRING,\n" +
                        " author STRING,\n" +
                        " PRIMARY KEY (id) NOT ENFORCED \n " +
                        ") WITH (\n" +
                        " 'connector' = 'jdbc',\n" +
                        " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                        " 'url' = '" + url + "',\n" +
                        " 'username' = '" + userName + "',\n" +
                        " 'password' = '" + password + "',\n" +
                        " 'table-name' = '" + mysqlSinkTable + "'\n" +
                        ")";
        // 简单的聚合处理
        String transformSQL =
                "insert into test_cdc_sink select * from mysql_binlog";

        tableEnv.executeSql(sourceDDL);
        tableEnv.executeSql(sinkDDL);
        TableResult result = tableEnv.executeSql(transformSQL);

        // 等待flink-cdc完成快照
        result.print();
        env.execute("sync-flink-cdc");
    }
}

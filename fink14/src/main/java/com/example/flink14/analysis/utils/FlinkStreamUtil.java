package com.example.flink14.analysis.utils;

import com.example.flink14.analysis.constant.JdbcConnectorOptions;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.types.DataType;

import java.util.Map;

public class FlinkStreamUtil {


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


    /**
     * @param cloumnType
     * @return
     */
    public static DataType convertDataType(String cloumnType) {
        DataType dataType = null;

        if (cloumnType.startsWith("varchar")) {
            return DataTypes.STRING();
        } else if (cloumnType.startsWith("int") || cloumnType.startsWith("smallint")) {
            return DataTypes.INT();
        } else if (cloumnType.startsWith("bigint")) {
            return DataTypes.BIGINT();
        } else if (cloumnType.startsWith("char")) {
            return DataTypes.STRING();
        } else if (cloumnType.startsWith("decimal")) {
            return DataTypes.DOUBLE();
        } else if (cloumnType.startsWith("datetime")) {
            return DataTypes.TIMESTAMP();
        } else if (cloumnType.startsWith("date")) {
            return DataTypes.DATE();
        }

        return dataType;
    }

    public static TableDescriptor buildMysqlCdcTable(String connector, String hostname, String port, String username,
                                                     String password, String databaseName, String tableName,
                                                     Schema sourceSchema) {
        return TableDescriptor.forConnector(connector)
                .option("hostname", hostname)
                .option("port", port)
                .option("username", username)
                .option("password", password)
                .option("database-name", databaseName)
                .option("table-name", tableName)
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


    public static StreamTableEnvironment buildTableEnvironment() {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()//Flink1.14开始就删除了其他的执⾏器了，只保留了BlinkPlanner
                .inStreamingMode() //默认就是StreamingMode
                //.inBatchMode()
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(2000L, CheckpointingMode.EXACTLY_ONCE);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);

        return tableEnv;
    }

}

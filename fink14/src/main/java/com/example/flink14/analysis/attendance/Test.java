package com.example.flink14.analysis.attendance;

import com.example.flink14.analysis.constant.JdbcConnectorOptions;
import com.example.flink14.analysis.utils.FlinkStreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.table.api.*;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;

import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
public class Test {

    public static void main(String[] args) throws Exception {
//        testStreamApi();
        //testTableApi();
        testTableApi2();
    }


    public static void testTableApi2() {
        TableEnvironment tableEnvironment = FlinkStreamUtil.buildTableEnvironment();

        // 查询表元数据
        Map<String, String> sinkColumnMap = new LinkedHashMap<>();
        sinkColumnMap.put("TABLE_SCHEMA", "varchar");
        sinkColumnMap.put("TABLE_NAME", "varchar");
        sinkColumnMap.put("COLUMN_NAME", "varchar");
        sinkColumnMap.put("DATA_TYPE", "varchar");
        sinkColumnMap.put("CHARACTER_MAXIMUM_LENGTH", "bigint");

        Schema tableSchema = FlinkStreamUtil.buildSchema(sinkColumnMap, "sourceTable");
        TableDescriptor schemaTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306"
                        + "/information_schema?serverTimezone=UTC", "root",
                "root", "COLUMNS", tableSchema);

        tableEnvironment.createTemporaryTable("schemaTable", schemaTableDescriptor);

        /*Table schemaTable = tableEnvironment.from("schemaTable");
        schemaTable.*/

        log.error("______________sql limit 10");
        tableEnvironment.sqlQuery("select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE from schemaTable limit 10 ").execute().print();

        log.error("______________sql where");
        tableEnvironment.sqlQuery("select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE from schemaTable where TABLE_SCHEMA = "
                + "'test_sql'"
                + " and TABLE_NAME = 'a' ").execute().print();

        log.error("______________table api");
        tableEnvironment.from("schemaTable").select("TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE").filter("TABLE_SCHEMA = "
                + "'test_sql'").where("TABLE_NAME = 'a'").execute().print();


        log.error("______________table api result ");
        CloseableIterator<Row> schemaTable =
                tableEnvironment.sqlQuery("select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE from schemaTable  where "
                        + "TABLE_SCHEMA = 'test_sql' and TABLE_NAME = 'a'  ").execute().collect();

        while (schemaTable.hasNext()) {
            Row next = schemaTable.next();
            String columnName = next.getField("COLUMN_NAME").toString();
            String dataType = next.getField("DATA_TYPE").toString();
            System.out.println(columnName + ":" + dataType);
        }

    }


    public static void testTableApi() {
        TableEnvironment tableEnvironment = FlinkStreamUtil.buildTableEnvironment();

        // 查询表元数据
        Map<String, String> schemaColumnMap = new LinkedHashMap<>();
        schemaColumnMap.put("COLUMN_NAME", "varchar");
        schemaColumnMap.put("DATA_TYPE", "varchar");
        schemaColumnMap.put("TABLE_NMAE", "varchar");
        schemaColumnMap.put("TABLE_SCHEMA", "varchar");
        schemaColumnMap.put("CHARACTER_MAXIMUM_LENGTH", "bigint");

        Schema tableSchema = FlinkStreamUtil.buildSchema(schemaColumnMap, "sourceTable");
        TableDescriptor schemaTableDescriptor = FlinkStreamUtil.buildJdbcTable("jdbc", "jdbc:mysql://localhost:3306"
                        + "/information_schema?serverTimezone=UTC", "root",
                "root", "COLUMNS", tableSchema);

        tableEnvironment.createTemporaryTable("schemaTable", schemaTableDescriptor);

        /*Table schemaTable = tableEnvironment.from("schemaTable");
        schemaTable.*/


        tableEnvironment.sqlQuery("select * from schemaTable ").execute().print();

        CloseableIterator<Row> schemaTable =
                tableEnvironment.sqlQuery("select * from schemaTable ").execute().collect();

        while (schemaTable.hasNext()) {
            Row next = schemaTable.next();
            String columnName = next.getField("COLUMN_NAME").toString();
            String dataType = next.getField("DATA_TYPE").toString();
            System.out.println(columnName + ":" + dataType);
        }

    }

    // 不用sql方式创建 source sink table
    public static void testStreamApi() {
        //1、创建TableEnvironment
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()//Flink1.14开始就删除了其他的执⾏器了，只保留了BlinkPlanner
                .inStreamingMode() //默认就是StreamingMode
                //.inBatchMode()
                .build();

        TableEnvironment tEnv = TableEnvironment.create(settings);

        //2、创建source table: 1）读取外部表；2）从Table API或者SQL查询结果创建表
        Schema sourceSchema =
                Schema.newBuilder()
                        .column("id", DataTypes.BIGINT())
                        .column("title", DataTypes.STRING())
                        .build();

        TableDescriptor sourceTable =
                TableDescriptor.forConnector("jdbc")
                        .option(JdbcConnectorOptions.url, "jdbc:mysql://localhost:3306/test_sql")
                        .option(JdbcConnectorOptions.username, "root")
                        .option(JdbcConnectorOptions.password, "root")
                        .option(JdbcConnectorOptions.tablename, "a")
                        .schema(sourceSchema)
                        .build();

        tEnv.createTemporaryTable("sourceTable", sourceTable);

        //注册表到catalog(可选的)
        // 创建虚拟表
        //tEnv.createTemporaryView("sourceTable", tableDescriptor);
        // 创建常规表
        /* final TableDescriptor sourceDescriptor = TableDescriptor.forConnector("datagen")
                .schema(Schema.newBuilder()
                        .column("f0", DataTypes.STRING())
                        .build())
                .option(DataGenOptions.ROWS_PER_SECOND, 100)
                .build(); */
        //tEnv.createTable("SourceTableA", sourceDescriptor);
        //tEnv.createTemporaryTable("SourceTableA", sourceDescriptor);
        // 也可使⽤SQL DDL
        //tEnv.executeSql("CREATE [TEMPORARY] TABLE MyTable (...) WITH (...)")

        //3、创建sink table
        final Schema schema = Schema.newBuilder()
                .column("id", DataTypes.BIGINT())
                .column("title", DataTypes.STRING())
                .build();

        tEnv.createTemporaryTable("sinkTable", TableDescriptor.forConnector("print")
                .schema(schema)
                .build());

        //4、Table API执行查询(可以执行多次查询，中间表可以注册到catalog也可以不注册)
        Table resultTable = tEnv.from("sourceTable");
//        Table resultTable = projTable.select($("id"), $("name"));

        //5、输出(包括执行,不需要单独在调用tEnv.execute("job"))
        resultTable.executeInsert("sinkTable");
        resultTable.printSchema();
    }
}

package com.java.redis.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.types.Row;

public class MysqlReadDataSet {

    public static void main(String[] args) throws Exception {

        // ToDo 0.env
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // ToDo 1.source
        DataSet<Row> dataInput = env.createInput(JDBCInputFormat.buildJDBCInputFormat()
                // 配置数据库连接信息
                .setDrivername("com.mysql.jdbc.Driver")  // JDBC驱动名
                // com.mysql.jdbc.Driver：是 mysql-connector-java 5 中的
                // com.mysql.cj.jdbc.Driver：是 mysql-connector-java 6及以上 中的
                .setDBUrl("jdbc:mysql://localhost:3306/test_sql?serverTimezone=GMT%2B8&useSSL=false")  // 数据库URL
                .setUsername("root")  // 用户名
                .setPassword("root")  // 登录密码
                .setQuery("select id,title,author from a")  // 需要执行的SQL语句
                .setRowTypeInfo(new RowTypeInfo(  // 设置查询的列的类型
                        BasicTypeInfo.INT_TYPE_INFO,  // id：Int类型
                        BasicTypeInfo.STRING_TYPE_INFO,  // name：String类型
                        BasicTypeInfo.STRING_TYPE_INFO))  // age：Int类型
                .finish());

        // ToDo 2.transformation
        DataSet<Student> dataMap = dataInput.map(new MapFunction<Row, Student>() {

            @Override
            public Student map(Row row) throws Exception {
                // 转换为Student类型
                return new Student(
                        (int) row.getField(0),
                        (String) row.getField(1),
                        (String) row.getField(2));
            }
        });

        // ToDo 3.sink
        dataMap.print();

        // ToDo 4.execute
//        env.execute();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {

        private Integer id;
        private String name;
        private String author;
    }
}

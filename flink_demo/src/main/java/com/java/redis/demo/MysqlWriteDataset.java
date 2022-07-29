package com.java.redis.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat;
import org.apache.flink.types.Row;

import java.sql.Types;
import java.util.Arrays;

public class MysqlWriteDataset {

    public static void main(String[] args) throws Exception {


        // ToDo 0.env
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // ToDo 1.source
        DataSet<Student> dataSource = env.fromCollection(
                Arrays.asList(
                        new Student(5, "Nancy", 22),
                        new Student(6, "Emma", 20),
                        new Student(7, "Sophia", 19)
                )
        );

        // ToDo 2.transformation
        DataSet<Row> dataMap = dataSource.map(new MapFunction<Student, Row>() {

            @Override
            public Row map(Student student) throws Exception {
                // 转换为Row类型
                Row row = new Row(3);  // 有3个参数
                row.setField(0, student.getId());
                row.setField(1, student.getName());
                row.setField(2, student.getAge());
                return row;
            }
        });

        // ToDo 3.sink
        dataMap.output(JDBCOutputFormat.buildJDBCOutputFormat()
                .setDrivername("com.mysql.jdbc.Driver")  // JDBC驱动名
                .setDBUrl("jdbc:mysql://localhost:3306/demodb?serverTimezone=GMT%2B8&useSSL=false")  // 数据库URL
                .setUsername("root")  // 用户名
                .setPassword("123456")  // 登录密码
                .setQuery("insert into t_student(id,name,age) values(?,?,?)")  // 需要执行的SQL语句
                // 如果数据不存在则插入，数据存在则更新，可以将SQL语句替换为以下：
                // insert into t_student(id,name,age) values(?,?,?) on duplicate key update name=values(name),age=values(age)
                .setSqlTypes(new int[]{
                        Types.INTEGER, Types.VARCHAR, Types.INTEGER})  // 设置查询的列的类型
                .finish());

        // ToDo 4.execute
        env.execute();
        System.out.println("MySQL写入成功！");
    }

    @Data  // 注解在类上，为类提供读写属性，还提供equals()、hashCode()、toString()方法
    @AllArgsConstructor  // 注解在类上，为类提供全参构造函数，参数的顺序与属性定义的顺序一致
    @NoArgsConstructor  // 注解在类上，为类提供无参构造函数
    public static class Student {

        private Integer id;
        private String name;
        private Integer age;
    }
}

package com.java.redis.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;

public class MysqlwriteDatastream {

    public static void main(String[] args) throws Exception {


        // ToDo 0.env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // ToDo 1.source
        DataStreamSource<Student> dataInput = env.fromCollection(
                Arrays.asList(
                        new Student(3, "Linda", 20),
                        new Student(6, "Allen", 19),
                        new Student(8, "Marcia", 20),
                        new Student(9, "Helen", 18)
                )
        );

        // ToDo 2.transformation

        // ToDo 3.sink
        dataInput.addSink(new MySQLSink());

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

    public static class MySQLSink extends RichSinkFunction<Student> {


        private Connection conn = null;
        private PreparedStatement insertStmt = null;
        private PreparedStatement updateStmt = null;

        // 打开数据库连接，只执行一次，之后一直使用这个连接
        @Override
        public void open(Configuration parameters) throws Exception {

            super.open(parameters);
            Class.forName("com.mysql.jdbc.Driver");  // 加载数据库驱动
            conn = DriverManager.getConnection(  // 获取连接
                    "jdbc:mysql://localhost:3306/demodb?serverTimezone=GMT%2B8&useSSL=false",  // 数据库URL
                    "root",  // 用户名
                    "123456");  // 登录密码
            insertStmt = conn.prepareStatement(  // 获取执行语句
                    "insert into t_student(id,name,age) values (?,?,?)");  // 插入数据
            updateStmt = conn.prepareStatement(  // 获取执行语句
                    "update t_student set name=?,age=? where id=?");  // 更新数据
        }

        // 执行插入和更新
        @Override
        public void invoke(Student value, Context ctx) throws Exception {

            // 每条数据到来后，直接执行更新语句
            updateStmt.setString(1, value.getName());  // 与占位符(?)对应的参数
            updateStmt.setInt(2, value.getAge());
            updateStmt.setInt(3, value.getId());
            updateStmt.execute();  // 执行更新语句

            // 如果更新数为0，则执行插入语句
            if (updateStmt.getUpdateCount() == 0) {

                insertStmt.setInt(1, value.getId());
                insertStmt.setString(2, value.getName());
                insertStmt.setInt(3, value.getAge());
                insertStmt.execute();  // 执行插入语句
            }
        }

        // 关闭数据库连接
        @Override
        public void close() throws Exception {

            super.close();
            if(conn != null) conn.close();
            if(insertStmt != null) insertStmt.close();
            if(updateStmt != null) updateStmt.close();
        }
    }
}

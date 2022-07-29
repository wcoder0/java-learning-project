package com.java.redis.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlSourceDataStream {

    public static void main(String[] args) throws Exception {


        // ToDo 0.env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // ToDo 1.source
        DataStream<Student> dataInput = env.addSource(new MySQLSource());

        // ToDo 2.transformation

        // ToDo 3.sink
        dataInput.print();

        // ToDo 4.execute
        env.execute();
    }

    @Data  // 注解在类上，为类提供读写属性，还提供equals()、hashCode()、toString()方法
    @AllArgsConstructor  // 注解在类上，为类提供全参构造函数，参数的顺序与属性定义的顺序一致
    @NoArgsConstructor  // 注解在类上，为类提供无参构造函数
    public static class Student {

        private Integer id;
        private String name;
        private String author;
    }

    public static class MySQLSource extends RichSourceFunction<Student> {


        private volatile boolean isRunning = true;
        private Connection conn = null;
        private PreparedStatement ps = null;

        // 打开数据库连接，只执行一次，之后一直使用这个连接
        @Override
        public void open(Configuration parameters) throws Exception {

            super.open(parameters);
            Class.forName("com.mysql.jdbc.Driver");  // 加载数据库驱动
            conn = (Connection) DriverManager.getConnection(  // 获取连接
                    "jdbc:mysql://localhost:3306/test_sql?serverTimezone=GMT%2B8&useSSL=false",  // 数据库URL
                    "root",  // 用户名
                    "root");  // 登录密码
            ps = (PreparedStatement) conn.prepareStatement(  // 获取执行语句
                    "select id,title,author from a");  // 需要执行的SQL语句
        }

        // 执行查询并获取结果
        @Override
        public void run(SourceContext<Student> ctx) throws Exception {

            while (isRunning) {
                // 使用while循环可以不断读取数据
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {

                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    ctx.collect(new Student(id, name, author));  // 以流的形式发送结果
                }

                Thread.sleep(5000);  // 每隔5秒查询一次
            }
        }

        // 取消数据生成
        @Override
        public void cancel() {

            isRunning = false;
        }

        // 关闭数据库连接
        @Override
        public void close() throws Exception {

            super.close();
            if (conn != null) conn.close();
            if (ps != null) ps.close();
        }
    }
}

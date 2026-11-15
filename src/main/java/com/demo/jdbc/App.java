package com.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.mysql.cj.exceptions.ExceptionFactory;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        //记得改maven依赖路径
        //1. 注册驱动
        //Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/temp?useSSL=false";
        String username= "root";
        String password= "";
        Connection conn = DriverManager.getConnection(url,username,password);
        //3.定义sql
        String sql = "insert into user(uuid, name) values(20, '20');";
        //4.获取执行sql的对象statement
        Statement stmt = conn.createStatement();
        //5．执行sql
        int count = stmt.executeUpdate(sql); //受影响的行数
        //6. 处理结果
        System.out.println(count);
        //7. 释放资源 
        stmt.close(); 
        conn.close();
    }
}

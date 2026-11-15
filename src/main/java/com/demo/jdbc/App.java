package com.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

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
        try{
            conn.setAutoCommit(false); //事务保证操作的完整性
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            String sql1 = "select name, profession, age from user where status is not null for share;";
            //4.获取执行sql的对象statement
            Statement stmt = conn.createStatement();
            //5．执行sql
            ResultSet set = stmt.executeQuery(sql1); //executeQuery是用来执行的DQL的,和其他语句执行用的不一样
            //提交
            conn.commit();
            //6. 处理结果
            List<User> list = new ArrayList<>();
            while(set.next()){ //要先用一次next()才会指向第一条record
                list.add(new User(set.getString("name"), set.getString("profession"), (byte)set.getInt("age")));
            }
            System.out.println(list.size());
            //7. 释放资源
            stmt.close();
            set.close();
        }catch(Exception e){
            conn.rollback();
        }    
        //7. 释放资源  
        conn.close();
    }
}

package com.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        String url = "jdbc:mysql://127.0.0.1:3306/temp?useSSL=false&useServerPrepStmts=true";
        //加了&useServerPrepStmts=true才有预编译,否则即使是PreparedStatement也没有预编译.预编译可以提升sql执行的速度
        String username= "root";
        String password= "";
        Connection conn = DriverManager.getConnection(url,username,password);
        try{
            conn.setAutoCommit(false); //事务保证操作的完整性
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            String sql1 = "select name, phone where name = ? && phone = ? for share;";
            //4.准备装载sql的对象PreparedStatement
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            //等待3s后执行
            Thread.sleep(3000);
            //替换问号
            pstmt.setString(1, "Hacker");
            pstmt.setString(1, "incorrect_password' || '1'='1");//有效防止sql注入攻击,特殊字符会被转译(前面被加了\)
            //5．执行sql
            ResultSet set = pstmt.executeQuery(); //executeQuery是用来执行的DQL的,和其他语句执行用的不一样
            //提交
            conn.commit();
            //6. 处理结果
            if(set.next()){
                System.out.println("匹配成功");
            }    
            //7. 释放资源
            pstmt.close();
            set.close();
        }catch(Exception e){
            conn.rollback();
            System.out.println("匹配失败");
        }    
        //7. 释放资源  
        conn.close();
    }
}

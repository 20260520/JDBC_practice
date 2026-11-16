package com.demo.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mysql.cj.exceptions.ExceptionFactory;
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        //上一次commit中sql语句有bug,没写from user
        //加载druid配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src\\main\\resources\\druid.properties"));
        //获取连接池对象,连接池可以降低资源开销,避免多个用户交替反复创建/关闭连接对象
        DataSource ds = DruidDataSourceFactory.createDataSource(prop);
        //获取数据库连接资源
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false); //事务保证操作的完整性
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        String sql1 = "select name, phone from user where name = ? && phone = ? for share";
        //4.准备装载sql的对象PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(sql1);
        //替换问号
        pstmt.setString(1, "Hacker");
        pstmt.setString(2, "incorrect_password' || '1'='1");//有效防止sql注入攻击,特殊字符会被转译(前面被加了\)            
        try{
            //5．执行sql
            ResultSet set = pstmt.executeQuery(); //executeQuery是用来执行的DQL的,和其他语句执行用的不一样
            //提交
            conn.commit();
            //6. 处理结果
            if(set.next()){
                System.out.println("匹配成功");
            }else{
                System.out.println("匹配失败");
            }    
            //7. 释放资源
            set.close();
        }catch(Exception e){
            conn.rollback();
            //等待3s后执行
            Thread.sleep(3000);
            System.out.println("匹配失败 3000");
        }    
        //7. 释放资源 
        pstmt.close(); 
        conn.close();
    }
}

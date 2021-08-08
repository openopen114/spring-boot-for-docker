package org.example;

import java.awt.Dialog.ModalExclusionType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import cloud.sql.Druid;
import com.sun.org.apache.bcel.internal.generic.Select;
import com.sun.org.apache.xpath.internal.operations.Bool;
import entity.Accounts;


public class Repo {



    List<Accounts> accountsLists;


    public Repo() {
        accountsLists = new ArrayList<>();
    }








    //測試用
    public String getTest() throws SQLException {

        System.out.println(" ===> getTest");
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //取得連接
            conn = Druid.getConn();

			/*
			 *
			 *
                select
                  *
                from
                  accounts
                where
                  user_id = 1
			 */

            //SQL
            String query =
                    "select " +
                    "  * " +
                    "from " +
                    "  accounts " +
                    "where " +
                    "  user_id =  ? " ;

            //預處理SQL
            preparedStatement = null;
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,1);

            //查詢請求
            resultSet = preparedStatement.executeQuery();

            //結果集
            while (resultSet.next()) {
                System.out.println(resultSet.getString("username"));
            }


            // Close
            resultSet.close();


        } catch (Throwable e) {
            if (conn != null) {
                try {
                    //Roll back
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    preparedStatement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }







    // 取得Accounts
    public List<Accounts> getAccountsByUserId(Integer _userId) throws SQLException {


        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //取得連接
            conn = Druid.getConn();

			/*
			 *
			 *
				select * from accounts where user_id = 1;
			 */

            //SQL
            String query = "   select * from accounts where user_id =  ?  " ;

            //預處理SQL
            preparedStatement = null;
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,_userId);


            //查詢請求
            resultSet = preparedStatement.executeQuery();

            //結果集
            accountsLists.clear();
            while (resultSet.next()) {

                Accounts model = new Accounts();

                model.setUsername(resultSet.getString("username"));
                model.setPassword(resultSet.getString("password"));
                model.setCreated_on(resultSet.getTimestamp("created_on"));
                model.setUser_id(resultSet.getInt("user_id"));

                accountsLists.add(model);
            }


            // Close
            resultSet.close();


        } catch (Throwable e) {
            if (conn != null) {
                try {
                    //Roll back
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    preparedStatement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return accountsLists;
    }






    // 取得 tra01 最新一筆
    public String getTra01Lastest() throws SQLException {


        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet resultSet = null;


        String result = "===> ";

        try {
            //取得連接
            conn = Druid.getConn();

			/*
			 *
			 *
				select * from tra01
                where status = 'Y'
                order by date desc
                limit 1
			 */

            //SQL
            String query = "   select * from tra01 " +
                    "where status = 'Y' " +
                    "order by date desc " +
                    "limit 1 " ;

            //預處理SQL
            psmt = null;
            psmt = conn.prepareStatement(query);


            //查詢請求
            resultSet = psmt.executeQuery();

            //結果集
            accountsLists.clear();
            while (resultSet.next()) {

                result = result + resultSet.getString("date");
            }


            // Close
            resultSet.close();


        } catch (Throwable e) {
            if (conn != null) {
                try {
                    //Roll back
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    psmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return result;
    }




}

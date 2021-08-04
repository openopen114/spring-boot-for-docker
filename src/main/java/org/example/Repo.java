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



}

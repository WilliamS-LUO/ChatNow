package Sever;

import java.sql.*;

public class ConnectToDatabase {

    private String url = "jdbc:mysql://localhost:3306/User?useSSL=false";
    private String username = "root";
    private String password = "123456";
    private Connection connection;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public ConnectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建账户，在数据库中插入数据
    public boolean creatAccount(String username, String password) {
        String insertSql = "INSERT INTO User(Username, Password, Online)" +
                "VALUES(?,?,0)";
        int count = 0;
        try {
            preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    //查询账户是否存在
    public boolean checkAccountExist(String username) {
        String selectSql = "SELECT * From User WHERE Username = ?";
        boolean flag = false;
        try {
            preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            flag = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (flag) {
            return true;
        } else {
            return false;
        }
    }

    //验证账户密码是否正确
    public boolean loginCheck(String username, String password){
        String selectSql = "SELECT * From User WHERE Username = ? AND Password = ?";
        boolean flag = false;
        try {
            preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            flag = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (flag) {
            return true;
        } else {
            return false;
        }
    }

    //登录成功后，更新账户状态
    public void updateState(String username,int state){
        String updateSql = "UPDATE User SET Online = ? WHERE Username = ?";
        try {
            preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setInt(1,state);
            preparedStatement.setString(2,username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询特定用户登录状态
    public boolean checkState(String username){
        String selectSql = "SELECT * FROM User WHERE Online = 1 AND Username = ?";
        boolean flag = false;
        try {
            preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            flag = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (flag) {
            return true;
        } else {
            return false;
        }
    }
}
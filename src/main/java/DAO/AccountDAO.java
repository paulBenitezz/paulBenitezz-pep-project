package DAO;

import java.sql.*;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

/*
 * create table account (
    account_id int primary key auto_increment,
    username varchar(255) unique,
    password varchar(255)
);
 */


public class AccountDAO {
    
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generated_account_id = (int) rs.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    } 


    public Account loginAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        Account foundAccount = null;
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                foundAccount = new Account();
                foundAccount.setAccount_id(rs.getInt("account_id"));
                foundAccount.setUsername(rs.getString("username"));
                foundAccount.setPassword(rs.getString("password"));
            }

        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());        
        }

        return foundAccount;

    }

    public boolean isValidAccountCreation(Account account) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // Check if the username already exists
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
    
            // If a record with the same username exists, return false
            if (rs.next()) {
                return false;  // Username already exists
            }
    
            // Check if the password is at least 4 characters long and the username is not empty
            if (account.getUsername().isEmpty() || account.getPassword().length() < 4) {
                return false;  // Invalid username or password
            }
    
        } catch (SQLException e) {
            System.out.println("Error validating account creation: " + e.getMessage());
        }
    
        // All checks passed, account creation is valid
        return true;
    }

    public Account getAccountById(int account_id) {
         Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
                
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());

        }
        return null;
    }
    
}

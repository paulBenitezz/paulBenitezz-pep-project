package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

/*
 * 
create table message (
    message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)
);
 */

public class MessageDAO {

    public Message createMessage(Message message) {

        Connection connection = ConnectionUtil.getConnection();

        try {
            
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        
        return null;
    } 


    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());

        }

        return messages;
    }
    
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());

        }
        return null;
    }

    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // Use getMessageById to fetch the message before deletion
            Message messageToDelete = getMessageById(message_id);
            
            if (messageToDelete != null) {
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, message_id);
                int rowsAffected = deleteStatement.executeUpdate();
                deleteStatement.close();
    
                if (rowsAffected > 0) {
                    System.out.println("Message deleted successfully.");
                    return messageToDelete; // Return the deleted message
                }
            }
         } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());

        }
        return null;
    }

    public void updateMessageById(int message_id, String new_message) {
        Connection connection = ConnectionUtil.getConnection();
        try {

                String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, new_message);
                updateStatement.setInt(2, message_id);
                updateStatement.executeUpdate();
            
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());

        }

    }
    public List<Message> getMessagesByAccount(Account account) {
            Connection connection = ConnectionUtil.getConnection();
            List<Message> messages = new ArrayList<>();

            try {
                String sql = "SELECT * FROM message WHERE posted_by = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, account.getAccount_id());
                ResultSet rs = preparedStatement.executeQuery();
            // Process the result set and map it to message objects
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }
            
    
        return messages;  

    }
}

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message updateMessage(Message msg) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text=? WHERE message_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, msg.message_text);
            ps.setInt(2, msg.message_id);
            
            ps.executeUpdate();

            if (ps.getUpdateCount() != 0) {
                return getMessageFromID(msg.message_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message deleteMessage(int message_id) {
        Message msg = getMessageFromID(message_id);
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM Message WHERE message_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);

            int count = ps.executeUpdate();
            
            if (count > 0) {
                return msg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Message getMessageFromID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message m = new Message(rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                m.setMessage_id(rs.getInt("message_id"));

                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> msgs = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message m = new Message(rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                m.setMessage_id(rs.getInt("message_id"));

                msgs.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return msgs;
    }

    public Message createMessage(Message msg) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, msg.posted_by);
            ps.setString(2, msg.message_text);
            ps.setLong(3, msg.time_posted_epoch);
            
            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            while (pkeyResultSet.next()) {
                msg.setMessage_id((int) pkeyResultSet.getLong(1));
                
                return msg;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}

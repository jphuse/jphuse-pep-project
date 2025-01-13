package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account getAccountFromID(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE account_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, account_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account(rs.getString("username"), rs.getString("password"));
                account.setAccount_id(rs.getInt("account_id"));

                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Account getAccountFromUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account(rs.getString("username"), rs.getString("password"));
                account.setAccount_id(rs.getInt("account_id"));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Account registerAccount(Account acc) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());

            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            while (pkeyResultSet.next()) {
                acc.setAccount_id((int) pkeyResultSet.getLong(1));

                return acc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Message> getAccountMessages(int account_id) {
        List<Message> msgs = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, account_id);

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
    
}

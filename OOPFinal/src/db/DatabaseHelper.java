package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import course_management.*;
public class DatabaseHelper {
    private String url;
    private String user;
    private String password;

    public DatabaseHelper(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void executeUpdate(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet executeQuery(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(query);
            // Note: This ResultSet must be closed by the caller
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void executeSelectQuery(String query) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CourseManagement","root", "Cczw123890//");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print( rsmd.getColumnName(i)+": "+columnValue);
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeSelectQueryWithParams(String query, Object... params) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Setting the parameters for the PreparedStatement
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Executing the query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterating through the result set and printing each row
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                    }
                    System.out.println("");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void executeUpdateWithParams(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

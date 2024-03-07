package testcases;

import dao.UserDAO;
import db.DatabaseHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class UserDAOTest {

    DatabaseHelper connect_db = new DatabaseHelper("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");

    private static UserDAO userDAO;

    @BeforeClass
    public static void globalSetup() {
    }

    @Before
    public void setUp() {
        userDAO = new UserDAO();

        initializeDatabaseWithTestData();
    }

    private void initializeDatabaseWithTestData() {
        try (Connection conn = connect_db.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("insert into user (userId, userName, userPassword, type) VALUES (001, 'testUser1', 'password1', 'STUDENT')");
            stmt.executeUpdate("insert into user (userId, userName, userPassword, type) VALUES (002, 'testUser2', 'password2', 'FACULTY')");
            stmt.executeUpdate("insert into user (userId, userName, userPassword, type) VALUES (003, 'testUser3', 'password3', 'ADMIN')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidUsernameWithExistingUser() {
        String existingUsername = "testUser1";
        int expectedUserId = -1;
        int actualUserId = userDAO.validUsername(existingUsername);
        assertNotEquals("The method should return the correct user ID for an existing username", expectedUserId, actualUserId);
    }

    @Test
    public void testValidUsernameWithNonExistingUser() {
        String nonExistingUsername = "nonExistingUser";
        int result = userDAO.validUsername(nonExistingUsername);
        assertEquals("The method should return -1 for a non-existing username", -1, result);
    }

    @Test
    public void testGetUserIdWithExistingUser() {
        String existingUsername = "testUser1";
        int expectedUserId = -1;
        int actualUserId = userDAO.getUserId(existingUsername);
        assertNotEquals("The method should return the correct user ID for an existing username", expectedUserId, actualUserId);
    }

    @Test
    public void testGetUserIdWithNonExistingUser() {
        String nonExistingUsername = "nonExistingUser";
        int result = userDAO.getUserId(nonExistingUsername);
        assertEquals("The method should return -1 for a non-existing username", -1, result);
    }

    @Test
    public void testGetUserPasswordWithExistingUser() {
        String existingUsername = "testUser1";
        String expectedPassword = "password1";
        String actualPassword = userDAO.getUserPassword(existingUsername);
        assertEquals("The method should return the correct password for an existing username", expectedPassword, actualPassword);
    }

    @Test
    public void testGetUserPasswordWithNonExistingUser() {
        String nonExistingUsername = "nonExistingUser";
        String result = userDAO.getUserPassword(nonExistingUsername);
        assertNull("The method should return null for a non-existing username", result);
    }

    @Test
    public void testUserTypeWithExistingUser() {
        String existingUsername = "testUser1";
        String expectedType = "STUDENT";
        String actualType = userDAO.userType(existingUsername);
        assertEquals("The method should return the correct user type for an existing username", expectedType, actualType);
    }

    @Test
    public void testUserTypeWithNonExistingUser() {
        String nonExistingUsername = "nonExistingUser";
        String result = userDAO.userType(nonExistingUsername);
        assertNull("The method should return null for a non-existing username", result);
    }


    @After
    public void tearDown() {
        try (Connection conn = connect_db.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM user WHERE userName LIKE 'testUser%'");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error cleaning up the database after a test: " + e.getMessage());
        }
    }

}

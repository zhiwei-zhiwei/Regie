package testcases;

import dao.CourseDAO;
import db.DatabaseHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

public class CourseDAOTest {

    DatabaseHelper connect_db = new DatabaseHelper("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");

    private static CourseDAO courseDAO;

    @BeforeClass
    public static void globalSetup() {
    }

    @Before
    public void setUp() {
        courseDAO = new CourseDAO();
        initializeDatabaseWithTestData();
    }

    private void initializeDatabaseWithTestData() {
        try (Connection conn = connect_db.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO courses (courseNum, courseName) VALUES ('C101', 'Test1')");
            stmt.executeUpdate("INSERT INTO courses (courseNum, courseName) VALUES ('C102', 'Test2')");
            stmt.executeUpdate("INSERT INTO courses (courseNum, courseName) VALUES ('C103', 'Test3')");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error setting up the database with test data: " + e.getMessage());
        }
    }


    @Test
    public void testAvailableCourses() {
        List<String> availableCourses = courseDAO.availableCourses();
        assertNotNull("The list of available courses should not be null", availableCourses);
        assertFalse("The list of available courses should not be empty", availableCourses.isEmpty());
    }


    @Test
    public void testAddCourse() {
        String courseNum = "TEST102";
        String courseName = "Test Course 2";
        String sectionNum = "1";
        courseDAO.addCourse(courseNum, courseName, sectionNum);
        assertTrue("Course should be added successfully", checkCourseExists(courseNum, courseName, sectionNum));
    }

    private boolean checkCourseExists(String courseNum, String courseName, String sectionNum) {
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM courses WHERE courseNum = ? AND courseName = ? AND sectionNum = ?")) {
            pstmt.setString(1, courseNum);
            pstmt.setString(2, courseName);
            pstmt.setString(3, sectionNum);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    @Test
    public void testGetCourseIdByValidCourseNum() throws SQLException {
        String validCourseNum = "Test1";
        int expectedCourseId = 1;
        int actualCourseId = courseDAO.getCourseIdByCourseNum(validCourseNum);
        assertNotEquals(expectedCourseId, -1);
    }

    @Test
    public void testGetCourseIdByInvalidCourseNum() throws SQLException {
        String invalidCourseNum = "INVALID123";
        int result = courseDAO.getCourseIdByCourseNum(invalidCourseNum);
        assertEquals("The method should return -1 for an invalid course number", -1, result);
    }

    @Test
    public void testGetCourseSectionNumByValidCourseName() {
        String validCourseName = "Test1";
        String expectedSectionNum = "C101";
        String actualSectionNum = courseDAO.getCourseSectionNumByCourseName(validCourseName);
        assertEquals("The method should return the correct section number for a valid course name", "001", actualSectionNum);
    }

    @Test
    public void testGetCourseCapacityByValidCourseName() throws SQLException {
        String validCourseName = "Test1";
        int expectedCapacity = 5;
        int actualCapacity = courseDAO.getCourseCapacityByCourseName(validCourseName);
        assertEquals("The method should return the correct course capacity for a valid course name", expectedCapacity, actualCapacity);
    }

    @Test
    public void testGetCourseCapacityByInvalidCourseName() throws SQLException {
        String invalidCourseName = "Nonexistent Course";
        int result = courseDAO.getCourseCapacityByCourseName(invalidCourseName);
        assertEquals("The method should return -1 for an invalid course name", -1, result);
    }

    @Test
    public void testGetCourseCapacityByValidCourseNum() throws SQLException {
        String validCourseNum = "c101";
        int expectedCapacity = 5;
        int actualCapacity = courseDAO.getCourseCapacityByCourseNum(validCourseNum);
        assertEquals("The method should return the correct course capacity for a valid course number", expectedCapacity, actualCapacity);
    }

    @Test
    public void testGetCourseCapacityByInvalidCourseNum() throws SQLException {
        String invalidCourseNum = "INVALID101";
        int result = courseDAO.getCourseCapacityByCourseNum(invalidCourseNum);
        assertEquals("The method should return -1 for an invalid course number", -1, result);
    }

    @Test
    public void testIncrementCourseCapacity() throws SQLException {
        String courseName = "Test1";
        String sectionNum = "001";
        int initialCapacity = 0;
        courseDAO.incrementCourseCapacity(courseName, sectionNum);
        int updatedCapacity = getCurrentCourseCapacity(courseName, sectionNum);
        assertEquals("The course capacity should be incremented by 1", initialCapacity + 1, updatedCapacity);
    }

    private int getCurrentCourseCapacity(String courseName, String sectionNum) throws SQLException {
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT currentCourseCapacity FROM courses WHERE courseName = ? AND sectionNum = ?")) {
            pstmt.setString(1, courseName);
            pstmt.setString(2, sectionNum);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("currentCourseCapacity");
                }
            }
        }
        return -1;
    }

    @Test
    public void testGetSectionNumbersByCourseName() {
        String courseName = "Test1";
        List<String> sectionNumbers = courseDAO.getSectionNumbersByCourseName(courseName);
        assertNotNull("The list of section numbers should not be null", sectionNumbers);
        assertEquals("There should be 2 open sections for the course", 1, sectionNumbers.size());
        assertFalse("The list should not contain closed section 'S03'", sectionNumbers.contains("S03"));
    }


    @After
    public void tearDown() {
        try (Connection conn = connect_db.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM courses WHERE courseName LIKE 'Test%'");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error cleaning up the database after a test: " + e.getMessage());
        }
    }


}

package testcases;

import course_management.CourseManager;
import course_management.Course;
import db.DatabaseHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class CourseManagerTest {

    private CourseManager courseManager;
    private Connection connection;
    DatabaseHelper connect_db = new DatabaseHelper("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");


    @Before
    public void setUp() throws Exception {
        courseManager = CourseManager.getInstance();
        String url = "jdbc:mysql://localhost:3306/CourseManagement";
        String user = "root";
        String password = "Cczw123890//";
        connection = DriverManager.getConnection(url, user, password);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO courses (courseNum, courseName, sectionNum) VALUES ('TEST101', 'Test1', '02')");
            stmt.execute("INSERT INTO courses (courseNum, courseName, sectionNum) VALUES ('TEST102', 'Test2', '03')");
            stmt.execute("INSERT INTO courses (courseNum, courseName, sectionNum) VALUES ('TEST103', 'Test3', '04')");
            stmt.execute("INSERT INTO courses (courseNum, courseName, sectionNum, currentCourseCapacity, courseCapacity) VALUES ('TEST106', 'Test_Full Course', '01', 30, 30)");
            stmt.execute("INSERT INTO courses (courseNum, courseName, sectionNum, currentCourseCapacity, courseCapacity) VALUES ('TEST107', 'Test_Available Course', '01', 20, 30)");
        }
    }

    @Test
    public void testGetIdByNum() {
        try {
            String testCourseNum = "TEST101";
            int courseId = courseManager.getIdByNum(testCourseNum);
            String query = "SELECT id FROM courses WHERE courseNum = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, testCourseNum);
                var rs = pstmt.executeQuery();
                if (rs.next()) {
                    int expectedId = rs.getInt("id");
                    assertEquals(expectedId, courseId);
                } else {
                    fail("Test course not found in database.");
                }
            }
        } catch (Exception e) {
            fail("Exception should not have been thrown.");
        }
    }

    @Test
    public void testGetIdByName() {
        try {
            String testCourseName = "Test1";
            int courseId = courseManager.getIdByName(testCourseName);
            String query = "SELECT id FROM courses WHERE courseName = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, testCourseName);
                var rs = pstmt.executeQuery();
                if (rs.next()) {
                    int expectedId = rs.getInt("id");
                    assertEquals(expectedId, courseId);
                } else {
                    fail("Test course not found in the database.");
                }
            }
        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCourseNumByName() {
        try {
            String expectedCourseNum = "TEST101";
            String courseName = "Test1";
            String actualCourseNum = courseManager.getCourseNumByName(courseName);
            assertEquals(expectedCourseNum, actualCourseNum);
        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }


    @Test
    public void testIsAValidSectionNum_ValidSection() {
        assertTrue(courseManager.isAValidSectionNUm("02", "Test1"));
    }

    @Test
    public void testIsAValidSectionNum_InvalidSection() {
        assertFalse(courseManager.isAValidSectionNUm("05", "Test1"));
    }

    @Test
    public void testCourseHasLabs_WithoutLabs() {
        assertFalse(courseManager.courseHasLabs("Non-Lab Course"));
    }


    @Test
    public void testIsCourseFull_FullCourse() {
        // Test with a course name that is full
        assertTrue(courseManager.isCourseFull("Test_Full Course"));
    }

    @Test
    public void testIsCourseFull_AvailableCourse() {
        // Test with a course name that is not full
        assertFalse(courseManager.isCourseFull("Test_Available Course"));
    }

    @Test
    public void testValidSectionNumbersOfCourseName_Valid() {
        assertTrue(courseManager.validSectionNumbersOfCourseName("Test1", "02"));
    }

    @Test
    public void testValidSectionNumbersOfCourseName_Invalid() {
        assertFalse(courseManager.validSectionNumbersOfCourseName("CourseWithSections", "03"));
    }


    @After
    public void tearDown() throws Exception {
        try (Connection conn = connect_db.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM courses WHERE courseName LIKE 'Test%'");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error cleaning up the database after a test: " + e.getMessage());
        }
    }
}

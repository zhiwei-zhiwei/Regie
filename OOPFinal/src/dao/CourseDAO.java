package dao;

import course_management.Course;
import db.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    DatabaseHelper connect_db = new DatabaseHelper("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");

    public void availableCourse() {
        String sql = "SELECT * " +
                "FROM courses " +
                "WHERE currentCourseCapacity < courseCapacity;";
        DatabaseHelper.executeSelectQuery(sql);
    }


    public List<String> availableCourses() {
        List<String> availableCourses = new ArrayList<>();
        String sql = "SELECT courseName FROM courses WHERE currentCourseCapacity < courseCapacity";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String courseName = rs.getString("courseName");
                availableCourses.add(courseName);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving available courses: " + e.getMessage());
        }

        return availableCourses;
    }

    public void addCourse(String courseNum, String courseName, String sectionNum) {
        String sql = "INSERT INTO courses (courseNum, courseName,sectionNum ) VALUES (?, ?, ?);";
        connect_db.executeUpdateWithParams(sql, courseNum, courseName, sectionNum);
    }

    public void addCourseWithCapacity(String courseNum, String courseName, String sectionNum, int capacity) {
        String sql = "INSERT INTO courses (courseNum, courseName, sectionNum, courseCapacity) VALUES (?, ?, ?, ?);";

        connect_db.executeUpdateWithParams(sql, courseNum, courseName, sectionNum, capacity);
    }

    public void getAllCourses() {
        String sql = "SELECT * FROM courses";
        DatabaseHelper.executeSelectQuery(sql);
    }

    public void getAllCourse() {
        String sql = "SELECT * FROM courses";
        DatabaseHelper.executeSelectQuery(sql);
//        try (Connection conn = connect_db.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                // Assuming your courses table has columns: id, courseNum, courseName, sectionNum, courseCapacity, currentCourseCapacity
//                int id = rs.getInt("id");
//                String courseNum = rs.getString("courseNum");
//                String courseName = rs.getString("courseName");
//                String sectionNum = rs.getString("sectionNum");
//                int courseCapacity = rs.getInt("courseCapacity");
//                int currentCourseCapacity = rs.getInt("currentCourseCapacity");
//
//                System.out.println("ID: " + id + ", Course Number: " + courseNum + ", Course Name: " + courseName +
//                        ", Section Number: " + sectionNum + ", Capacity: " + courseCapacity +
//                        ", Current Enrollment: " + currentCourseCapacity);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving all courses: " + e.getMessage());
//        }
    }


    public void getALLCourseWithLab() {
        String sql = "SELECT c.id         AS CourseID,\n" +
                "       c.courseNum  AS CourseNumber,\n" +
                "       c.courseName AS CourseName,\n" +
                "       c.sectionNum AS sectionNum,\n" +
                "       l.labNum     AS LabNum\n" +
                "FROM courses c\n" +
                "         LEFT JOIN\n" +
                "     lab l ON c.id = l.course_id;";
        DatabaseHelper.executeSelectQuery(sql);
    }

    public int getCourseIdByCourseNum(String courseNum) throws SQLException {
        String sql = "SELECT id FROM courses WHERE courseNum = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseNum);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCourseIdByCourseName(String courseName) {
        String sql = "SELECT id FROM courses WHERE courseName = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCourseIdBySectionNum(String sectionNum, String courseName) {
        String sql = "SELECT id FROM courses WHERE sectionNum = ? and courseName = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sectionNum);
            pstmt.setString(2, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getCourseNumByCourseName(String courseName) {
        String sql = "SELECT courseNum FROM courses WHERE courseName = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("courseNum");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCourseSectionNumByCourseName(String courseName) {
        String sql = "SELECT sectionNum FROM courses WHERE courseName = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("sectionNum");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCourseCapacityByCourseName(String courseName) throws SQLException {
        String sql = "SELECT courseCapacity FROM courses WHERE courseName = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("courseCapacity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCourseCapacityByCourseNum(String courseNum) throws SQLException {
        String sql = "SELECT courseCapacity FROM courses WHERE courseNum = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseNum);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("courseCapacity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void incrementCourseCapacity(String courseName, String sectionNum) {
        String sql = "UPDATE courses SET currentCourseCapacity = currentCourseCapacity + 1 WHERE courseName = ? and sectionNum = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setString(2, sectionNum);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Successfully incremented capacity for course: " + courseName);
            } else {
                System.out.println("No course found with name: " + courseName);
            }
        } catch (SQLException e) {
            System.out.println("Error incrementing course capacity: " + e.getMessage());
        }
    }

    public void assignCourseLocation(int courseId, String building, String room) throws SQLException {
        String sql = "INSERT INTO courseLocation (building, room, course_id) VALUES (?, ?, ?);";
        connect_db.executeUpdateWithParams(sql, building, room, courseId);

    }

    public void assignCourseSchedule(int courseId, String day, String startTime, String endTime) throws SQLException {
        String sql = "INSERT INTO courseSchedule (day, startTime, endTime, course_id) VALUES (?, ?, ?, ?);";
        connect_db.executeUpdateWithParams(sql, day, startTime, endTime, courseId);
    }

    public void getCourseLocationAndScheduleByCourseID(int courseID) {
        String sql = "SELECT c.id AS CourseID, " +
                "c.courseNum AS CourseNumber, " +
                "c.courseName AS CourseName, " +
                "cl.building AS Building," +
                "cl.room AS Room, " +
                "cs.day AS Day, " +
                "cs.startTime AS StartTime, " +
                "cs.endTime AS EndTime " +
                "FROM courses c " +
                "LEFT JOIN courseLocation cl ON c.id = cl.course_id " +
                "LEFT JOIN courseSchedule cs ON c.id = cs.course_id " +
                "WHERE c.id = ?;";
        DatabaseHelper.executeSelectQueryWithParams(sql, courseID);
    }

    public void deleteCourse(int courseID) throws SQLException {
        Connection conn = null;
        try {
            conn = connect_db.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Define all SQL statements required for deletion
            String[] sqlStatements = {
                    "DELETE FROM labEnrollments WHERE course_id = ?;",
                    "DELETE FROM sectionStudents WHERE section_id IN (SELECT id FROM lab WHERE course_id = ?);",
                    "DELETE FROM sectionSchedule WHERE section_id IN (SELECT id FROM lab WHERE course_id = ?);",
                    "DELETE FROM sectionLocation WHERE section_id IN (SELECT id FROM lab WHERE course_id = ?);",
                    "DELETE FROM sectionFaculty WHERE section_id IN (SELECT id FROM lab WHERE course_id = ?);",
                    "DELETE FROM lab WHERE course_id = ?;",
                    "DELETE FROM courseEnrollments WHERE courseNum IN (SELECT courseNum FROM courses WHERE id = ?);",
                    "DELETE FROM courseStudents WHERE course_id = ?;",
                    "DELETE FROM courseSchedule WHERE course_id = ?;",
                    "DELETE FROM courseLocation WHERE course_id = ?;",
                    "DELETE FROM courseFaculty WHERE course_id = ?;",
                    "DELETE FROM courses WHERE id = ?;"
            };

            for (String sql : sqlStatements) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, courseID);
                    // For statements that might require setting the same parameter multiple times,
                    // ensure you set them as needed here.
                    pstmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Rethrow or handle as needed
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }



    public int isCourseFull(String courseName) {
        String sql = "SELECT courseCapacity - currentCourseCapacity as availableSpace " +
                "FROM courses " +
                "WHERE courseName = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("availableSpace");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isCourseAvailable(String courseName) {
        String sql = "SELECT COUNT(*) AS courseCount FROM courses WHERE courseName = ? AND currentCourseCapacity < courseCapacity;";


        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Check if there's at least one course that matches the criteria
                    int courseCount = rs.getInt("courseCount");
                    return courseCount > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking course availability: " + e.getMessage());
        }

        // Return false if the course was not found or an error occurred
        return false;
    }

    public void closeCourseByName(String courseName) {
        String sql = "UPDATE courses SET status = 'Closed' WHERE courseName = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Course status updated to 'Closed' for: " + courseName);
            } else {
                System.out.println("Course not found or already closed: " + courseName);
            }
        } catch (SQLException e) {
            System.out.println("Error updating course status: " + e.getMessage());
        }
    }

    public void coursesLessThan5Enrolled() {
        String sql = """
                SELECT id,
                       courseNum,
                       courseName,
                       courseCapacity,
                       currentCourseCapacity
                FROM courses
                WHERE currentCourseCapacity < 5;""";
        DatabaseHelper.executeSelectQuery(sql);
    }

    public void removeCourseLessThan5Enrolled() throws SQLException {
        Connection conn = null;
        List<Integer> courseIds = new ArrayList<>();

        try {
            conn = connect_db.getConnection();

            // SQL to find courses with currentCourseCapacity less than 5
            String sql = "SELECT id FROM courses WHERE currentCourseCapacity < 5";

            // Execute query and collect course IDs
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    courseIds.add(rs.getInt("id"));
                }
            }

            // Iterate over each course ID and call deleteCourse
            for (int courseId : courseIds) {
                deleteCourse(courseId);  // Call existing deleteCourse function
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean courseHasLabs(String courseName) {
        String sql = "SELECT COUNT(l.id) AS labCount " +
                "FROM lab l " +
                "JOIN courses c ON l.course_id = c.id " +
                "WHERE c.courseName = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int labCount = rs.getInt("labCount");
                    return labCount > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking for lab: " + e.getMessage());
        }

        return false;
    }

    public void getCoursesLab(String courseName) {
        String sql = """
                SELECT l.id                            AS LabID,
                       l.labNum                        AS LabNumber,
                       l.labCapacity - l.currentLabCapacity AS RemainingSpace,
                       c.id                            AS CourseID,
                       c.courseName                    AS CourseName
                FROM lab l
                         JOIN courses c ON l.course_id = c.id
                WHERE c.courseName = ?;""";
        DatabaseHelper.executeSelectQueryWithParams(sql, courseName);
    }

    public void getAvailableCourseLab(String courseName) {
        String sql = """
                SELECT l.id                             AS LabID,
                       l.labNum                         AS LabNumber,
                       l.labCapacity - l.currentLabCapacity AS RemainingSpace,
                       c.id                             AS CourseID,
                       c.courseName                     AS CourseName
                FROM lab l
                         JOIN courses c ON l.course_id = c.id
                WHERE c.courseName = ?
                  AND l.labCapacity > l.currentLabCapacity;""";
        DatabaseHelper.executeSelectQueryWithParams(sql, courseName);
    }

    public List<String> getSectionNumbersByCourseName(String courseName) {
        List<String> sectionNumbers = new ArrayList<>();
        String sql = "SELECT sectionNum FROM courses WHERE courseName = ? and status = 'open'";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String sectionNum = rs.getString("sectionNum");
                    sectionNumbers.add(sectionNum);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving section numbers: " + e.getMessage());
        }

        return sectionNumbers;
    }


    public List<Course> getCoursesByName(String courseName) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT courseNum, courseName, sectionNum, courseCapacity,currentCourseCapacity FROM courses WHERE courseName = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Assuming a Course class exists that matches the schema of the courses table
                    Course course = new Course(
                            rs.getString("courseNum"),
                            rs.getString("courseName"),
                            rs.getString("sectionNum"),
                            rs.getInt("courseCapacity"),
                            rs.getInt("currentCourseCapacity")
                            // Add other fields from your table as needed
                    );
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving courses by name: " + e.getMessage());
        }

        return courses;
    }

    public List<String> findScheduleAndLocationConflicts() {
        List<String> conflicts = new ArrayList<>();
        String sql = "SELECT 'Course-Course' AS conflict_type, cs1.course_id AS entity1_id, cs2.course_id AS entity2_id, cs1.day, cs1.startTime, cs1.endTime, cl1.building, cl1.room " +
                "FROM courseSchedule cs1 " +
                "JOIN courseLocation cl1 ON cs1.course_id = cl1.course_id " +
                "JOIN courseSchedule cs2 ON cs1.day = cs2.day " +
                "JOIN courseLocation cl2 ON cs2.course_id = cl2.course_id AND cl1.building = cl2.building AND cl1.room = cl2.room " +
                "WHERE cs1.course_id != cs2.course_id AND " +
                "((cs1.startTime <= cs2.startTime AND cs1.endTime > cs2.startTime) OR " +
                "(cs2.startTime <= cs1.startTime AND cs2.endTime > cs1.startTime)) " +
                "UNION " +
                "SELECT 'Section-Section' AS conflict_type, ss1.section_id AS entity1_id, ss2.section_id AS entity2_id, ss1.day, ss1.startTime, ss1.endTime, sl1.building, sl1.room " +
                "FROM sectionSchedule ss1 " +
                "JOIN sectionLocation sl1 ON ss1.section_id = sl1.section_id " +
                "JOIN sectionSchedule ss2 ON ss1.day = ss2.day " +
                "JOIN sectionLocation sl2 ON ss2.section_id = sl2.section_id AND sl1.building = sl2.building AND sl1.room = sl2.room " +
                "WHERE ss1.section_id != ss2.section_id AND " +
                "((ss1.startTime <= ss2.startTime AND ss1.endTime > ss2.startTime) OR " +
                "(ss2.startTime <= ss1.startTime AND ss2.endTime > ss1.startTime)) " +
                "UNION " +
                "SELECT 'Course-Section' AS conflict_type, cs.course_id AS entity1_id, ss.section_id AS entity2_id, cs.day, cs.startTime, cs.endTime, cl.building, cl.room " +
                "FROM courseSchedule cs " +
                "JOIN courseLocation cl ON cs.course_id = cl.course_id " +
                "JOIN sectionSchedule ss ON cs.day = ss.day " +
                "JOIN sectionLocation sl ON ss.section_id = sl.section_id AND cl.building = sl.building AND cl.room = sl.room " +
                "WHERE " +
                "((cs.startTime <= ss.startTime AND cs.endTime > ss.startTime) OR " +
                "(ss.startTime <= cs.startTime AND ss.endTime > cs.startTime))";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String conflictType = rs.getString("conflict_type");
                    int entity1Id = rs.getInt("entity1_id");
                    int entity2Id = rs.getInt("entity2_id");
                    String day = rs.getString("day");
                    String startTime = rs.getString("startTime");
                    String endTime = rs.getString("endTime");
                    String building = rs.getString("building");
                    String room = rs.getString("room");

                    String conflictDetails = String.format("Conflict Type: %s, Entity1 ID: %d, Entity2 ID: %d, Day: %s, Time: %s-%s, Location: %s %s",
                            conflictType, entity1Id, entity2Id, day, startTime, endTime, building, room);
                    conflicts.add(conflictDetails);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conflicts;
    }

    public void printConflicts() {
        List<String> conflicts = findScheduleAndLocationConflicts();
        if (conflicts.isEmpty()) {
            System.out.println("No conflicts found.");
        } else {
            System.out.println("Conflicts found:");
            for (String conflict : conflicts) {
                System.out.println(conflict);
            }
        }
    }

    public boolean courseNameExists(String courseName) {
        String sql = "SELECT COUNT(*) AS count FROM courses WHERE courseName = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking if course name exists: " + e.getMessage());
        }
        return false;
    }

    public boolean courseNumExists(String courseNum) {
        String sql = "SELECT COUNT(*) AS count FROM courses WHERE courseNum = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseNum);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking if course num exists: " + e.getMessage());
        }
        return false;
    }


}

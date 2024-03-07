package dao;

import course_management.Schedule;
import db.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    DatabaseHelper connect_db = new DatabaseHelper("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");

    public int validUsername(String userName) {
        String sql = "SELECT id FROM user where userName = ?";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
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

    public int getUserId(String userName) {
        String sql = "SELECT id FROM user where userName = ?";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
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

    public String getUserPassword(String userName){
        String sql = "SELECT userPassword FROM user where userName = ?";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("userPassword");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean enrollInCourse(String courseNum, String courseName, Integer userId, String sectionNum) {
        String sql = "INSERT INTO courseEnrollments (courseNum, courseName, userId, sectionNum) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseNum);
            pstmt.setString(2, courseName);
            if (userId != null) {
                pstmt.setInt(3, userId);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER); // Assuming userId is an INTEGER in the DB
            }
            pstmt.setString(4, sectionNum);

            int affectedRows = pstmt.executeUpdate();

            // Check if the insert was successful
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enrollLab(String labNum, Integer courseId, Integer userId) {
        // First, get the course ID from the course number
//        int courseId = getCourseIdByCourseNum(courseNum);


        String sql = "INSERT INTO labEnrollments (labNum, course_id, userId) VALUES (?, ?, ?)";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, labNum);
            pstmt.setInt(2, courseId); // Set the course ID
            if (userId != null) {
                pstmt.setInt(3, userId);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER); // Assuming userId is an INTEGER in the DB
            }

            int affectedRows = pstmt.executeUpdate();

            // Check if the insert was successful
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int countEnrolledCourses(int userId) {
        String sql = "SELECT COUNT(*) AS courseCount FROM courseEnrollments WHERE userId = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("courseCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if the user is not enrolled in any courses or an error occurs
    }



    public String userType(String userName) {
        String sql = "SELECT type FROM user where userName = ?";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUserAlreadyEnrolledInCourse(String courseName, int userId) {
        String sql = "SELECT COUNT(*) AS enrollmentCount FROM courseEnrollments WHERE courseName = ? AND userId = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Check if there's at least one enrollment record for this user and course
                    int enrollmentCount = rs.getInt("enrollmentCount");
                    return enrollmentCount > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking user enrollment: " + e.getMessage());
        }

        // Return false if the user is not enrolled in the course or an error occurred
        return false;
    }

    public boolean hasTimeConflict(int userId) throws SQLException {
        String query = "SELECT cs.day, cs.startTime, cs.endTime " +
                "FROM courseEnrollments ce " +
                "JOIN courses c ON ce.courseNum = c.courseNum " +
                "JOIN courseSchedule cs ON c.id = cs.course_id " +
                "WHERE ce.userId = ? AND ce.sectionNum = c.sectionNum";

        List<Schedule> schedules = new ArrayList<>();

        try (Connection conn = connect_db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String day = rs.getString("day");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                schedules.add(new Schedule(day, startTime, endTime));
            }
        }

        for (int i = 0; i < schedules.size(); i++) {
            for (int j = i + 1; j < schedules.size(); j++) {
                if (conflictsWith(schedules.get(i), schedules.get(j))) {
                    return true; // Found a conflict
                }
            }
        }

        return false;
    }

    private boolean conflictsWith(Schedule schedule1, Schedule schedule2) {
        if (!schedule1.getDays().equals(schedule2.getDays())) {
            return false; // Different days, no conflict
        }

        // Convert startTime and endTime from String to Time for comparison
        Time start1 = Time.valueOf(schedule1.getStartTime() + ":00");
        Time end1 = Time.valueOf(schedule1.getEndTime() + ":00");
        Time start2 = Time.valueOf(schedule2.getStartTime() + ":00");
        Time end2 = Time.valueOf(schedule2.getEndTime() + ":00");

        // Check for time overlap
        return !(end1.before(start2) || start1.after(end2));
    }

    public void currentSchedule(int userId) {
        String sql = "SELECT c.courseName, c.sectionNum, cs.day, cs.startTime, cs.endTime, cl.building, cl.room " +
                "FROM courseEnrollments ce " +
                "JOIN courses c ON ce.courseNum = c.courseNum " +
                "LEFT JOIN courseSchedule cs ON c.id = cs.course_id " +
                "LEFT JOIN courseLocation cl ON c.id = cl.course_id " +
                "WHERE ce.userId = ? AND ce.sectionNum = c.sectionNum";
        DatabaseHelper.executeSelectQueryWithParams(sql, userId);
    }


}

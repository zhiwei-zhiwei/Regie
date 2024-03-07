package dao;

import course_management.Locations;
import course_management.Schedule;
import db.DatabaseHelper;

import java.sql.*;

public class LabDAO {

    DatabaseHelper connect_db = new DatabaseHelper("jdbc:mysql://localhost:3306/CourseManagement", "root", "Cczw123890//");

    public void addSectionToCourse(String labNum, int courseId, int capacity) {
        String sql = "INSERT INTO lab (labNum, course_id, labCapacity) VALUES (?, ?, ?);";
        connect_db.executeUpdateWithParams(sql, labNum, courseId, capacity);
    }

    public void assignSectionLocation(int sectionId, Locations locations) {
        String sql = "INSERT INTO sectionLocation (building, room, section_id) VALUES (?, ?, ?);";
        connect_db.executeUpdateWithParams(sql, locations.getBuilding(), locations.getRoom(), sectionId);

    }

    public void assignSectionSchedule(int sectionId, Schedule schedule) {
        String sql = "INSERT INTO sectionSchedule (day, startTime, endTime, section_id) VALUES (?, ?, ?, ?);";
        connect_db.executeUpdateWithParams(sql, schedule.getDays(), schedule.getStartTime(), schedule.getEndTime(), sectionId);
    }

    public void getLabLocationAndScheduleByLabID(int sectionId) {
        String sql = """
                SELECT
                    l.id AS LabID,
                    l.labNum AS LabNumber,
                    sl.building AS Building,
                    sl.room AS Room,
                    ss.day AS Day,
                    ss.startTime AS StartTime,
                    ss.endTime AS EndTime
                FROM
                    lab l
                        JOIN sectionLocation sl ON l.id = sl.section_id
                        JOIN sectionSchedule ss ON l.id = ss.section_id
                WHERE
                    l.id = ?;""";
        connect_db.executeSelectQueryWithParams(sql, sectionId);
    }

    public int getSectionIdByCourseNum(String sectionNum) throws SQLException {
        String sql = "SELECT id FROM lab WHERE labNum = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sectionNum);
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

    public int validLabNum(String labNum) {
        String sql = "SELECT id FROM lab where labNum = ?";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, labNum);
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

    public boolean incrementLabCapacity(String labNum) {
        String sql = "UPDATE lab SET currentLabCapacity = currentLabCapacity + 1 WHERE labNum = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, labNum);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error incrementing lab capacity: " + e.getMessage());
            return false;
        }
    }

    public int isLabFull(String labNum) {
        String sql = "SELECT labCapacity - currentLabCapacity as availableSpace " +
                "FROM lab " +
                "WHERE labNum = ?;";
        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, labNum);
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

    public void closeByName(String labNum) {
        String sql = "UPDATE lab SET status = 'Closed' WHERE labNum = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, labNum);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Course status updated to 'Closed' for: " + labNum);
            } else {
                System.out.println("Course not found or already closed: " + labNum);
            }
        } catch (SQLException e) {
            System.out.println("Error updating course status: " + e.getMessage());
        }
    }

    public boolean labNumExists(String labNum, int courseId) {
        String sql = "SELECT COUNT(*) AS count FROM lab WHERE labNum = ? AND course_id = ?";

        try (Connection conn = connect_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, labNum);
            pstmt.setInt(2, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking if lab number exists: " + e.getMessage());
        }
        return false;
    }




}

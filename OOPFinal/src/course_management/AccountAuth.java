package course_management;

import dao.UserDAO;

import java.sql.SQLException;

public class AccountAuth {
    UserDAO userDAO = new UserDAO();

    public boolean isStudentUserNameValid(String userName) {
        return userDAO.validUsername(userName) != -1;
    }

    public boolean isStudentPasswordMatches(String userName, String inputPassword) {
        return userDAO.getUserPassword(userName).equals(inputPassword);
    }

    public String getUserType(String userName) {
        return userDAO.userType(userName);
    }

    public int getId(String userName) {
        return userDAO.getUserId(userName);
    }

    public int enrollNum(String userName) {
        int count = userDAO.countEnrolledCourses(userDAO.getUserId(userName));
        return count;
    }

    public boolean isUserAlreadyEnrolledInCourse(String courseName, int userId){
        return userDAO.isUserAlreadyEnrolledInCourse(courseName, userId);
    }

    public void enrollACourse(String courseNum, String courseName, Integer userId, String sectionNum) {
        userDAO.enrollInCourse(courseNum, courseName, userId, sectionNum);
    }

    public void enrollALab(String labNum, Integer courseId, Integer userId) {
        userDAO.enrollLab(labNum, courseId, userId);
    }

    public void currentSchedule(int userId){
        userDAO.currentSchedule(userId);
    }

    public boolean hasTimeWarning(int userId) throws SQLException {
        return userDAO.hasTimeConflict(userId);
    }
}

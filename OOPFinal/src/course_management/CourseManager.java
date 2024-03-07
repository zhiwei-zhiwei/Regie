package course_management;

import interfaces.*;

import dao.CourseDAO;

import java.sql.SQLException;
import java.util.List;

public class CourseManager implements ICourseAddManagement, ICourseStatus{

    private static CourseManager instance;
    private CourseDAO courseDAO;

    private CourseManager() {
        courseDAO = new CourseDAO();
    }

    public static CourseManager getInstance() {
        if (instance == null) {
            instance = new CourseManager();
        }
        return instance;
    }


    // Method to add a course remains the same as previously shown
    @Override
    public void addCourse(Course course){
        courseDAO.addCourse(course.getCourseNum(), course.getCourseName(), course.getSectionNum());
    }
    @Override
    public void addCourseWithCapacity(Course course){
        courseDAO.addCourseWithCapacity(course.getCourseNum(), course.getCourseName(), course.getSectionNum(), course.getCourseCapacity());
    }
    @Override
    public void openedCourses() {
        courseDAO.availableCourse();
    }
    @Override
    public void getAllCourses() {
        courseDAO.getAllCourse();
    }
    public void getCoursesWithLabs() {
        // Again, directly call the DAO method and process as needed
        courseDAO.getALLCourseWithLab();
    }
    public int getIdByNum(String courseId) throws SQLException {
        return courseDAO.getCourseIdByCourseNum(courseId);
    }
    public int getIdByName(String courseName) {
        return courseDAO.getCourseIdByCourseName(courseName);
    }

    public String getCourseNumByName(String courseName){
        return courseDAO.getCourseNumByCourseName(courseName);
    }

    public String getCourseSectionNumByCourseName(String courseName){
        return courseDAO.getCourseSectionNumByCourseName(courseName);
    }

    public boolean isAValidSectionNUm(String sectionNum, String courseName){
        return courseDAO.getCourseIdBySectionNum(sectionNum, courseName)!=-1;
    }

    public int getIdByCourse(Course course) throws  SQLException {
        return courseDAO.getCourseIdByCourseName(course.getCourseName());
    }

    public boolean courseHasLabs(String courseName) {
        return courseDAO.courseHasLabs(courseName);
    }
    public void deleteCourseByNum(String courseNum) {
        try {
            int courseId = courseDAO.getCourseIdByCourseNum(courseNum);
            if (courseId != -1) {
                courseDAO.deleteCourse(courseId);
                System.out.println("Course successfully deleted.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteCourseByName(String courseName) {
        try {
            int courseId = courseDAO.getCourseIdByCourseName(courseName);
            if (courseId != -1) {
                courseDAO.deleteCourse(courseId);
                System.out.println("Course successfully deleted.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteCourseByCourse(Course course) {
        try {
            int courseId = courseDAO.getCourseIdByCourseName(course.getCourseName());
            if (courseId != -1) {
                courseDAO.deleteCourse(courseId);
                System.out.println("Course successfully deleted.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void incrementCapacity(String courseName, String sectionNUm) {
        courseDAO.incrementCourseCapacity(courseName, sectionNUm);
    }

    public boolean isCourseFull(String courseName) {
        List<String> availableCourses = courseDAO.availableCourses(); // Use the modified availableCourses method
        return !availableCourses.contains(courseName);
    }

    public void closedCourse(String courseName) {
        courseDAO.closeCourseByName(courseName);
    }
    public void courseLessThan5Enrolled() {
        courseDAO.coursesLessThan5Enrolled();
    }

    public void removeCourseLessThan5Enrolled() throws SQLException {
        courseDAO.removeCourseLessThan5Enrolled();
    }

    public void getCourseSection(String courName) {
        courseDAO.getCoursesLab(courName);
    }

    public void getAllCourseWithLabs() {courseDAO.getALLCourseWithLab();};

    public void getAvailableCourseLab(String courseName) {
        courseDAO.getAvailableCourseLab(courseName);
    }

    public void getSectionNumbersByCourseName(String courseName){
        List<String> sectionNumbers = courseDAO.getSectionNumbersByCourseName(courseName);
        for (String sectionNum : sectionNumbers) {
            System.out.println("VALID: ----  Course: "+courseName+" - Section Number: " + sectionNum);
        }
    }

    public boolean validSectionNumbersOfCourseName(String courseName, String input){
        List<String> sectionNumbers = courseDAO.getSectionNumbersByCourseName(courseName);
        for (String sectionNum : sectionNumbers) {
            if (sectionNum.equals(input)){
                return true;
            }
        }
        return false;
    }

    public boolean uniqueCourseName(String courseName){
        return courseDAO.courseNameExists(courseName);
    }

    public boolean uniqueCourseNum(String courseNum){
        return courseDAO.courseNumExists(courseNum);
    }

    public void assignLocation(int courseId, Locations locations) throws SQLException {
        courseDAO.assignCourseLocation(courseId, locations.getBuilding(), locations.getRoom());
    }

    public void assignSchedule(int courseId, Schedule schedule) throws SQLException {
        courseDAO.assignCourseSchedule(courseId,schedule.getDays(),schedule.getStartTime(),schedule.getEndTime());
    }

}

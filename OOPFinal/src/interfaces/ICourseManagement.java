package interfaces;

import course_management.Course;

import java.sql.SQLException;

public interface ICourseManagement {
    void addCourse(Course course);
    void addCourseWithCapacity(Course course);
    void openedCourses();
    void getAllCourses();
    void getCoursesWithSections();
}

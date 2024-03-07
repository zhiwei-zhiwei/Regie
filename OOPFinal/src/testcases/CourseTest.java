package testcases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import course_management.*;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    private Course course;

    Faculty faculty = new Faculty("Mark");

    Locations classroom = new Locations("JCL","390");

    Schedule schedule = new Schedule("Mon", "1:00", "2:00");
    @BeforeEach
    void setUp() {
        course = new Course("12345", "OOP", "Section 1");
        course.setFaculty(faculty);
        course.setLocation(classroom);
        course.setSchedules(schedule);
    }

    @Test
    void testCourseHasName() {
        assertEquals("OOP", course.getCourseName());
    }

    @Test
    void testCourseHasCapacity() {
        assertEquals(5, course.getCourseCapacity());
    }

    @Test
    void testCourseIsOpen() {
        assertEquals("Open", course.getStatus());
    }

    @Test
    void testCourseTimePeriodAndLocation() {
        assertEquals("JCL", course.getLocation().getBuilding());
        assertEquals("390", course.getLocation().getRoom());
        assertEquals("Mon", course.getSchedules().getDays());
        assertEquals("1:00", course.getSchedules().getStartTime());
        assertEquals("2:00", course.getSchedules().getEndTime());
    }

    @Test
    void testCourseHasFaculty(){
        assertEquals(course.getFaculty().getName(), faculty.getName());
    }

    @Test
    void testCourseHasRoom(){
        assertEquals(course.getLocation().getRoom(), classroom.getRoom());
    }

    @Test
    void testEnrollStudents() throws CapacityException {
        Student student = new Student("100","Student A");
        course.enrolledStudents(student);
        assertTrue(course.getEnrolledStudents().contains(student));
    }

    @Test
    void testEnrollmentLimitation() throws CapacityException {
        Student student1 = new Student("100","Student 1");
        Student student2 = new Student("100","Student 2");
        Student student3 = new Student("100","Student 3");
        Student student4 = new Student("100","Student 3");
        Student student5 = new Student("100","Student 3");
        course.enrolledStudents(student1);
        course.enrolledStudents(student2);
        course.enrolledStudents(student3);
        course.enrolledStudents(student4);
        course.enrolledStudents(student5);
        Student student6 = new Student("100","Student 3");
        Exception exception = assertThrows(CapacityException.class, () -> {
            course.enrolledStudents(student6);
        });
        String message = "The Course is CLOSED, Out of course capacity";
        String returnMessage = exception.getMessage();

        assertEquals(message, returnMessage);
    }
}


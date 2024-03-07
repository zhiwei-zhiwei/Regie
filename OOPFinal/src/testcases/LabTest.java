package testcases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import course_management.*;

import static org.junit.jupiter.api.Assertions.*;

public class LabTest {
    private Lab lab;

    Faculty faculty = new Faculty("Mark");

    Locations classroom = new Locations("JCL","390");

    Schedule schedule = new Schedule("Firday", "1:00", "2:00");

    @BeforeEach
    void setUp() {
        lab = new Lab("OOP-01");
        lab.setFaculty(faculty);
        lab.setLocations(classroom);
        lab.setSchedule(schedule);
    }

    @Test
    void testSectionWithSectionNumber() {
        assertEquals("OOP-01", lab.getLabNum());
    }

    @Test
    void testSectionWithCapacity() {
        assertEquals(5, lab.getSectionCapacity());
    }

    @Test
    void testSectionWithStatus() {
        assertEquals("Open", lab.getStatus());
    }

    @Test
    void testSectionLocationAndTimeLocation() {
        assertEquals("JCL", lab.getLocations().getBuilding());
        assertEquals("390", lab.getLocations().getRoom());
        assertEquals("Firday", lab.getSchedule().getDays());
        assertEquals("1:00", lab.getSchedule().getStartTime());
        assertEquals("2:00", lab.getSchedule().getEndTime());
    }

    @Test
    void testSectionHasFaculty(){
        assertEquals(lab.getFaculty().getName(), faculty.getName());
    }

    @Test
    void testSectionHasRoom(){
        assertEquals(lab.getLocations().getRoom(), classroom.getRoom());
    }

    @Test
    void testEnrollStudents() throws CapacityException {
        Student student = new Student("100","Student A");
        lab.enrolledStudents(student);
        assertTrue(lab.getEnrolledStudents().contains(student));
    }

    @Test
    void testEnrollmentLimitation() throws CapacityException {
        Student student1 = new Student("100","Student 1");
        Student student2 = new Student("100","Student 2");
        Student student3 = new Student("100","Student 3");
        Student student4 = new Student("100","Student 3");
        Student student5 = new Student("100","Student 3");
        lab.enrolledStudents(student1);
        lab.enrolledStudents(student2);
        lab.enrolledStudents(student3);
        lab.enrolledStudents(student4);
        lab.enrolledStudents(student5);
        Student student6 = new Student("100","Student 3");
        Exception exception = assertThrows(CapacityException.class, () -> {
            lab.enrolledStudents(student6);
        });

        String message = "The Section is CLOSED, Out of section capacity";
        String returnMessage = exception.getMessage();

        assertEquals(message, returnMessage);
    }
}


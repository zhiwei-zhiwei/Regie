package course_management;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseNum;
    private String courseName;
    private List<Lab> labs;
    private Schedule schedules;
    private Locations location;
    private List<Student> enrolledStudents;
    private int courseCapacity;
    private String status;
    private Faculty faculty;
    private String sectionNum;

    private int currCapacity;

    public Course() {
    }

    public Course(String courseId, String courseName, String sectionNum) {
        this.courseNum = courseId;
        this.courseName = courseName;
        this.sectionNum = sectionNum;
        this.status = "Open";
        this.courseCapacity = 5;
        this.schedules = new Schedule();
        this.location = new Locations();
        this.faculty = new Faculty();
        this.enrolledStudents = new ArrayList<>();
        this.sectionNum = "001";
    }

    public Course(String courseName, String courseNum, String sectionNum, int courseCapacity, int currCapacity) {
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.sectionNum = sectionNum;
        this.courseCapacity = courseCapacity;
        this.currCapacity = currCapacity;
    }

    public void enrolledStudents(Student student) throws CapacityException {
        if (enrolledStudents.size() >= courseCapacity) {
            this.status = "Closed";
            throw new CapacityException("The Course is CLOSED, Out of course capacity");
        }
        enrolledStudents.add(student);
    }

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void addLab(Lab lab) {
        labs.add(lab);
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(int courseCapacity) {
        this.courseCapacity = courseCapacity;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Lab> getLabs() {
        return labs;
    }

    public void setLabs(List<Lab> labs) {
        this.labs = labs;
    }

//    public List<Section> getSections() {
//        return sections;
//    }
//
//    public void setSections(List<Section> sections) {
//        this.sections = sections;
//    }

    public Schedule getSchedules() {
        return schedules;
    }

    public void setSchedules(Schedule schedules) {
        this.schedules = schedules;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }
}

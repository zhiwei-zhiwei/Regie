package course_management;

import java.util.ArrayList;
import java.util.List;

public class Lab {
    private String labNum;
    private Schedule schedule;
    private Locations locations;
    private int sectionCapacity;
    private String status;
    private Faculty faculty;
    private List<Student> enrolledStudents;


    public Lab(String labNum) {
        this.labNum = labNum;
        this.sectionCapacity = 5;
        this.status = "Open";
        this.schedule = new Schedule();
        this.locations = new Locations();
        this.enrolledStudents = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionId='" + labNum + '\'' +
                ", schedule=" + schedule +
                ", locations=" + locations +
                '}';
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
    public void enrolledStudents(Student student) throws CapacityException {
        if (enrolledStudents.size() >= sectionCapacity){
            this.status = "Closed";
            throw new CapacityException("The Section is CLOSED, Out of section capacity");
        }
        enrolledStudents.add(student);
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

    public int getSectionCapacity() {
        return sectionCapacity;
    }

    public void setSectionCapacity(int sectionCapacity) {
        this.sectionCapacity = sectionCapacity;
    }

    public String getLabNum() {
        return labNum;
    }

    public void setLabNum(String labNum) {
        this.labNum = labNum;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }
}

package course_management;

public class Faculty {
    private String name;

    public Faculty() {
        this.name = "Not assigned yet!";
    }

    public Faculty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

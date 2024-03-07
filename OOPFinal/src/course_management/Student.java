package course_management;
import dao.UserDAO;

public class Student {
    private String UChiID;
    private String name;

    public Student(String UChiID, String name) {
        this.UChiID = UChiID;
        this.name = name;
    }

    public String getUChiID() {
        return UChiID;
    }

    public void setUChiID(String UChiID) {
        this.UChiID = UChiID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    UserDAO dao = new UserDAO();
    public boolean isUserNameValid(String userName) {
        return dao.validUsername(userName) != -1;
    }



}

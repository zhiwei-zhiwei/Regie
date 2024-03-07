package course_management;

public class Locations {
    private String building;
    private String room;

    public Locations() {
        this.building = "Not assigned yet";
        this.room = "Not assigned yet";
    }

    public Locations(String building, String room) {
        this.building = building;
        this.room = room;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "building='" + building + '\'' +
                ", room='" + room + '\'' +
                '}';
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}

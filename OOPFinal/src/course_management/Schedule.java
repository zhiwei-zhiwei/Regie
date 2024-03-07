package course_management;

public class Schedule {
    private String days;
    private String startTime;
    private String endTime;

    public Schedule() {
        this.days = "Not assigned yet";
        this.startTime = "Not assigned yet";
        this.endTime = "Not assigned yet";
    }

    public Schedule(String days, String startTime, String endTime) {
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "days='" + days + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

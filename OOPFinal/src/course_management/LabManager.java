package course_management;

import dao.LabDAO;
import interfaces.ISectionAddManagement;
import interfaces.ISectionStatus;

import java.sql.SQLException;

public class LabManager implements ISectionAddManagement, ISectionStatus {

    private static LabManager instance;
    private LabDAO labDAO;

    private LabManager() {
        labDAO = new LabDAO();
    }

    public static LabManager getInstance() {
        if (instance == null) {
            instance = new LabManager();
        }
        return instance;
    }
    @Override
    public void addSection(Lab lab, int courseId) {
        labDAO.addSectionToCourse(lab.getLabNum(), courseId,lab.getSectionCapacity());
    }


    @Override
    public void openedSection() {
    }


    public void incrementLabCapacity(String labNum){
        labDAO.incrementLabCapacity(labNum);
    }

    public void closeLab(String labNum){
        labDAO.closeByName(labNum);
    }

    public boolean isLabFill(String labNum) {
        return labDAO.isLabFull(labNum) <= 0;
    }

    public boolean isLabNameValid(String userName) {
        return labDAO.validLabNum(userName) != -1;
    }

    public boolean labNumExists(String labNum, int courseId){
        return labDAO.labNumExists(labNum, courseId);
    }


    public int getIdByNum(Lab lab) throws SQLException {
        return labDAO.getSectionIdByCourseNum(lab.getLabNum());
    }

    public void assignLocation(int labId, Locations locations) throws SQLException {
        labDAO.assignSectionLocation(labId, locations);
    }

    public void assignSchedule(int labId, Schedule schedule){
        labDAO.assignSectionSchedule(labId, schedule);
    }
    public int getIdByName(String sectionName) throws SQLException {
        return 0;
    }

    public int getIdBySection(Lab lab) throws SQLException {
        return 0;
    }

    public void deleteSectionByNum(String sectionNum) {

    }

    public void deleteSectionByName(String sectionName) {

    }

    public void deleteSectionByCourse(Lab lab) {

    }

    public boolean isSectionFill(Lab lab) {
        return false;
    }
}

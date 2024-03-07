package interfaces;

import course_management.Lab;

public interface ISectionManagement {
    void addSection(Lab lab);
    void addSectionWithCapacity(Lab lab);
    void openedSection();
    void getAllSection();
    void getSectionWithSections();
}

use CourseManagement;

create table courses
(
    id         integer primary key auto_increment,
    courseNum  varchar(40),
    courseName varchar(40)
);

create table sections
(
    id         integer primary key auto_increment,
    sectionNum varchar(40),
    course_id  integer,
    foreign key (course_id) references courses (id)
);

create table courseSchedule
(
    id        integer primary key auto_increment,
    day       varchar(40),
    startTime varchar(40),
    endTime   varchar(40),
    course_id integer,
    foreign key (course_id) references courses (id)
);

create table courseLocation
(
    id        integer primary key auto_increment,
    building  varchar(40),
    room      varchar(40),
    course_id integer,
    foreign key (course_id) references courses (id)
);

create table sectionSchedule
(
    id         integer primary key auto_increment,
    day        varchar(40),
    startTime  varchar(40),
    endTime    varchar(40),
    section_id integer,
    foreign key (section_id) references sections (id)
);

create table sectionLocation
(
    id         integer primary key auto_increment,
    building   varchar(40),
    room       varchar(40),
    section_id integer,
    foreign key (section_id) references sections (id)
);

ALTER TABLE courses
    ADD COLUMN status VARCHAR(20) DEFAULT 'open';

ALTER TABLE courses
    ADD COLUMN courseCapacity INT DEFAULT 5;

ALTER TABLE courses
    ADD COLUMN currentCourseCapacity INT DEFAULT 0;

ALTER TABLE sections
    ADD COLUMN status VARCHAR(20) DEFAULT 'open';

ALTER TABLE sections
    ADD COLUMN courseCapacity INT DEFAULT 5;

ALTER TABLE sections
    ADD COLUMN currentCourseCapacity INT DEFAULT 0;

CREATE TABLE courseStudents
(
    id        int primary key auto_increment,
    UChiID    VARCHAR(255),
    name      VARCHAR(255),
    course_id INTEGER,
    FOREIGN KEY (course_id) REFERENCES courses (id)
);

CREATE TABLE sectionStudents
(
    id         int primary key auto_increment,
    UChiID     VARCHAR(255),
    name       VARCHAR(255),
    section_id INTEGER,
    FOREIGN KEY (section_id) REFERENCES sections (id)
);

CREATE TABLE courseFaculty
(
    id        int primary key auto_increment,
    name      VARCHAR(255),
    course_id INTEGER,
    FOREIGN KEY (course_id) REFERENCES courses (id)
);

CREATE TABLE sectionFaculty
(
    id         int primary key auto_increment,
    name       VARCHAR(255),
    section_id INTEGER,
    FOREIGN KEY (section_id) REFERENCES sections (id)
);

ALTER TABLE sections
    RENAME COLUMN courseCapacity TO sectionCapacity;

ALTER TABLE sections
    RENAME COLUMN currentCourseCapacity TO currentSectionCapacity;

CREATE TABLE user
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    userId       VARCHAR(255)                NOT NULL,
    userName     VARCHAR(255)                NOT NULL,
    userPassword VARCHAR(255)                NOT NULL,
    type         ENUM ('STUDENT', 'FACULTY') NOT NULL
);

CREATE TABLE courseEnrollments
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    courseNum  VARCHAR(255) NOT NULL,
    courseName VARCHAR(255) NOT NULL,
    userId     INT,
    FOREIGN KEY (userId) REFERENCES user (id)
);

ALTER TABLE courses
    ADD COLUMN sectionNum VARCHAR(255);

ALTER TABLE courseEnrollments
    ADD COLUMN sectionNum VARCHAR(255);

ALTER TABLE user
    MODIFY COLUMN type ENUM ('STUDENT', 'FACULTY', 'ADMIN') NOT NULL;

CREATE TABLE labEnrollments
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    labNum    VARCHAR(255) NOT NULL,
    course_id INT,
    userId    INT,
    FOREIGN KEY (course_id) REFERENCES courses (id),
    FOREIGN KEY (userId) REFERENCES user (id)
);


SELECT c.courseName, c.sectionNum, c.id, cs.day, cs.startTime, cs.endTime
FROM courseEnrollments ce
         JOIN courses c ON ce.courseNum = c.courseNum and ce.sectionNum = c.sectionNum
         JOIN courseSchedule cs ON c.id = cs.course_id
WHERE ce.userId = 1;


create table temp_course
(
    id                    int auto_increment
        primary key,
    courseNum             varchar(40)                 null,
    courseName            varchar(40)                 null,
    status                varchar(20)  default 'open' null,
    courseCapacity        int          default 5      null,
    currentCourseCapacity int          default 0      null,
    sectionNum            varchar(255) default '001'  null
);

ALTER TABLE courses
    MODIFY COLUMN sectionNum VARCHAR(255) DEFAULT '001';


CREATE TABLE temp_user
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    userId       VARCHAR(255)                NOT NULL,
    userName     VARCHAR(255)                NOT NULL,
    userPassword VARCHAR(255)                NOT NULL,
    type         ENUM ('STUDENT', 'FACULTY', 'ADMIN') NOT NULL
);


INSERT INTO CourseManagement.courses (id, courseNum, courseName, status, courseCapacity, currentCourseCapacity, sectionNum) VALUES (1, 'CSCI101', 'Introduction to Computer Science', 'open', 30, 28, '001');
INSERT INTO CourseManagement.courses (id, courseNum, courseName, status, courseCapacity, currentCourseCapacity, sectionNum) VALUES (2, 'MATH201', 'Advanced Mathematics', 'open', 25, 23, '001');
INSERT INTO CourseManagement.courses (id, courseNum, courseName, status, courseCapacity, currentCourseCapacity, sectionNum) VALUES (3, 'PHYS301', 'Principles of Physics', 'open', 30, 15, '001');
INSERT INTO CourseManagement.courses (id, courseNum, courseName, status, courseCapacity, currentCourseCapacity, sectionNum) VALUES (11, 'CS1010', 'OOP', 'Closed', 15, 15, '001');
INSERT INTO CourseManagement.courses (id, courseNum, courseName, status, courseCapacity, currentCourseCapacity, sectionNum) VALUES (12, 'CSCI101', 'Introduction to Computer Science', 'open', 30, 29, '002');


INSERT INTO CourseManagement.courseSchedule (id, day, startTime, endTime, course_id) VALUES (3, 'Monday', '09:00', '11:00', 1);
INSERT INTO CourseManagement.courseSchedule (id, day, startTime, endTime, course_id) VALUES (4, 'Wednesday', '09:00', '11:00', 1);
INSERT INTO CourseManagement.courseSchedule (id, day, startTime, endTime, course_id) VALUES (5, 'Tuesday', '10:00', '12:00', 2);
INSERT INTO CourseManagement.courseSchedule (id, day, startTime, endTime, course_id) VALUES (6, 'Thursday', '10:00', '12:00', 2);
INSERT INTO CourseManagement.courseSchedule (id, day, startTime, endTime, course_id) VALUES (7, 'Monday', '09:00', '11:00', 3);


INSERT INTO CourseManagement.courseLocation (id, building, room, course_id) VALUES (4, 'Science Building', '101', 1);
INSERT INTO CourseManagement.courseLocation (id, building, room, course_id) VALUES (5, 'Math Building', '202', 2);
INSERT INTO CourseManagement.courseLocation (id, building, room, course_id) VALUES (6, 'Physics Building', '303', 3);


INSERT INTO CourseManagement.lab (id, labNum, course_id, status, labCapacity, currentLabCapacity) VALUES (1, 'S101', 1, 'open', 15, 14);
INSERT INTO CourseManagement.lab (id, labNum, course_id, status, labCapacity, currentLabCapacity) VALUES (2, 'S102', 1, 'Closed', 15, 15);
INSERT INTO CourseManagement.lab (id, labNum, course_id, status, labCapacity, currentLabCapacity) VALUES (3, 'S201', 2, 'open', 12, 10);
INSERT INTO CourseManagement.lab (id, labNum, course_id, status, labCapacity, currentLabCapacity) VALUES (4, 'S202', 2, 'open', 13, 10);
INSERT INTO CourseManagement.lab (id, labNum, course_id, status, labCapacity, currentLabCapacity) VALUES (5, 'S301', 3, 'open', 15, 7);

INSERT INTO CourseManagement.sectionLocation (id, building, room, section_id) VALUES (4, 'Science Annex', '102', 1);
INSERT INTO CourseManagement.sectionLocation (id, building, room, section_id) VALUES (5, 'Math Annex', '203', 2);
INSERT INTO CourseManagement.sectionLocation (id, building, room, section_id) VALUES (6, 'Physics Annex', '304', 3);
INSERT INTO CourseManagement.sectionLocation (id, building, room, section_id) VALUES (7, 'Chemistry Annex', '405', 4);
INSERT INTO CourseManagement.sectionLocation (id, building, room, section_id) VALUES (8, 'Biology Annex', '506', 5);


INSERT INTO CourseManagement.sectionSchedule (id, day, startTime, endTime, section_id) VALUES (4, 'Friday', '09:00', '10:30', 1);
INSERT INTO CourseManagement.sectionSchedule (id, day, startTime, endTime, section_id) VALUES (5, 'Friday', '11:00', '12:30', 2);
INSERT INTO CourseManagement.sectionSchedule (id, day, startTime, endTime, section_id) VALUES (6, 'Wednesday', '14:00', '15:30', 3);
INSERT INTO CourseManagement.sectionSchedule (id, day, startTime, endTime, section_id) VALUES (7, 'Tuesday', '16:00', '17:30', 4);
INSERT INTO CourseManagement.sectionSchedule (id, day, startTime, endTime, section_id) VALUES (8, 'Thursday', '18:00', '19:30', 5);


INSERT INTO CourseManagement.user (id, userId, userName, userPassword, type) VALUES (1, '00001', 'student', 'student', 'STUDENT');
INSERT INTO CourseManagement.user (id, userId, userName, userPassword, type) VALUES (2, '00002', 'admin', '123', 'ADMIN');


INSERT INTO CourseManagement.courseEnrollments (id, courseNum, courseName, userId, sectionNum) VALUES (12, 'CS1010', 'OOP', 1, '001');
INSERT INTO CourseManagement.courseEnrollments (id, courseNum, courseName, userId, sectionNum) VALUES (15, 'CSCI101', 'Introduction to Computer Science', 1, '001');


INSERT INTO CourseManagement.labEnrollments (id, labNum, course_id, userId) VALUES (2, 'S101', 1, 1);
INSERT INTO CourseManagement.labEnrollments (id, labNum, course_id, userId) VALUES (3, 'S101', 1, 1);
INSERT INTO CourseManagement.labEnrollments (id, labNum, course_id, userId) VALUES (4, 'S101', 1, 1);


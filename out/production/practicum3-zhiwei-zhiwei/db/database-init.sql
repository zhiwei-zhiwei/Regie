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
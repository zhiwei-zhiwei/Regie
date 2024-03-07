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
    id        integer primary key auto_increment,
    building  varchar(40),
    room      varchar(40),
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
    id  int primary key auto_increment,
    UChiID    VARCHAR(255),
    name      VARCHAR(255),
    course_id INTEGER,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE sectionStudents
(
    id  int primary key auto_increment,
    UChiID    VARCHAR(255),
    name      VARCHAR(255),
    section_id INTEGER,
    FOREIGN KEY (section_id) REFERENCES sections(id)
);

CREATE TABLE courseFaculty
(
    id  int primary key auto_increment,
    name      VARCHAR(255),
    course_id INTEGER,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE sectionFaculty
(
    id  int primary key auto_increment,
    name      VARCHAR(255),
    section_id INTEGER,
    FOREIGN KEY (section_id) REFERENCES sections(id)
);

SELECT courseCapacity FROM courses WHERE courseNum = 22222;
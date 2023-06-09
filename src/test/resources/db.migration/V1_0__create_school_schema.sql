CREATE SCHEMA IF NOT EXISTS school;
SET search_path TO school, public;

CREATE TABLE IF NOT EXISTS school.course (
    ID BIGSERIAL PRIMARY KEY,
    NAME TEXT,
    DESCRIPTION TEXT
);
CREATE TABLE IF NOT EXISTS school.group (
    ID BIGSERIAL PRIMARY KEY,
    NAME TEXT
);
CREATE TABLE IF NOT EXISTS school.student (
    ID BIGSERIAL PRIMARY KEY,
    GROUP_ID INT,
    FIRST_NAME TEXT,
    LAST_NAME TEXT,
    CONSTRAINT GROUP_ID_FK
    FOREIGN KEY (GROUP_ID)
    REFERENCES school.group (ID)
);
CREATE TABLE IF NOT EXISTS school.student_course (
    STUDENT_ID bigserial,
    COURSE_ID bigserial,
    CONSTRAINT STUDENT_ID_FK FOREIGN KEY (STUDENT_ID) REFERENCES student (ID) ON DELETE CASCADE,
    CONSTRAINT COURSE_ID_FK FOREIGN KEY (COURSE_ID) REFERENCES course (ID) ON DELETE CASCADE,
    UNIQUE (STUDENT_ID, COURSE_ID)
);
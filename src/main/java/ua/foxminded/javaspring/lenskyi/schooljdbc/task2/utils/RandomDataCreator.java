package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.StudentCourse;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class RandomDataCreator {

    private static final String SON = "son";
    private static final String SEMICOLON = ";";
    private static final String HYPHEN = "-";
    private static final String WHITESPACE_HYPHEN_WHITESPACE = " - ";
    private FileReader reader;
    private Random secureRandom;
    @Autowired
    private SchoolJDBCCache schoolJDBCCache;
    @Value("${filename.names}")
    private String NAMES;
    @Value("${filename.courses}")
    private String COURSES;

    @Autowired
    public RandomDataCreator(FileReader reader, Random secureRandom) {
        this.reader = reader;
        this.secureRandom = secureRandom;
    }

    public void setNAMES(String NAMES) {
        this.NAMES = NAMES;
    }

    public void setCOURSES(String COURSES) {
        this.COURSES = COURSES;
    }

    public List<Group> generateGroups(int numGroups) {
        List<Group> groups = new ArrayList<>();
        for (int i = 1; i <= numGroups; i++) {
            Group group = new Group();
            group.setId(i);
            group.setName(generateGroupName());
            groups.add(group);
        }
        return groups;
    }

    private String generateGroupName() {
        StringBuilder groupName = new StringBuilder();
        int minNumberInGroupName = 10;
        int maxNumberInGroupName = 99;
        return groupName.append(getRandomCharactersUpperCase(2))
                .append(HYPHEN)
                .append(secureRandom.nextInt(minNumberInGroupName, maxNumberInGroupName + 1))
                .toString();
    }

    private String getRandomCharactersUpperCase(int numOfChars) {
        byte[] array = new byte[256];
        secureRandom.nextBytes(array);
        String randomString = new String(array, StandardCharsets.UTF_8);
        StringBuilder r = new StringBuilder();
        for (int k = 0; k < randomString.length(); k++) {
            char ch = randomString.charAt(k);
            if ((ch >= 'A' && ch <= 'Z') && (numOfChars > 0)) {
                r.append(ch);
                numOfChars--;
            }
        }
        return r.toString();
    }

    public List<Student> generateStudents(int numStudents) {
        List<Student> students = new ArrayList<>();
        String[] names = reader.readFile(NAMES).split(SEMICOLON);
        for (int i = 1; i <= numStudents; i++) {
            Student student = new Student();
            student.setId(i);
            student.setGroupId(null);
            student.setFirstName(generateStudentFirstName(names));
            student.setLastName(generateStudentLastName(names));
            students.add(student);
        }
        assignStudentsToGroups(students, 10, schoolJDBCCache.getMinGroupId());
        return students;
    }

    private String generateStudentFirstName(String[] names) {
        int maxNumInNamesArray = 40;
        return names[secureRandom.nextInt(maxNumInNamesArray)];
    }

    private String generateStudentLastName(String[] names) {
        int maxNumInNamesArray = 40;
        StringBuilder studentLastName = new StringBuilder();
        return studentLastName.append(names[secureRandom.nextInt(maxNumInNamesArray)]).append(SON).toString();
    }

    private void assignStudentsToGroups(List<Student> students, int numOfGroups, int minGroupId) {
        int numOfAssignedStudents = 0;
        int numStudentsToAssign = students.size();
        int minNumStudentsForGroup = 10;
        int maxNumStudentsForGroup = 30;
        for (int i = minGroupId; i < minGroupId + numOfGroups; i++) {
            int randomNumOfStudentsForOneGroup = secureRandom.nextInt(minNumStudentsForGroup, maxNumStudentsForGroup + 1);
            numOfAssignedStudents += randomNumOfStudentsForOneGroup;
            if (numOfAssignedStudents > students.size()) {
                break;
            } else {
                while (randomNumOfStudentsForOneGroup > 0) {
                    students.get((numStudentsToAssign) - 1).setGroupId(Long.valueOf(i));
                    randomNumOfStudentsForOneGroup--;
                    numStudentsToAssign--;
                }
            }
        }
    }

    public Set<StudentCourse> generateStudentCourseRelation(int numStudents) {
        Set<StudentCourse> studentsCourses = new HashSet<>();
        int minCourseId = schoolJDBCCache.getMinCourseId();
        int maxCourseId = schoolJDBCCache.getMaxCourseId();
        int minStudentId = schoolJDBCCache.getMinStudentId();
        for (int i = minStudentId; i < minStudentId + numStudents; i++) {
            int numCourses = secureRandom.nextInt(1, 4);
            Set<Integer> coursesForStudent = new HashSet<>();
            while (numCourses > 0) {
                coursesForStudent.add(secureRandom.nextInt(minCourseId, maxCourseId + 1));
                numCourses--;
            }
            int finalI = i;
            coursesForStudent.forEach(courseId -> {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudentId(finalI);
                studentCourse.setCourseId(courseId);
                studentsCourses.add(studentCourse);
            });
        }
        return studentsCourses;
    }

    public List<Course> getCoursesFromResources() {
        List<Course> courses = new ArrayList<>();
        String[] coursesWithDescription = reader.readFile(COURSES).split(SEMICOLON);
        int id = 0;
        for (String courseWithDescription : coursesWithDescription) {
            id++;
            String[] str = courseWithDescription.split(WHITESPACE_HYPHEN_WHITESPACE);
            Course course = new Course();
            course.setId(id);
            course.setName(str[0]);
            course.setDescription(str[1]);
            courses.add(course);
        }
        return courses;
    }
}
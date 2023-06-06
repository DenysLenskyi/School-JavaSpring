package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.SchoolCache;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Course;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Group;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.Student;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.orm.StudentCourse;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class RandomDataCreator {

    private static final String SON = "son";
    private static final String SEMICOLON = ";";
    private static final String HYPHEN = "-";
    private static final String WHITESPACE_HYPHEN_WHITESPACE = " - ";
    private static final String SELECT_ALL_STUDENTS = "select s from Student s";
    private static final String SELECT_ALL_COURSES = "select c from Course c";
    private FileReader reader;
    private Random secureRandom;
    @Autowired
    private SchoolCache schoolCache;
    @Value("${filename.names}")
    private String NAMES;
    @Value("${filename.courses}")
    private String COURSES;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RandomDataCreator(FileReader reader, Random secureRandom) {
        this.reader = reader;
        this.secureRandom = secureRandom;
    }

    public List<Group> generateGroups(int numGroups) {
        List<Group> groups = new ArrayList<>();
        for (int i = 1; i <= numGroups; i++) {
            Group group = new Group();
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
            student.setGroupId(null);
            student.setFirstName(generateStudentFirstName(names));
            student.setLastName(generateStudentLastName(names));
            students.add(student);
        }
        assignStudentsToGroups(students, 10, Math.toIntExact(schoolCache.getMinGroupId()));
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

    public Set<StudentCourse> enrollStudentsToCourses() {
        Set<StudentCourse> studentsCourses = new HashSet<>();
        List<Student> students = entityManager.createQuery(SELECT_ALL_STUDENTS).getResultList();
        List<Course> courses = entityManager.createQuery(SELECT_ALL_COURSES).getResultList();
        students.forEach(student -> {
            int numCourses = secureRandom.nextInt(1, 4);
            while (numCourses > 0) {
                studentsCourses.add(new StudentCourse(student, courses.get(secureRandom.nextInt(courses.size()))));
                numCourses--;
            }
        });
        return studentsCourses;
    }

    public List<Course> getCoursesFromResources() {
        List<Course> courses = new ArrayList<>();
        String[] coursesWithDescription = reader.readFile(COURSES).split(SEMICOLON);
        for (String courseWithDescription : coursesWithDescription) {
            String[] str = courseWithDescription.split(WHITESPACE_HYPHEN_WHITESPACE);
            Course course = new Course();
            course.setName(str[0]);
            course.setDescription(str[1]);
            courses.add(course);
        }
        return courses;
    }
}
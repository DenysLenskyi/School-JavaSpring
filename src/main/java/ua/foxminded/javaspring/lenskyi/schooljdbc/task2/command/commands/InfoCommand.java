package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.SchoolCache;

@Component
public class InfoCommand implements Command {

    private static final String INFO_LINE_1 = """
            info
               prints available commands and how to use them
            exit
               exits the app
            find_course --course_id={value}
               prints course's info by course id which could be in range """;
    private static final String INFO_LINE_2 = """
            find_groups --num_students={value}
               prints groups with less or equal student's number (max 30 students in one group)
            find_students_course --course_name={value}
               example: --course_name=History - prints students enrolled to course
            add_student --group_id={value} --first_name={value} --last_name={value}
               adds new student; group id should 0 or in range """;
    private static final String INFO_LINE_3 = """
            delete_student --student_id={value}
               deletes student by student id which could be in range """;
    private static final String INFO_LINE_4 = """
            add_student_course --student_id={value} --course_name={value}
               adds student to course
            delete_student_course --student_id={value} --course_name={value}
               deletes student from course
            """;
    private static final String FORMAT = "%1$s %2$s - %3$s\n%4$s %5$s - %6$s\n%7$s %8$s - %9$s\n%10$s";

    @Autowired
    private SchoolCache schoolCache;

    @Override
    public void execute(CommandHolder commandHolder) {
        long minCourseId = schoolCache.getMinCourseId();
        long maxCourseId = schoolCache.getMaxCourseId();
        long minGroupId = schoolCache.getMinGroupId();
        long maxGroupId = schoolCache.getMaxGroupId();
        long minStudentId = schoolCache.getMinStudentId();
        long maxStudentId = schoolCache.getMaxStudentId();
        System.out.println(String.format(FORMAT,
                INFO_LINE_1, minCourseId, maxCourseId,
                INFO_LINE_2, minGroupId, maxGroupId,
                INFO_LINE_3, minStudentId, maxStudentId, INFO_LINE_4));
    }
}
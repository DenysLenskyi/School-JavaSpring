package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;

@Component
public class InfoCommand implements Command {

    private static final String INFO = """
            info
               prints available commands and how to use them
            exit
               exits the app
            find_course --course_id={value}
               prints course's info by course id number (1-10)
            find_groups --num_students={value}
               prints groups with less or equal student's number (max 30)
            find_students_course --course_name={value}
               example: --course_name=History - prints students enrolled to course
            add_student --group_id={value} --first_name={value} --last_name={value}
               adds new student; group id should be 0-10
            delete_student --student_id={value}
               deletes student by student id
               """;
    /*



            add_student_course --student_id={value} --course_name={value}
               adds student to course
            delete_student_course --student_id={value} --course_name={value}
               deletes student from course
            """;

     */

    @Override
    public void execute(CommandHolder commandHolder) {
        System.out.println(INFO);
    }
}
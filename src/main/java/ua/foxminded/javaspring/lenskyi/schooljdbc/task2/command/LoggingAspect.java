package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.DeleteStudentCommand.execute(..))")
    public void logDeleteStudentCommandCalled() {
        log.info("Delete student command called");
    }
}
package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution" +
            "(* ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.FindCourseByIdCommand.execute(..))")
    public void logFindCourseByIdCommandCalled() {
        log.info("FindCourseByIdCommand called");
    }

    @AfterThrowing(pointcut = "execution" +
            "(* ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.FindCourseByIdCommand.execute(..))",
            throwing = "e")
    public void logFindCourseByIdCommandEmptyResult(EmptyResultDataAccessException e) throws Throwable {
        log.error(e.getMessage());
    }
}
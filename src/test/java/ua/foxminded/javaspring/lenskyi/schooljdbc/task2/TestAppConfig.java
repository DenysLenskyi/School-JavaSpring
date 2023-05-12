package ua.foxminded.javaspring.lenskyi.schooljdbc.task2;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcGroupDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentCourseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

@Profile("test")
@Configuration
public class TestAppConfig {

    @Bean
    @Primary
    public JdbcCourseDao jdbcCourseDao() {
        return Mockito.mock(JdbcCourseDao.class);
    }

    @Bean
    @Primary
    public JdbcGroupDao jdbcGroupDao() {
        return Mockito.mock(JdbcGroupDao.class);
    }

    @Bean
    @Primary
    public JdbcStudentDao jdbcStudentDao() {
        return Mockito.mock(JdbcStudentDao.class);
    }

    @Bean
    @Primary
    public JdbcStudentCourseDao jdbcStudentCourseDao() {
        return Mockito.mock(JdbcStudentCourseDao.class);
    }
}
package ua.foxminded.javaspring.lenskyi.schooljdbc.task2;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands.AddStudentCommand;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcStudentDao;

@Profile("test")
@Configuration
public class TestAppConfig {

    @Bean
    @Primary
    public JdbcStudentDao jdbcStudentDao() {
        return Mockito.mock(JdbcStudentDao.class);
    }

    @Bean
    @Primary
    public AddStudentCommand addStudentCommand() {
        return Mockito.mock(AddStudentCommand.class);
    }

}

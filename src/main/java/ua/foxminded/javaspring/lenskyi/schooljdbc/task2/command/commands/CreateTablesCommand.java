package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.Command;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.command.CommandHolder;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.JdbcBaseDao;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils.FileReader;

@Component
public class CreateTablesCommand implements Command {

    private FileReader reader;
    private JdbcBaseDao jdbcBaseDao;
    private static final String TABLES_INITIATION_SCRIPT_FILE_NAME = "/initiate-tables.sql";

    @Autowired
    public CreateTablesCommand(FileReader reader, JdbcBaseDao jdbcBaseDao) {
        this.reader = reader;
        this.jdbcBaseDao = jdbcBaseDao;
    }

    @Override
    public void execute(CommandHolder commandHolder) {
        jdbcBaseDao.executeQuery(reader.readFile(TABLES_INITIATION_SCRIPT_FILE_NAME));
    }
}
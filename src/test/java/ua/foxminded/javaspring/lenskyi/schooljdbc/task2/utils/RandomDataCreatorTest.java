package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.AppConfig;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.Main;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class RandomDataCreatorTest {

    private int numGroups = 10;


    @Autowired
    RandomDataCreator randomDataCreator;


    @Test
    void generateGroupsTest() {
        List<Group> groups = randomDataCreator.generateGroups(numGroups);
        assertTrue(groups.size() == 10);
    }
}
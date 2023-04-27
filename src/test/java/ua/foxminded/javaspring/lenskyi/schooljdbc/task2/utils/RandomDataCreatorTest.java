package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.junit.jupiter.api.Test;
import ua.foxminded.javaspring.lenskyi.schooljdbc.task2.dao.domain.Group;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDataCreatorTest {

    private Random random = new Random();
    private FileReader reader = new FileReader();
    private RandomDataCreator randomDataCreator = new RandomDataCreator(reader, random);

    private int numGroups = 10;


    @Test
    void generateGroupsTest() {
        List<Group> groups = randomDataCreator.generateGroups(numGroups);
        assertTrue(groups.size() == 10);
    }
}
package ua.foxminded.javaspring.lenskyi.schooljdbc.task2.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public String readFile(String fileName) {
        List<String> list = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            list = reader.lines().toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.forEach(result::append);
        return result.toString();
    }
}
package com.example.demo.repository;

import com.example.demo.domain.Project;
import com.example.demo.domain.ProjectFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testInsert() {
        Project project = Project.builder()
                .title("test1")
                .content("test content1")
                .dateTime(LocalDateTime.now())
                .build();

        projectRepository.save(project);

    }
}

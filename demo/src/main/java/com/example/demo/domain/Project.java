package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Table(name = "tbl_project")
@Getter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @NonNull
    private String title;

    private String content;

    private LocalDateTime dateTime;

    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ProjectFile> fileList = new ArrayList<>();

    public void addFile(ProjectFile file) {

        file.setOrd(fileList.size());
        fileList.add(file);
    }

    public void addFileString(String fileName) {

        ProjectFile projectFile = ProjectFile.builder()
                .fileName(fileName)
                .build();

        addFile(projectFile);
    }

    public void clearList() {
        this.fileList.clear();
    }
}

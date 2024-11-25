package com.example.demo.controller;


import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.service.ProjectService;
import com.example.demo.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final CustomFileUtil fileUtil;
    private final ProjectService projectService;

    @PostMapping("/")
    private Map<String, String> register(ProjectDTO projectDTO) {
        log.info("--------------------------");
        log.info("register" + projectDTO);

        List<MultipartFile> files = projectDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        projectDTO.setUploadFileNames(uploadFileNames);

        projectService.register(projectDTO);

        return Map.of("RESULT", "SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProjectDTO> list(PageRequestDTO pageRequestDTO){
        return projectService.getList(pageRequestDTO);
    }
}

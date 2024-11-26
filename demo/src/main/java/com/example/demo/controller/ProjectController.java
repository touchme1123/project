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
import java.util.stream.Collectors;

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
    public PageResponseDTO<ProjectDTO> list(PageRequestDTO pageRequestDTO) {
        return projectService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProjectDTO read(@PathVariable("pno") Long pno) {

        return projectService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable Long pno, ProjectDTO projectDTO) {
        projectDTO.setPno(pno);

        //기존
        ProjectDTO oldProjectDTO = projectService.get(pno);

        //파일 업로드
        List<MultipartFile> files = projectDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //기존 파일 유지
        List<String> uploadedFileNames = projectDTO.getUploadFileNames();

        //추가 파일이 있으면 추가해주기
        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        //기존 파일중에서 삭제 된 파일 처리
        List<String> oldFileNames = oldProjectDTO.getUploadFileNames();
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFiles =
                    oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("{pno}")
    public Map<String, String> remove(@PathVariable Long pno){

        List<String> oldFileNames = projectService.get(pno).getUploadFileNames();
        fileUtil.deleteFiles(oldFileNames);
        return  Map.of("RESULT", "SUCCESS");
    }
}

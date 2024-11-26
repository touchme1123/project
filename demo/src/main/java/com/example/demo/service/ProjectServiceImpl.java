package com.example.demo.service;


import com.example.demo.domain.Project;
import com.example.demo.domain.ProjectFile;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    private Project dtoToEntity(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .pno(projectDTO.getPno())
                .title(projectDTO.getTitle())
                .dateTime(projectDTO.getDateTime())
                .content(projectDTO.getContent())
                .build();

        List<String> uploadFileNames = projectDTO.getUploadFileNames();

        if (uploadFileNames.isEmpty()) {
            return project;
        }

        uploadFileNames.forEach(fileName -> {
            project.addFileString(fileName);
        });

        return project;
    }

    private ProjectDTO entityToDTO(Project project) {

        ProjectDTO projectDTO = ProjectDTO.builder()
                .pno(project.getPno())
                .title(project.getTitle())
                .dateTime(project.getDateTime())
                .content(project.getContent())
                .build();

        List<ProjectFile> fileList = project.getFileList();
        if (fileList.isEmpty()) {
            return projectDTO;
        }

        List<String> fileNameList = fileList.stream().map(projectFile ->
                projectFile.getFileName()).toList();
        projectDTO.setUploadFileNames(fileNameList);
        return projectDTO;
    }

    @Override
    public PageResponseDTO<ProjectDTO> getList(PageRequestDTO pageRequestDTO) {
        //Page 뽑아내는 규칙 만들기
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        //repository에서 Object [] 형태로 page size만큼 가져오기 object[0] 은 Project, object[1]은 ProjectFile 가 여러개있음
        Page<Object[]> result = projectRepository.selectList(pageable);

        //result 에서 하나씩 꺼내서 map 적용. 객체를 꺼내서 0은 project, 1은 projectFile 변수에 각각 넣고 DTO 에 SET화해서 리스트로 변환
        List<ProjectDTO> dtoList = result.get().map(arr -> {

            ProjectDTO projectDTO = null;
            Project project = (Project) arr[0];
            ProjectFile projectFile = (ProjectFile) arr[1];

            projectDTO = ProjectDTO.builder()
                    .pno(project.getPno())
                    .content(project.getContent())
                    .dateTime(project.getDateTime())
                    .title(project.getTitle())
                    .delFlag(project.isDelFlag())
                    .build();
            String fileStr = projectFile.getFileName();
            projectDTO.setUploadFileNames(List.of(fileStr));

            return projectDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProjectDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProjectDTO projectDTO) {

        Project project = dtoToEntity(projectDTO);
        Long pno = projectRepository.save(project).getPno();
        return pno;
    }

    @Override
    public ProjectDTO get(Long pno) {
        Optional<Project> result = projectRepository.findById(pno);
        Project project = result.orElseThrow();
        return entityToDTO(project);
    }

    @Override
    public void modify(ProjectDTO projectDTO) {
        //조회 하고 변경내용 반영 후 저장
        Optional<Project> result = projectRepository.findById(projectDTO.getPno());

        Project project = result.orElseThrow();

        project.setTitle(projectDTO.getTitle());
        project.setContent(projectDTO.getContent());
        project.setDelFlag(projectDTO.isDelFlag());

        //파일 처리
        List<String> uploadFileNames = projectDTO.getUploadFileNames();
        project.clearList();
        if (!uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(uploadName -> {
                project.addFileString(uploadName);
            });
        }

        projectRepository.save(project);

    }

    @Override
    public void remove(Long pno) {
        projectRepository.deleteById(pno);
    }

}


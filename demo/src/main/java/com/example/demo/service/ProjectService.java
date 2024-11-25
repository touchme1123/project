package com.example.demo.service;

import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.ProjectDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProjectService {

    PageResponseDTO<ProjectDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProjectDTO projectDTO);

    ProjectDTO get(Long pno);

    void modify(ProjectDTO projectDTO);

    void remove(Long pno);


}

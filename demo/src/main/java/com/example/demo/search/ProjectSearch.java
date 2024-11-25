package com.example.demo.search;

import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.ProjectDTO;

public interface ProjectSearch {
    PageResponseDTO<ProjectDTO> searchList (PageRequestDTO pageRequestDTO);
}

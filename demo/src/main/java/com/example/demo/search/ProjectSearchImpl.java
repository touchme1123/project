package com.example.demo.search;

import com.example.demo.domain.Project;
import com.example.demo.domain.QProject;
import com.example.demo.domain.QProjectFile;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResponseDTO;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.repository.ProjectRepository;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Log4j2
public class ProjectSearchImpl extends QuerydslRepositorySupport implements ProjectSearch {

    public ProjectSearchImpl() {
        super(Project.class);
    }

    @Override
    public PageResponseDTO<ProjectDTO> searchList(PageRequestDTO pageRequestDTO) {

        log.info("-----------searchList----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        QProject project = QProject.project;
        QProjectFile projectFile = QProjectFile.projectFile;

        JPQLQuery<Project> query = from(project);

        query.leftJoin(project.fileList, projectFile);

        query.where(projectFile.ord.eq(0)) ;

        getQuerydsl().applyPagination(pageable,query);
        return null;
    }
}

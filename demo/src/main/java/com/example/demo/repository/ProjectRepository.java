package com.example.demo.repository;

import com.example.demo.domain.Project;
import com.example.demo.search.ProjectSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectSearch {

    @EntityGraph(attributePaths = "fileList")
    @Query("select p from Project p where p.pno = :pno")
    Optional<Project> selectOne(@Param("pno") Long pno);

    @Query("UPDATE Project p set p.delFlag = :delFlag where p.pno=:pno ")
    void updateToDelete(@Param("pno") Long pno, @Param("delFlag") boolean delFlag);

    @Query("select p, pi  from Project p left join p.fileList pi  where pi.ord = 0 and p.delFlag = false ")
    Page<Object[]> selectList(Pageable pageable);
}

package com.example.demo.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProjectFile is a Querydsl query type for ProjectFile
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QProjectFile extends BeanPath<ProjectFile> {

    private static final long serialVersionUID = -1239052571L;

    public static final QProjectFile projectFile = new QProjectFile("projectFile");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> ord = createNumber("ord", Integer.class);

    public QProjectFile(String variable) {
        super(ProjectFile.class, forVariable(variable));
    }

    public QProjectFile(Path<? extends ProjectFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProjectFile(PathMetadata metadata) {
        super(ProjectFile.class, metadata);
    }

}


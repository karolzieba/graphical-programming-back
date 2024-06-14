package com.example.demo.classdiagram;

import lombok.Data;

import java.util.List;

/***
 * Diagram klasy z polaczeniami
 */
@Data
public class ClassDiagram {
    private List<ClassElement> classes;
    private List<Relationship> relationships;
}

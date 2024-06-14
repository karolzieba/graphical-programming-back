package com.example.demo.classdiagram;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/***
 * Element klasy
 */
@Data
@AllArgsConstructor
public class ClassElement {
    private Long id;
    private String name;
    private List<Field> fields;
    private List<Method> methods;
}

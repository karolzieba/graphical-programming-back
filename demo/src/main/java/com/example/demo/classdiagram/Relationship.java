package com.example.demo.classdiagram;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * Relacja
 */
@Data
@AllArgsConstructor
public class Relationship {
    private Long id;
    private String from;
    private String to;
    private String type;
}

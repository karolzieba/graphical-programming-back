package com.example.demo.classdiagram;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * Pole
 */
@Data
@AllArgsConstructor
public class Field {
    private String name;
    private String type;
}

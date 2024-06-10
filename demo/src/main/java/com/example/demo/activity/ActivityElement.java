package com.example.demo.activity;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * Element.
 */
@Data
@AllArgsConstructor
public class ActivityElement {

    private Long id;
    private String type;
    private String label;
    private Boolean visible;
    private Boolean used;
}

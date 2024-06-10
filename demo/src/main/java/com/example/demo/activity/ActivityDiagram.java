package com.example.demo.activity;

import lombok.Data;

import java.util.List;

/***
 * DTO zawierajace dane przesylane z front-endu.
 */
@Data
public class ActivityDiagram {

    private List<ActivityElement> elements;
    private List<ActivityConnection> connections;
}

package com.example.demo.activity;

import lombok.Data;

import java.util.List;

@Data
public class ActivityDiagram {

    private List<ActivityElement> elements;
    private List<ActivityConnection> connections;
}

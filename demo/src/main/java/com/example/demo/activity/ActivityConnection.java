package com.example.demo.activity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityConnection {

    private Long id;
    private Long src;
    private Long trg;
}

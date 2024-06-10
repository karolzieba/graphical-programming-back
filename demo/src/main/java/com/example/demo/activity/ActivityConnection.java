package com.example.demo.activity;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * Polaczenie.
 */
@Data
@AllArgsConstructor
public class ActivityConnection {

    private Long id;
    private Long src;
    private Long trg;
}

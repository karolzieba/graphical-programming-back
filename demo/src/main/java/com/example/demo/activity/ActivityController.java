package com.example.demo.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * Kontroler dotyczacy diagramu aktywnosci.
 */
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    /***
     * Endpoint dotyczacy diagramu aktywnosci.
     * @param diagram DTO.
     * @return 200 - w przypadku sukcesu, 500 - w przypadku bledu.
     */
    @PostMapping
    public ResponseEntity<String> generateCodeForDiagram(@RequestBody ActivityDiagram diagram) {
        try {
            return new ResponseEntity<>(activityService.generateCodeForDiagram(diagram), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

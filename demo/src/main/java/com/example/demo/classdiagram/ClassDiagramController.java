package com.example.demo.classdiagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/***
 * Kontroler dotyczacy diagramu klas.
 */
@RestController
@RequestMapping("/api/class-diagram")
public class ClassDiagramController {

    private final ClassDiagramService classDiagramService;

    @Autowired
    public ClassDiagramController(ClassDiagramService classDiagramService) {
        this.classDiagramService = classDiagramService;
    }

    /***
     * Endpoint dotyczacy diagramu klas.
     * @param diagram DTO.
     * @return 200 - w przypadku sukcesu, 500 - w przypadku bledu.
     */
    @PostMapping
    public ResponseEntity<String> generateCodeForDiagram(@RequestBody ClassDiagram diagram) {
        try {
            return new ResponseEntity<>(classDiagramService.generateCodeForDiagram(diagram), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

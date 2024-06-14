package com.example.demo.classdiagram;

import com.squareup.javapoet.*;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.util.List;

/***
 * Serwis dotyczacy diagramu klas
 */
@Service
public class ClassDiagramService {

    /***
     * Glowna metoda generowania kodu na bazie DTO diagramu klas.
     * @param diagram DTO.
     * @return Kod.
     */
    public String generateCodeForDiagram(ClassDiagram diagram) {
        TypeSpec.Builder diagramClass = TypeSpec
                .classBuilder("Diagram")
                .addModifiers(Modifier.PUBLIC);

        for (ClassElement classElement : diagram.getClasses()) {
            TypeSpec.Builder classBuilder = TypeSpec
                    .classBuilder(classElement.getName())
                    .addModifiers(Modifier.PUBLIC);

            for (Field field : classElement.getFields()) {
                classBuilder.addField(FieldSpec
                        .builder(String.class, field.getName(), Modifier.PRIVATE)
                        .build());
            }

            for (Method method : classElement.getMethods()) {
                classBuilder.addMethod(MethodSpec
                        .methodBuilder(method.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .build());
            }

            diagramClass.addType(classBuilder.build());
        }

        JavaFile javaFile = JavaFile
                .builder("pl.diagram.classdiagram", diagramClass.build())
                .build();

        return javaFile.toString();
    }
}

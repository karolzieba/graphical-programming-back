package com.example.demo.activity;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/***
 * Serwis dotyczacy diagramu aktywnosci.
 */
@Service
public class ActivityService {

    /***
     * Glowna metoda generowania kodu na bazie DTO diagramu aktywnosci.
     * @param diagram DTO.
     * @return Kod.
     */
    public String generateCodeForDiagram(ActivityDiagram diagram) {
        ActivityElement element = findFirstElement(diagram);
        List<MethodSpec> methodSpecs = new ArrayList<>();
        methodSpecs.add(generateContextMethod());
        methodSpecs.add(generateMainMethod());
        List<ActivityElement> nextElements = new ArrayList<>();
        while (true) {
            if (nextElements.isEmpty()) nextElements = findNextElements(diagram, element);
            if (nextElements.isEmpty()) break;
            element = nextElements.get(0);
            if (element.getType().equals("action")) generateStatement(methodSpecs, diagram, element);
            nextElements.remove(0);
        }
        TypeSpec diagramClass = TypeSpec
                .classBuilder("Diagram")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .build();
        return JavaFile
                .builder("pl.diagram.activity", diagramClass)
                .build()
                .toString();
    }

    /***
     * Metoda do stworzenia metody kontekstowej.
     * @return Obiekt metody kontekstowej.
     */
    private MethodSpec generateContextMethod() {
        return MethodSpec
                .methodBuilder("context")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .build();
    }

    /***
     * Metoda do stworzenia glownej metody.
     * @return Obiekt glownej metody.
     */
    private MethodSpec generateMainMethod() {
        return MethodSpec
                .methodBuilder("main")
                .addParameter(String[].class, "args")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("context()")
                .build();
    }

    /***
     * Metoda do edycji metody kontekstowej.
     * @param methodSpecs Lista metod.
     * @param codeBlock Blok kodu do dodania.
     */
    private void editContextMethod(List<MethodSpec> methodSpecs, CodeBlock codeBlock) {
        MethodSpec contextMethod = methodSpecs.stream()
                .filter(method -> method.name.equals("context"))
                .findFirst().orElseThrow(NoSuchElementException::new);
        methodSpecs.remove(contextMethod);
        contextMethod = contextMethod.toBuilder()
                .addCode(codeBlock)
                .build();
        methodSpecs.add(contextMethod);
    }

    /***
     * Metoda do wygenerowania wlasciwej linii kodu.
     * @param methodSpecs Lista metod.
     * @param diagram DTO.
     * @param element Element diagramu.
     */
    private void generateStatement(List<MethodSpec> methodSpecs, ActivityDiagram diagram, ActivityElement element) {
        String methodName = generateMethodName(element.getLabel());
        List<ActivityElement> previousElements = findPreviousElements(diagram, element);
        boolean isAnyPreviousElementCondition = previousElements.stream()
                .anyMatch(el -> el.getType().equals("condition"));
        CodeBlock codeBlock = CodeBlock.builder().build();
        if (isAnyPreviousElementCondition) {
            String [] labelParts = element.getLabel().split(":");
            String condition = labelParts.length > 1 ? labelParts[1].trim() : "conditionIfExist";
            String statement;
            if (previousElements.size() == 1) {
                statement = methodName + "()";
                codeBlock = CodeBlock.builder()
                        .beginControlFlow("if (" + condition + ")")
                        .addStatement(statement)
                        .endControlFlow()
                        .build();
            } else if (element.getUsed() != null && !element.getUsed()) {
                statement = methodName + "()";
                codeBlock = CodeBlock.builder()
                        .addStatement(statement)
                        .build();
            } else if (element.getUsed() != null) {
                statement = element.getType().equals("action") ? methodName + "()" : "context()";
                codeBlock = CodeBlock.builder()
                        .beginControlFlow("if (" + condition + ")")
                        .addStatement(statement)
                        .endControlFlow()
                        .build();
            }
        } else {
            codeBlock = CodeBlock.builder()
                    .addStatement(methodName + "()")
                    .build();
        }
        MethodSpec newMethod;
        if (element.getUsed() == null || !element.getUsed()) {
            newMethod = MethodSpec
                    .methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build();
            methodSpecs.add(newMethod);
        }
        editContextMethod(methodSpecs, codeBlock);
        element.setUsed(true);
    }

    /***
     * Metoda do znajdowania pierwszego elementu diagramu.
     * @param diagram DTO.
     * @return Obiekt pierwszego elementu.
     */
    private ActivityElement findFirstElement(ActivityDiagram diagram) {
        return diagram.getElements().stream()
                .filter(element -> element.getType().equals("first"))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    /***
     * Metoda do znajdowania nastepnych elementow diagramu.
     * @param diagram DTO.
     * @param element Element diagramu.
     * @return Lista nastepnych elementow.
     */
    private List<ActivityElement> findNextElements(ActivityDiagram diagram, ActivityElement element) {
        List<Long> nextElementsIds = diagram.getConnections().stream()
                .filter(connection -> connection.getSrc().equals(element.getId()))
                .map(ActivityConnection::getTrg)
                .toList();
        return diagram.getElements().stream()
                .filter(el -> nextElementsIds.contains(el.getId()))
                .collect(Collectors.toList());
    }

    /***
     * Metoda do znajdowania poprzednich elementow diagramu.
     * @param diagram DTO.
     * @param element Element.
     * @return Lista poprzednich elementow.
     */
    private List<ActivityElement> findPreviousElements(ActivityDiagram diagram, ActivityElement element) {
        List<Long> previousElementsIds = diagram.getConnections().stream()
                .filter(connection -> connection.getTrg().equals(element.getId()))
                .map(ActivityConnection::getSrc)
                .toList();
        return diagram.getElements().stream()
                .filter(el -> previousElementsIds.contains(el.getId()))
                .collect(Collectors.toList());
    }

    /***
     * Metoda do generowania nazwy metody.
     * @param label Pole "label" elementu.
     * @return Nazwa metody.
     */
    private String generateMethodName(String label) {
        if (label.contains(":")) label = label.split(":")[0];
        String[] labelParts = label.split(" ");
        StringBuilder methodName = new StringBuilder(labelParts[0]);
        for (int i = 1; i < labelParts.length; i++) {
            String labelPart = labelParts[i].substring(0, 1).toUpperCase() + labelParts[i].substring(1).toLowerCase();
            methodName.append(labelPart);
        }
        return methodName.toString();
    }
}
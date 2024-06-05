package com.example.demo.activity;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    public void generateCodeForDiagram(ActivityDiagram diagram) {

    }

    private ActivityElement findFirstElement(ActivityDiagram diagram) {
        return diagram.getElements().stream()
                .filter(element -> element.getType().equals("first"))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private List<ActivityElement> findNextElements(ActivityDiagram diagram, ActivityElement element) {
        List<Long> nextElementsIds = diagram.getConnections().stream()
                .filter(connection -> connection.getSrc().equals(element.getId()))
                .map(ActivityConnection::getTrg)
                .toList();
        return diagram.getElements().stream()
                .filter(el -> nextElementsIds.contains(el.getId()))
                .collect(Collectors.toList());
    }

    private List<ActivityElement> findPreviousElements(ActivityDiagram diagram, ActivityElement element) {
        List<Long> previousElementsIds = diagram.getConnections().stream()
                .filter(connection -> connection.getTrg().equals(element.getId()))
                .map(ActivityConnection::getSrc)
                .toList();
        return diagram.getElements().stream()
                .filter(el -> previousElementsIds.contains(el.getId()))
                .collect(Collectors.toList());
    }

    private String generateMethodName(String label) {
        String[] labelParts = label.split(" ");
        StringBuilder methodName = new StringBuilder(labelParts[0]);
        for (int i = 1; i < labelParts.length; i++) {
            String labelPart = labelParts[i].substring(0, 1).toUpperCase() + labelParts[i].substring(1).toLowerCase();
            methodName.append(labelPart);
        }
        return methodName.toString();
    }
}
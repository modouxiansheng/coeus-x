package com.modou.coeus.domain;

import com.modou.coeus.node.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-28 10:55
 **/
public class GitChangeDate {

    private String className;

    private List<Line> changeLines;

    public void setClassName(String className) {
        this.className = className;
    }

    public void addWithInit(Line line){
        if (changeLines == null){
            changeLines = new ArrayList<>();
        }
        changeLines.add(line);
    }

    public String getClassName() {
        return className;
    }

    public List<Line> getChangeLines() {
        return changeLines;
    }
}

package com.modou.coeus.node;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-09-24 15:05
 **/
public class Line {

    public int startLine;

    public int endLine;

    public Line(int startLine, int endLine) {
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public boolean isContainLine(Line line){
        if (line == null){
            return Boolean.FALSE;
        }
        return line.getStartLine() >= this.startLine
                && line.getEndLine() <= this.endLine;
    }
}

package com.modou.coeus.ability.scan;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusMethodNode;

import java.util.Stack;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-06 10:20
 **/
public class ScanCallHandlerData {

    // 当前执行到的方法信息
    private CoeusMethodNode coeusMethodNodeCurrent;

    private CoeusMethodNode coeusMethodNodePre;

    // 类的路由信息
    private ClassRouter classRouter;

    // 执行路径信息
    private Stack<CoeusMethodNode> direction;

    private CoeusMethodNode begin;

    public ScanCallHandlerData(CoeusMethodNode coeusMethodNodeCurrent, ClassRouter classRouter, Stack<CoeusMethodNode> direction,CoeusMethodNode coeusMethodNodePre) {
        this.coeusMethodNodeCurrent = coeusMethodNodeCurrent;
        this.coeusMethodNodePre = coeusMethodNodePre;
        this.classRouter = classRouter;
        this.direction = new Stack<>();
        this.direction.addAll(direction);
    }

    public CoeusMethodNode getCoeusMethodNodeCurrent() {
        return coeusMethodNodeCurrent;
    }

    public ClassRouter getClassRouter() {
        return classRouter;
    }

    public Stack<CoeusMethodNode> getDirection() {
        return direction;
    }

    public CoeusMethodNode getCoeusMethodNodePre() {
        return coeusMethodNodePre;
    }

    public void setBegin(CoeusMethodNode begin) {
        this.begin = begin;
    }

    public CoeusMethodNode getBegin() {
        return begin;
    }
}

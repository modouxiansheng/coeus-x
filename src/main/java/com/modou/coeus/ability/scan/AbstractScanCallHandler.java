package com.modou.coeus.ability.scan;

import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;

import java.util.ArrayList;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-03-06 17:31
 **/
public abstract class AbstractScanCallHandler implements ScanCallHandlerInterface{

    private CoeusClassNode classNode;

    private CoeusMethodNode coeusMethodNode;

    @Override
    public void invoke(ScanCallHandlerData scanCallHandlerData) {
       doInvoke(scanCallHandlerData);
    }

    public abstract void doInvoke(ScanCallHandlerData scanCallHandlerData);

    public CoeusClassNode getClassNode() {
        return classNode;
    }

    public void setClassNode(CoeusClassNode classNode) {
        this.classNode = classNode;
    }

    public CoeusMethodNode getCoeusMethodNode() {
        return coeusMethodNode;
    }

    public void setCoeusMethodNode(CoeusMethodNode coeusMethodNode) {
        this.coeusMethodNode = coeusMethodNode;
    }
}

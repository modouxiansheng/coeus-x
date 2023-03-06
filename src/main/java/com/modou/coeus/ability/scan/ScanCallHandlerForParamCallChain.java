package com.modou.coeus.ability.scan;

import com.modou.coeus.common.Constant;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import com.modou.coeus.utils.ParamParseUtils;

import java.util.*;

/**
 * @program: coeus-x
 * @description: 参数调用链
 * @author: hu_pf
 * @create: 2023-03-05 17:59
 * doc: https://www.processon.com/diagraming/5fe9dddfe0b34d2934f07545
 **/
public class ScanCallHandlerForParamCallChain implements ScanCallHandlerInterface{

    private String className;

    private String paramName;

    private List<List<CoeusMethodNode>> traces = new ArrayList<>();

    private Map<CoeusMethodNode,CoeusMethodNode> parent = new HashMap<>();

    public ScanCallHandlerForParamCallChain(String paramInfo) {
        String[] split = paramInfo.split(Constant.SPLIT);
        this.className = split[0];
        this.paramName = split[1];
    }

    @Override
    public void invoke(ScanCallHandlerData scanCallHandlerData) {
        // 到这个方法节点,然后查看自己方法节点有没有对这个参数的赋值, 如果有的话就将此快照栈信息全部输出出来
        CoeusMethodNode coeusMethodNode = scanCallHandlerData.getCoeusMethodNodeCurrent();
        if (coeusMethodNode == null){
            return;
        }

        parent.put(scanCallHandlerData.getCoeusMethodNodeCurrent(),scanCallHandlerData.getCoeusMethodNodePre());
        coeusMethodNode.coeusParamNodes.removeIf(e->!paramName.equals(e.name));

        for (CoeusParamNode coeusParamNode : coeusMethodNode.coeusParamNodes) {
            if (ParamParseUtils.isParameterAssigned(coeusMethodNode.getMetaData(),coeusParamNode)){
                List<CoeusMethodNode> path = new ArrayList<>();
                CoeusMethodNode node = scanCallHandlerData.getCoeusMethodNodeCurrent();
                while (!node.equals(scanCallHandlerData.getBegin())){
                    path.add(node);
                    node = parent.get(node);
                }
                path.add(scanCallHandlerData.getBegin());
                Collections.reverse(path);
//                ArrayList<CoeusMethodNode> path = new ArrayList<>(scanCallHandlerData.getDirection());
//                path.add(scanCallHandlerData.getCoeusMethodNodeCurrent());
                traces.add(path);
            }
        }
    }

    public List<List<CoeusMethodNode>> getTraces() {
        return traces;
    }
}

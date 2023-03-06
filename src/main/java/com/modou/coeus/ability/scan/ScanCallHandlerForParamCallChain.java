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
public class ScanCallHandlerForParamCallChain extends AbstractScanCallHandler{

    private String className;

    private String paramName;

    private Map<CoeusMethodNode, ArrayList<CoeusMethodNode>> adjList = new HashMap<>();

    public ScanCallHandlerForParamCallChain(String paramInfo) {
        String[] split = paramInfo.split(Constant.SPLIT);
        this.className = split[0];
        this.paramName = split[1];
    }

    @Override
    public void doInvoke(ScanCallHandlerData scanCallHandlerData) {
        // 到这个方法节点,然后查看自己方法节点有没有对这个参数的赋值, 如果有的话就将此快照栈信息全部输出出来
        CoeusMethodNode coeusMethodNode = scanCallHandlerData.getCoeusMethodNodeCurrent();
        if (coeusMethodNode == null){
            return;
        }

        if (adjList.containsKey(scanCallHandlerData.getCoeusMethodNodePre())){
            adjList.get(scanCallHandlerData.getCoeusMethodNodePre()).add(scanCallHandlerData.getCoeusMethodNodeCurrent());
        }else {
            ArrayList<CoeusMethodNode> arrayList = new ArrayList<>();
            arrayList.add(scanCallHandlerData.getCoeusMethodNodeCurrent());
            adjList.put(scanCallHandlerData.getCoeusMethodNodePre(),arrayList);
        }
    }

    public List<List<CoeusMethodNode>> getTraces() {
        return traverse();
    }

    /**
     * 使用深度优先遍历和栈实现的图遍历方法
     *
     * @return 所有从起点到终点的路径，每条路径以ArrayList形式返回
     */
    public List<List<CoeusMethodNode>> traverse() {
        List<List<CoeusMethodNode>> allPaths = new ArrayList<>();
        Stack<ArrayList<CoeusMethodNode>> stack = new Stack<>();
        Map<CoeusMethodNode, Boolean> visited = new HashMap<>(); // 记录已访问的节点
        ArrayList<CoeusMethodNode> path = new ArrayList<>();
        path.add(this.getCoeusMethodNode());
        stack.push(path);
        while (!stack.empty()) {
            ArrayList<CoeusMethodNode> curPath = stack.pop();
            CoeusMethodNode curNode = curPath.get(curPath.size() - 1);
            if (visited.containsKey(curNode) && visited.get(curNode)) {
                continue; // 如果已经访问过，跳过当前节点
            }
            visited.put(curNode, true); // 标记当前节点已经访问
            curNode.coeusParamNodes.removeIf(e -> !paramName.equals(e.name));
            if (valid(curNode, allPaths, curPath)) {
                continue;
            }
            ArrayList<CoeusMethodNode> neighbors = adjList.get(curNode);
            if (neighbors != null) {
                for (CoeusMethodNode neighbor : neighbors) {
                    if (!curPath.contains(neighbor)) {
                        ArrayList<CoeusMethodNode> newPath = new ArrayList<>(curPath);
                        newPath.add(neighbor);
                        stack.push(newPath);
                    }
                }
            }
        }
        return allPaths;
    }


    private boolean valid(CoeusMethodNode curNode,List<List<CoeusMethodNode>> allPaths,ArrayList<CoeusMethodNode> curPath){
        for (CoeusParamNode coeusParamNode : curNode.coeusParamNodes) {
            if (ParamParseUtils.isParameterAssigned(curNode.getMetaData(),coeusParamNode) && coeusParamNode.isEqualClass(className)){
                allPaths.add(curPath);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}

package com.modou.coeus.ability.scan;

import com.modou.coeus.common.Constant;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import com.modou.coeus.utils.ParamParseUtils;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.*;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.modou.coeus.common.Constant.CLASS_SPLIT_POINT;
import static com.modou.coeus.common.Constant.CLASS_SPLIT_SLASH;

/**
 * @program: coeus-x
 * @description: 参数赋值回溯
 * @author: hu_pf
 * @create: 2023-03-05 17:59
 * doc:
 **/
public class ScanCallHandlerForParamBacktrack extends AbstractScanCallHandler{


    private Map<CoeusMethodNode, ArrayList<CoeusMethodNode>> adjList = new HashMap<>();


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

    /**
     * @Description: 根据传入的参数信息获取其调用链
     * @Param: [paramInfo]
     * @return: java.util.List<java.util.List<com.modou.coeus.node.CoeusMethodNode>>
     * @Author: hu_pf
     * @Date: 2023/3/6
     */
    public List<List<CoeusMethodNode>> getTraces(String paramInfo) {
        return traverse(paramInfo);
    }

    /**
     * 使用深度优先遍历和栈实现的图遍历方法
     *
     * @return 所有从起点到终点的路径，每条路径以ArrayList形式返回
     */
    private List<List<CoeusMethodNode>> traverse(String paramInfo) {
        String[] split = paramInfo.split(Constant.SPLIT);
        String className = split[0];
        String paramName = split[1];
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

            if (valid(curNode, allPaths, curPath,className,paramName)) {
                continue;
            }
            visited.put(curNode, true); // 标记当前节点已经访问

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
        return allPaths.stream().distinct().collect(Collectors.toList());
    }


    private boolean valid(CoeusMethodNode curNode,List<List<CoeusMethodNode>> allPaths,ArrayList<CoeusMethodNode> curPath,String className,String paramName){
        if (curNode == null){
            return Boolean.FALSE;
        }
        for (CoeusParamNode coeusParamNode : curNode.coeusParamNodes) {
            if (ParamParseUtils.isParameterAssigned(curNode.getMetaData(),coeusParamNode)
                    && coeusParamNode.isConformParam(className,paramName)){
                allPaths.add(curPath);
                isParameterAssigned(curPath.get(curPath.size()-1).getMetaData(),coeusParamNode);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public void isParameterAssigned(MethodNode mn, CoeusParamNode coeusParamNode) {
        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        AbstractInsnNode currentNode = null;

        while (it.hasNext()) {
            AbstractInsnNode instruction = it.next();
            if (instruction.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) instruction).var == 0) {
                currentNode = instruction.getNext();
            }
            if (instruction.getOpcode() == Opcodes.PUTFIELD && currentNode != null) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) instruction;
                if (fieldInsnNode.name.equals(coeusParamNode.name) && fieldInsnNode.desc.equals(coeusParamNode.desc)) {
                    AbstractInsnNode previous = instruction.getPrevious();
                    if (previous instanceof MethodInsnNode){
                        MethodInsnNode node = (MethodInsnNode) previous;
                        if (node.name.contains("get")){
                            String className = node.desc.replaceAll(CLASS_SPLIT_SLASH, CLASS_SPLIT_POINT);
                            String param = node.name.replace("get", "").toLowerCase();
                            traverse(className+"#"+param);
                        }
                    }

                    if (previous instanceof VarInsnNode){
                        VarInsnNode node = (VarInsnNode) previous;

                    }
                }
                currentNode = null;
            }
        }
    }


}

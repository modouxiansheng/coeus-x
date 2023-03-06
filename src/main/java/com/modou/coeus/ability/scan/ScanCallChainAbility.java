package com.modou.coeus.ability.scan;

import com.modou.coeus.ability.Ability;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @program: coeus-x
 * @description: 调用链
 * @author: hu_pf
 * @create: 2023-03-05 17:51
 **/
public class ScanCallChainAbility extends Ability {

    private AbstractScanCallHandler scanCallHandlerInterface;

    public ScanCallChainAbility(String path,AbstractScanCallHandler scanCallHandlerInterface) {
        super(path);
        this.scanCallHandlerInterface = scanCallHandlerInterface;
    }

    public void invoke(String className,String methodName){
        CoeusClassNode classNode = classRouter.getClass(className);
        CoeusMethodNode invoke = classNode.getMethod(methodName);
        this.scanCallHandlerInterface.setClassNode(classNode);
        this.scanCallHandlerInterface.setCoeusMethodNode(invoke);
        parseInvoke(invoke);
    }

    /**
     * @Description: 遍历所有入口处执行下去的方法[深度优先]
     * @Param: [invoke]
     * @return: void
     * @Author: hu_pf
     * @Date: 2023/3/5
     */
    private void parseInvoke(CoeusMethodNode start){
        if (start == null || start.invokeInfos == null){
            return;
        }
        Stack<CoeusMethodNode> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        stack.push(start);
        visited.add(start.getOwnerClass()+start.getId());
        while (!stack.isEmpty()){
            //先把自己弹出来
            CoeusMethodNode curr = stack.pop();

            for (String invokeInfo : curr.invokeInfos) {
                CoeusMethodNode next = getNext(invokeInfo);
                if (next != null){
                    ScanCallHandlerData scanCallHandlerData = new ScanCallHandlerData(next, classRouter, stack, curr);
                    scanCallHandlerData.setBegin(start);
                    // 执行真正的业务逻辑,可扩展
                    scanCallHandlerInterface.invoke(scanCallHandlerData);

                    if (!visited.contains(next.getOwnerClass()+next.getId())){
                        stack.push(next);
                        visited.add(next.getOwnerClass()+next.getId());
                    }
                }
            }
        }
    }

    private CoeusMethodNode getNext(String invokeInfo){
        String[] split = invokeInfo.split("#");
        String className = split[0];
        String methodName = split[1];
        String desc = split[2];
        CoeusClassNode aClass = classRouter.getClass(className);
        if (aClass == null){
            return null;
        }
        return aClass.getMethod(methodName,desc);
    }

}

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

    private ScanCallHandlerInterface scanCallHandlerInterface;

    public ScanCallChainAbility(String path,ScanCallHandlerInterface scanCallHandlerInterface) {
        super(path);
        this.scanCallHandlerInterface = scanCallHandlerInterface;
    }

    public void invoke(String className,String methodName){
        CoeusClassNode aClass = classRouter.getClass(className);
        CoeusMethodNode invoke = aClass.getMethod(methodName);
        parseInvoke(invoke);
    }

    private void parseInvoke(CoeusMethodNode invoke){
        if (invoke == null || invoke.invokeInfos == null){
            return;
        }
        Stack<CoeusMethodNode> stack = new Stack<>();
        Set<String> coeusMethodNodeSet = new HashSet<>();
        stack.add(invoke);
        coeusMethodNodeSet.add(invoke.getOwnerClass()+invoke.getId());
        while (!stack.isEmpty()){
            CoeusMethodNode curr = stack.pop();

            for (String invokeInfo : curr.invokeInfos) {
                String[] split = invokeInfo.split("#");
                String className = split[0];
                String methodName = split[1];
                String desc = split[2];
                CoeusClassNode aClass = classRouter.getClass(className);
                if (aClass == null){
                    continue;
                }
                CoeusMethodNode next = aClass.getMethod(methodName,desc);
                if (next != null){
                    scanCallHandlerInterface.invoke(next,classRouter);
                    if (!coeusMethodNodeSet.contains(next.getOwnerClass()+next.getId())){
                        stack.add(curr);
                        stack.add(next);
                        coeusMethodNodeSet.add(next.getOwnerClass()+next.getId());
                    }
                }
            }
        }
    }
}

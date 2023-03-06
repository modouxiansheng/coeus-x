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

    /**
    * @Description: 遍历所有入口处执行下去的方法[深度优先]
    * @Param: [invoke]
    * @return: void
    * @Author: hu_pf
    * @Date: 2023/3/5
    */
    private void parseInvoke(CoeusMethodNode invoke){
        if (invoke == null || invoke.invokeInfos == null){
            return;
        }
        Stack<CoeusMethodNode> stack = new Stack<>();
        Set<String> coeusMethodNodeSet = new HashSet<>();
        stack.add(invoke);
        coeusMethodNodeSet.add(invoke.getOwnerClass()+invoke.getId());
        while (!stack.isEmpty()){
            //先把自己弹出来
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
                    ScanCallHandlerData scanCallHandlerData = new ScanCallHandlerData(next, classRouter, stack, curr);
                    scanCallHandlerData.setBegin(invoke);
                    // 执行真正的业务逻辑,可扩展
                    scanCallHandlerInterface.invoke(scanCallHandlerData);
                    if (!coeusMethodNodeSet.contains(next.getOwnerClass()+next.getId())){
                        //再把自己及下1个节点压进去
                        //由于stack是先进后出，
                        //所以弹出的顺序就变成了 下一个节点（即：更深层的）先弹出
                        //从而达到了深度优先的效果
//                        stack.add(curr);
//                        stack.add(next);
                        stack.push(next);
                        coeusMethodNodeSet.add(next.getOwnerClass()+next.getId());
                    }
                }
            }
        }
    }
}

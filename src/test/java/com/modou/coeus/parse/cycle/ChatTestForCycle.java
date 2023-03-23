package com.modou.coeus.parse.cycle;

import com.modou.coeus.NodeFacade;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-02-27 18:33
 **/
public class ChatTestForCycle {

    public static void main(String[] args) {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/cycle";
        NodeFacade.buildSource(projectRoot);

        ClassRouter instance = ClassRouter.getInstance();
        instance.initSubClass();

        CoeusClassNode aClass = instance.getClass("com.modou.coeus.parse.cycle.CycleInvoke");
        CoeusClassNode cyCleMain = instance.getClass("com.modou.coeus.parse.cycle.CyCleMain");
        CoeusClassNode cyCleSub1 = instance.getClass("com.modou.coeus.parse.cycle.CyCleSub1");
        CoeusClassNode cyCleSub2 = instance.getClass("com.modou.coeus.parse.cycle.CyCleSub2");
        CoeusMethodNode invoke = aClass.getMethod("invoke");
        CoeusMethodNode invoke1 = cyCleMain.getMethod("invoke");
        System.out.println("1");
    }
}

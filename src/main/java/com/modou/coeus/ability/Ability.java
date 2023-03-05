package com.modou.coeus.ability;

import com.modou.coeus.NodeFacade;
import com.modou.coeus.common.ClassRouter;

/**
 * @program: coeus-x
 * @description: 可扩展的能力
 * @author: hu_pf
 * @create: 2023-03-05 17:05
 **/
public class Ability {
    public ClassRouter classRouter;

    public Ability(String path){
        NodeFacade.buildSource(path);
        classRouter = ClassRouter.getInstance();
        classRouter.initSubClass();
    }
}

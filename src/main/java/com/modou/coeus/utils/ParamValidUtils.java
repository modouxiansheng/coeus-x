package com.modou.coeus.utils;

import com.modou.coeus.node.CoeusParamNode;

import java.util.List;

/**
 * @program: coeus-x
 * @description: 参数判断工具类
 * @author: hu_pf
 * @create: 2023-03-06 11:30
 **/
public class ParamValidUtils {

    public static boolean getResultByClassNameAndParamName(List<CoeusParamNode> coeusParamNodes,String className,String paraName){
        CoeusParamNode coeusParamNode = getParamNodeByClassNameAndParamName(coeusParamNodes, className, paraName);
        return coeusParamNode != null;
    }


    /**
    * @Description: 根据传入的参数 Node 获取条件的 Node 信息
    * @Param: [coeusParamNodes, className, paraName]
    * @return: com.modou.coeus.node.CoeusParamNode
    * @Author: hu_pf
    * @Date: 2023/3/6
    */
    public static CoeusParamNode getParamNodeByClassNameAndParamName(List<CoeusParamNode> coeusParamNodes,String className,String paraName){
        if (coeusParamNodes == null){
            return null;
        }
        for (CoeusParamNode coeusParamNode : coeusParamNodes){
            if (coeusParamNode.isConformParam(className,paraName)){
                return coeusParamNode;
            }
        }
        return null;
    }
}

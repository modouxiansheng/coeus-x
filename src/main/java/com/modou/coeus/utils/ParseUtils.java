package com.modou.coeus.utils;

import com.modou.coeus.common.Constant;
import com.modou.coeus.domain.ClassAndMethodData;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-05-16 16:56
 **/
public class ParseUtils {


    public static ClassAndMethodData parseClassAndMethodString(String str){
        String[] split = str.split(Constant.SPLIT);

       return new ClassAndMethodData(split[0],split[1],split[2]);
    }
}

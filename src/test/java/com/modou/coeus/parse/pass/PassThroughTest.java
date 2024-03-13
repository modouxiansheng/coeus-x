package com.modou.coeus.parse.pass;

import com.modou.coeus.NodeFacade;
import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.utils.*;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.ASMifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-10-09 18:15
 **/
public class PassThroughTest {


    public void main(String args) throws IOException {
        String cmd = new A().methodA(args,"x",new Student());

        new B().methodB(cmd);
    }

     class A{

         private String name;
        public String methodA(String name,String age1,Student age){
            this.name = age.getName();
            return this.name;
        }

        private String age(String name){
            return name;
        }
    }

    class B{
        public void methodB(String name) throws IOException {
            new C().methodC(name);
        }
    }

    class C{

       private String name;

        public void methodC(String name) throws IOException {
            this.name = name;
        }
    }

    class Student{

        private String name ;

        public String getName(){
            this.name = "xxx";
            return name;
        }
    }
}

package com.modou.coeus.ability.scan;

import com.modou.coeus.common.ClassRouter;
import com.modou.coeus.common.Constant;
import com.modou.coeus.node.CoeusClassNode;
import com.modou.coeus.node.CoeusMethodNode;
import com.modou.coeus.node.CoeusParamNode;
import com.modou.coeus.utils.GraphCall;
import com.modou.coeus.utils.ModelGeneratorMethodVisitor;
import com.modou.coeus.utils.ParamParseUtils;
import com.modou.coeus.utils.PassthroughDataflowClassVisitor;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.commons.JSRInlinerAdapter;
import jdk.internal.org.objectweb.asm.tree.*;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
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

    public static Set<GraphCall> discoveredCalls = new HashSet<>();


    private Map<CoeusMethodNode, ArrayList<CoeusMethodNode>> adjList = new HashMap<>();

    private ScanCallHandlerData scanCallHandlerData;

    @Override
    public void doInvoke(ScanCallHandlerData scanCallHandlerData) {
        // 到这个方法节点,然后查看自己方法节点有没有对这个参数的赋值, 如果有的话就将此快照栈信息全部输出出来
        if (adjList.containsKey(scanCallHandlerData.getCoeusMethodNodePre())){
            adjList.get(scanCallHandlerData.getCoeusMethodNodePre()).add(scanCallHandlerData.getCoeusMethodNodeCurrent());
        }else {
            ArrayList<CoeusMethodNode> arrayList = new ArrayList<>();
            arrayList.add(scanCallHandlerData.getCoeusMethodNodeCurrent());
            adjList.put(scanCallHandlerData.getCoeusMethodNodePre(),arrayList);
        }
        this.scanCallHandlerData = scanCallHandlerData;
    }

    /**
     * @Description: 根据传入的参数信息获取其调用链
     * @Param: [paramInfo]
     * @return: java.util.List<java.util.List<com.modou.coeus.node.CoeusMethodNode>>
     * @Author: hu_pf
     * @Date: 2023/3/6
     */
    public List<List<CoeusMethodNode>> getTraces(String paramInfo) {
        // todo 需要做一个全局调用的逆拓扑排序
        List<List<CoeusMethodNode>> traverse = traverse(paramInfo);
        List<CoeusMethodNode> coeusMethodNodes1 = topologicallySortMethodCalls();
        Map<CoeusMethodNode, Set<Integer>> passthroughDataflow = new HashMap<>();
        ClassRouter classRouter = this.scanCallHandlerData.getClassRouter();

        for (List<CoeusMethodNode> coeusMethodNodes : traverse) {
            for (int i = coeusMethodNodes.size() - 1; i >= 0; i--) {

                CoeusMethodNode coeusMethodNode = coeusMethodNodes.get(i);

                CoeusClassNode coeusClassNode = classRouter.getClass(coeusMethodNode.getOwnerClass());

                // 做单个方法中的参数设置,单个方法中的返回值和入参之间的关系,如果是有关系则进行设置.
                PassthroughDataflowClassVisitor passthroughDataflowClassVisitor = new PassthroughDataflowClassVisitor(Opcodes.ASM5,coeusMethodNode,passthroughDataflow,classRouter);

                coeusClassNode.getMetadata().accept(passthroughDataflowClassVisitor);

                passthroughDataflow.put(coeusMethodNode,passthroughDataflowClassVisitor.getReturnTaint());
            }
        }

        for (List<CoeusMethodNode> coeusMethodNodes : traverse) {
            for (int i = coeusMethodNodes.size() - 1; i >= 0; i--) {

                CoeusMethodNode coeusMethodNode = coeusMethodNodes.get(i);
                MethodNode metaDataMethod = coeusMethodNode.getMetaData();

                CoeusClassNode coeusClassNode = classRouter.getClass(coeusMethodNode.getOwnerClass());

                // 做调用关系之间的传递参数设置
                ClassNode metadata = coeusClassNode.getMetadata();
                ModelGeneratorMethodVisitor modelGeneratorMethodVisitor = new ModelGeneratorMethodVisitor(classRouter,passthroughDataflow,Opcodes.ASM5,null,metadata.name,metaDataMethod.access,metaDataMethod.name,metaDataMethod.desc);

                metaDataMethod.accept(modelGeneratorMethodVisitor);

            }
        }

        return traverse(paramInfo);
    }


    private void invoke(){

        Map<ModelGeneratorMethodVisitor.Handle, Set<GraphCall>> graphCallMap = new HashMap<>();

        for (GraphCall graphCall : discoveredCalls) {

            ModelGeneratorMethodVisitor.Handle caller = graphCall.getCallerMethod();

            if (!graphCallMap.containsKey(caller)) {
                Set<GraphCall> graphCalls = new HashSet<>();
                graphCalls.add(graphCall);
                graphCallMap.put(caller, graphCalls);
            } else {
                graphCallMap.get(caller).add(graphCall);
            }
        }

        Set<GadgetChainLink> exploredMethods = new HashSet<>();
        LinkedList<GadgetChain> methodsToExplore = new LinkedList<>();

        ModelGeneratorMethodVisitor.Handle handle = new ModelGeneratorMethodVisitor.Handle("com/modou/coeus/parse/pass/order/OrderBuyUnitTest","invoke","(Lcom/modou/coeus/parse/pass/order/InvokeRequest;)V");

        GadgetChainLink srcLink = new GadgetChainLink(handle, 1);

        methodsToExplore.add(new GadgetChain(Arrays.asList(srcLink)));
        exploredMethods.add(srcLink);

        long iteration = 0;

        Set<GadgetChain> discoveredGadgets = new HashSet<>();

        while (methodsToExplore.size() > 0) {

            iteration += 1;

            GadgetChain chain = methodsToExplore.pop();
            GadgetChainLink lastLink = chain.links.get(chain.links.size()-1);

            // 集合是 方法对应有影响的对象
            Set<GraphCall> methodCalls = graphCallMap.get(lastLink.method);
            if (methodCalls != null) {
                // 然后看方法的执行链条下的每一个方法
                for (GraphCall graphCall : methodCalls) {

                    // 如果方法调用参数和 有影响的参数不相等,那么就跳过
                    if (graphCall.getCallerArgIndex() != lastLink.taintedArgIndex) {
                        continue;
                    }

                    // 改造过的,获取该方法下的所有方法实现.

                    ModelGeneratorMethodVisitor.Handle methodImpl = graphCall.getTargetMethod();

                    GadgetChainLink newLink = new GadgetChainLink(methodImpl, graphCall.getTargetArgIndex());
                    if (exploredMethods.contains(newLink)) {
                        continue;
                    }

                    GadgetChain newChain = new GadgetChain(chain, newLink);
                    if (isSink(methodImpl, graphCall.getTargetArgIndex())) {
                        discoveredGadgets.add(newChain);
                    } else {
                        methodsToExplore.add(newChain);
                        exploredMethods.add(newLink);
                    }
                }
            }
        }

        System.out.println("1");
    }

    private boolean isSink(ModelGeneratorMethodVisitor.Handle method, int argIndex){

        if (method.getOwner().contains("OrderBo") && method.getName().equals("setPrice")){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public List<List<CoeusMethodNode>> getTraces2(String paramInfo) {
        // todo 需要做一个全局调用的逆拓扑排序,或者将最后一个调用的方法下所有调用方法加上去
        List<CoeusMethodNode> traverse = topologicallySortMethodCalls();
        Map<CoeusMethodNode, Set<Integer>> passthroughDataflow = new HashMap<>();
        ClassRouter classRouter = this.scanCallHandlerData.getClassRouter();

        for (CoeusMethodNode coeusMethodNode : traverse) {
            CoeusClassNode coeusClassNode = classRouter.getClass(coeusMethodNode.getOwnerClass());

            // 做单个方法中的参数设置,单个方法中的返回值和入参之间的关系,如果是有关系则进行设置
            PassthroughDataflowClassVisitor passthroughDataflowClassVisitor = new PassthroughDataflowClassVisitor(Opcodes.ASM5,coeusMethodNode,passthroughDataflow,classRouter);

            coeusClassNode.getMetadata().accept(passthroughDataflowClassVisitor);

            passthroughDataflow.put(coeusMethodNode,passthroughDataflowClassVisitor.getReturnTaint());
        }

        for (CoeusMethodNode coeusMethodNode : traverse) {
            MethodNode metaDataMethod = coeusMethodNode.getMetaData();

            CoeusClassNode coeusClassNode = classRouter.getClass(coeusMethodNode.getOwnerClass());

            // 做调用关系之间的传递参数设置
            ClassNode metadata = coeusClassNode.getMetadata();
            ModelGeneratorMethodVisitor modelGeneratorMethodVisitor = new ModelGeneratorMethodVisitor(classRouter,passthroughDataflow,Opcodes.ASM5,null,metadata.name,metaDataMethod.access,metaDataMethod.name,metaDataMethod.desc);

            metaDataMethod.accept(modelGeneratorMethodVisitor);
        }

        invoke();

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


    private List<CoeusMethodNode> topologicallySortMethodCalls() {
        Map<CoeusMethodNode, ArrayList<CoeusMethodNode>> outgoingReferences = new HashMap<>();
        for (Map.Entry<CoeusMethodNode, ArrayList<CoeusMethodNode>> entry : adjList.entrySet()) {
            CoeusMethodNode method = entry.getKey();
            outgoingReferences.put(method, new ArrayList<>(entry.getValue()));
        }

        // Topological sort methods
        Set<CoeusMethodNode> dfsStack = new HashSet<>();
        Set<CoeusMethodNode> visitedNodes = new HashSet<>();
        List<CoeusMethodNode> sortedMethods = new ArrayList<>(outgoingReferences.size());
        for (CoeusMethodNode root : outgoingReferences.keySet()) {
            dfsTsort(outgoingReferences, sortedMethods, visitedNodes, dfsStack, root);
        }

        return sortedMethods;
    }

    private static void dfsTsort(Map<CoeusMethodNode, ArrayList<CoeusMethodNode>> outgoingReferences,
                                 List<CoeusMethodNode> sortedMethods, Set<CoeusMethodNode> visitedNodes,
                                 Set<CoeusMethodNode> stack, CoeusMethodNode node) {

        if (stack.contains(node)) {
            return;
        }
        if (visitedNodes.contains(node)) {
            return;
        }
        ArrayList<CoeusMethodNode> outgoingRefs = outgoingReferences.get(node);
        if (outgoingRefs == null) {
            return;
        }

        stack.add(node);
        for (CoeusMethodNode child : outgoingRefs) {
            dfsTsort(outgoingReferences, sortedMethods, visitedNodes, stack, child);
        }
        stack.remove(node);
        visitedNodes.add(node);
        sortedMethods.add(node);
    }




    private boolean valid(CoeusMethodNode curNode,List<List<CoeusMethodNode>> allPaths,ArrayList<CoeusMethodNode> curPath,String className,String paramName){
        if (curNode == null){
            return Boolean.FALSE;
        }
        for (CoeusParamNode coeusParamNode : curNode.coeusParamNodes) {
            if (ParamParseUtils.isParameterAssigned(curNode.getMetaData(),coeusParamNode)
                    && coeusParamNode.isConformParam(className,paramName)){
                allPaths.add(curPath);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static class GadgetChainLink {
        private final ModelGeneratorMethodVisitor.Handle method;
        private final int taintedArgIndex;

        private GadgetChainLink(ModelGeneratorMethodVisitor.Handle method, int taintedArgIndex) {
            this.method = method;
            this.taintedArgIndex = taintedArgIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GadgetChainLink that = (GadgetChainLink) o;

            if (taintedArgIndex != that.taintedArgIndex) return false;
            return method != null ? method.equals(that.method) : that.method == null;
        }

        @Override
        public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + taintedArgIndex;
            return result;
        }
    }

    private static class GadgetChain {
        private final List<GadgetChainLink> links;

        private GadgetChain(List<GadgetChainLink> links) {
            this.links = links;
        }

        private GadgetChain(GadgetChain gadgetChain, GadgetChainLink link) {
            List<GadgetChainLink> links = new ArrayList<GadgetChainLink>(gadgetChain.links);
            links.add(link);
            this.links = links;
        }
    }

}

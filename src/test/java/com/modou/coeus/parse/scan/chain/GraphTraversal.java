package com.modou.coeus.parse.scan.chain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GraphTraversal {

    // 有向图，使用邻接表表示
    private Map<Integer, ArrayList<Integer>> adjList;

    public GraphTraversal(Map<Integer, ArrayList<Integer>> adjList) {
        this.adjList = adjList;
    }

    /**
     * 使用深度优先遍历和栈实现的图遍历方法
     * 
     * @param start 起点
     * @param end   终点
     * @return 所有从起点到终点的路径，每条路径以ArrayList形式返回
     */
    public ArrayList<ArrayList<Integer>> traverse(int start, int end) {
        ArrayList<ArrayList<Integer>> allPaths = new ArrayList<>(); // 存储所有路径
        Stack<ArrayList<Integer>> stack = new Stack<>(); // 存储当前遍历的路径
        ArrayList<Integer> path = new ArrayList<>(); // 存储起点
        path.add(start);
        stack.push(path);
        while (!stack.empty()) {
            ArrayList<Integer> curPath = stack.pop(); // 取出当前遍历的路径
            int curNode = curPath.get(curPath.size() - 1); // 取出当前遍历的节点
            if (curNode == end) { // 如果当前节点是终点，将该路径加入结果集
                allPaths.add(curPath);
                continue; // 继续遍历
            }
            ArrayList<Integer> neighbors = adjList.get(curNode); // 获取当前节点的所有邻居
            for (int neighbor : neighbors) {
                if (!curPath.contains(neighbor)) { // 避免死循环
                    ArrayList<Integer> newPath = new ArrayList<>(curPath); // 复制当前路径
                    newPath.add(neighbor); // 添加邻居
                    stack.push(newPath); // 将新路径加入栈中
                }
            }
        }
        return allPaths;
    }

    public static void main(String[] args) {
        // 构造一个有向图
        Map<Integer, ArrayList<Integer>> adjList = new HashMap<>();
        adjList.put(1, new ArrayList<Integer>(){{add(2); add(3);}});
        adjList.put(2, new ArrayList<Integer>(){{add(4);}});
        adjList.put(3, new ArrayList<Integer>(){{add(4); add(5);}});
        adjList.put(4, new ArrayList<Integer>(){{add(6);}});
        adjList.put(5, new ArrayList<Integer>(){{add(6);}});
        adjList.put(6, new ArrayList<Integer>());

        GraphTraversal graph = new GraphTraversal(adjList);

        // 测试
        int start = 1;
        int end = 6;
        ArrayList<ArrayList<Integer>> allPaths = graph.traverse(start, end);
        System.out.printf("从%d到%d的所有路径：%n", start, end);
        for (ArrayList<Integer> path : allPaths) {
            System.out.println(path);
        }
    }
}

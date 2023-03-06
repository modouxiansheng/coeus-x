package com.modou.coeus.parse.scan.chain;

import java.util.*;

class Graph {
    private int V;
    private LinkedList<Integer>[] adj;

    Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i) {
            adj[i] = new LinkedList();
        }
    }

    void addEdge(int v, int w) {
        adj[v].add(w);
    }

    List<Integer> DFS(int s, int d) {
        boolean[] visited = new boolean[V];
        Map<Integer, Integer> parent = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        List<Integer> path = new ArrayList<>();

        stack.push(s);
        parent.put(s, -1);

        while (!stack.empty()) {
            s = stack.pop();
            if (!visited[s]) {
                visited[s] = true;

                if (s == d) {
                    // construct path
                    while (s != -1) {
                        path.add(0, s);
                        s = parent.get(s);
                    }
                    return path;
                }

                Iterator<Integer> i = adj[s].iterator();
                while (i.hasNext()) {
                    int n = i.next();
                    if (!visited[n]) {
                        stack.push(n);
                        parent.put(n, s);
                    }
                }
            }
        }

        // no path found
        return null;
    }

    public static void main(String[] args) {
        Graph g = new Graph(4);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);

        int start = 0;
        int end = 3;

        List<Integer> path = g.DFS(start, end);

        if (path != null) {
            System.out.print("Path from " + start + " to " + end + ": ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i) + " ");
            }
        } else {
            System.out.println("No path found from " + start + " to " + end);
        }
    }
}

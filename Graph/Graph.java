import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Graph {
    public Node breadthFirstSearch(Node root, int target){
        if(root == null) return null;

        Queue<Node> queue = new LinkedList<>();
        Node node = root;
        queue.add(node);

        while(!queue.isEmpty()){
            node = queue.remove();
            if(node.getValue() == target) return node;
            Node nextNode;
            while((nextNode = getUnvisitedNeighbor(node)) != null)
                queue.add(nextNode);
        }
        return null;
    }

    public Node depthFirstSearch(Node root, int target){
        if(root == null) return null;

        Stack<Node> stack = new Stack<>();
        Node node = root;
        stack.push(node);

        while(!stack.isEmpty()){
            node = stack.pop();
            if(node.getValue() == target) return node;
            Node nextNode;
            while((nextNode = getUnvisitedNeighbor(node)) != null)
                stack.push(nextNode);
        }
        return null;
    }

    private Node getUnvisitedNeighbor(Node node){
        Node[] neighboringNodes = node.getNeighbors();
        for (int i = 0 ; i < neighboringNodes.length ; i ++){
            Node neighbor = neighboringNodes[i];
            if (!neighbor.isVisited()) {
                neighbor.setVisited();
                return neighbor;
            }
        }
        return null;
    }
}

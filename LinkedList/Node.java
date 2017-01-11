public class Node{ 

private DataItem DataItem;
private Node nextNode;

public Node(DataItem DataItem, Node nextNode){
	this.DataItem=DataItem;
	this.nextNode=nextNode;
}

public void setNextNode(Node nextNode){
	this.nextNode=nextNode;
}

public Node getNextNode(){
	return nextNode;
}

public DataItem getDataItem(){
	return DataItem;
}

public void setDataItem(DataItem DataItem){
	this.DataItem=DataItem;
}

}
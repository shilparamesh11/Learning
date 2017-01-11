public class LinkedList
{
	private Node head;

	public LinkedList()
	{
		head=new Node(null,null);
	}

	// insert element at the beginning of the list
	public void prependElement(DataItem data)
	{
		addElementAtIndex(0,data);
	}

	// insert a element at index
	public void addElementAtIndex(int index, DataItem data)
	{
		if(index==0)
		{
			Node newNode=new Node(data,head.getNextNode());
			head.setNextNode(newNode);
		}
		else
		{
			Node prevNode=nodeAtIndex(index-1);
			if(prevNode!=null)
			{
				Node nextNode=prevNode.getNextNode();
				Node newNode=new Node(data,nextNode);
				prevNode.setNextNode(newNode);
			}
			else
			{
				System.out.println("Index greater than the size of the list");
			}
		}
	}

	// delete node at the beginning of the list
	public void removeElement()
	{
		removeElementAtIndex(0);
	}

	// delete a node at index
	public void removeElementAtIndex(int index)
	{
		if(index==0)
		{
			Node toBeDeletedNode=head.getNextNode();
			if(toBeDeletedNode!=null)
			{
				Node nextNode=toBeDeletedNode.getNextNode();
				head.setNextNode(nextNode);
				toBeDeletedNode.setNextNode(null);
			}
			else
			{
				System.out.println("No nodes to be deleted");
			}
		}
		else
		{
			Node prevNode=nodeAtIndex(index-1);
			if(prevNode!=null)
			{
				Node targetNode=prevNode.getNextNode();
				Node nextNode=targetNode.getNextNode();
				targetNode.setNextNode(null);
				prevNode.setNextNode(nextNode);
			}
			else
			{
				System.out.println("Index greater than the size of the list");
			}
		}
	}

	// display all nodes data
	public void displayAllElements()
	{
		Node nodes=head.getNextNode();
		int i=0;
		while(nodes!=null)
		{
			DataItem data=nodes.getDataItem();
			System.out.println("Node "+i+" : "+data.toString());
			nodes=nodes.getNextNode();
			i++;
		}
	}

	// reverse order of linked list
	public void reverse()
	{
		Node currentNode=head.getNextNode();
		Node prevNode=null;
		Node nextNode=null;
		while(currentNode!=null)
		{	
			nextNode=currentNode.getNextNode();
			currentNode.setNextNode(prevNode);
			prevNode=currentNode;
			currentNode=nextNode;
		}
		head.setNextNode(prevNode);
	}

	// reverse order of linked list
	public void searchKey(int key)
	{
		DataItem data=null;
		int i=0;
		Node node=head.getNextNode();
		Boolean foundFlag=false;
		while(node!=null)
		{
			data=node.getDataItem();
			if(data.getKey()==key)
			{
				System.out.println("Node at index : "+i+" has data item : "+data.toString());
				foundFlag=true;
			}
			i++;
			node=node.getNextNode();
		}
		if(foundFlag==false)
		{
			System.out.println("No matches found");
		}
	}

	// return data item at particular node
	public DataItem dataAtIndex(int index)
	{
		Node nodes=nodeAtIndex(index);
		if(nodes!=null)
		{
			return nodes.getDataItem();
		}
		else
		{
			return null;
		}
	}

	// return node at particular index
	private Node nodeAtIndex(int index){
		if(index<0)
		{
			return null;
		}
		else
		{
			Node nodes=head.getNextNode();
			int i=0;
			while(i<index && nodes!=null)
			{
				nodes=nodes.getNextNode();
				i++;
			}
			return nodes;
		}
	}

	// return the size of linked list
	public int size()
	{
		int count=0;
		Node nodes=head.getNextNode();
		while(nodes!=null)
		{
			++count;
			nodes=nodes.getNextNode();
		}
		return count;
	}

}
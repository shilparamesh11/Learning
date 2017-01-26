import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;

public class Queue<T>
{
	private int size;
	private int rarePointer=-1;
	private T[] queueArray;

	public Queue()
	{
			this.size=10;
			queueArray=(T[]) new Object[size];
	}

	public Queue(int size)
	{
		this.size=size;
		queueArray=(T[]) new Object[size];
	}

	public void enQueue(T element)
	{
		if(isFull())
		{
			throw new UnsupportedOperationException("Queue is full");
		}
		else
		{
			rarePointer++;
			queueArray[rarePointer]=element;
			System.out.println(element+" pushed to queue");
		}
	}

	public T deQueue()
	{
		if(isEmpty())
		{
			throw new UnsupportedOperationException("Queue is empty");
		}
		else
		{
			T element=queueArray[0];
			for(int i=0;i<size-1;i++)
			{
				queueArray[i]=queueArray[i+1];
			}
			rarePointer--;
			return element;
		}
	}

	public boolean isFull()
	{
		if(rarePointer==size-1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isEmpty()
	{
		if(rarePointer<0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public T peek()
	{
		if(isEmpty())
		{
			throw new UnsupportedOperationException("Queue is empty");
		}
		else
		{
			return queueArray[0];
		}
	}

	public int search(T element)
	{
		if(!isEmpty())
		{
			for(int i=0;i<size;i++)
			{
				if(queueArray[i]!=null && queueArray[i].equals(element))
				{
					return i+1;
				}
			}
		}
		throw new NoSuchElementException("Element not found : "+element.toString());
	}
}
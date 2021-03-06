import java.util.NoSuchElementException;
import java.lang.UnsupportedOperationException;

public class Stack<T>
{
	private static final int DEFAULT_STACK_SIZE=10;
	private int size;
	private int topPointer=-1;
	private T[] stackArray;

	public Stack(int size)
	{
		this.size=size;
		stackArray=(T[]) new Object[size];
	}

	public Stack()
	{
		this(DEFAULT_STACK_SIZE);
	}

	public void push(T element)
	{
		if(isFull())
		{
			throw new UnsupportedOperationException("Stack is full");
		}
		else
		{
			topPointer++;
			stackArray[topPointer]=element;
			System.out.println(element+" pushed to stack");
		}
	}

	public T pop()
	{
		if(isEmpty())
		{
			throw new UnsupportedOperationException("Stack is empty");
		}
		else
		{
			T element=stackArray[topPointer];
			topPointer--;
			return element;
		}
	}

	public boolean isFull()
	{
		return topPointer==size-1;
	}

	public boolean isEmpty()
	{
		return topPointer<0;
	}

	public T peek()
	{
		if(isEmpty())
		{
			throw new UnsupportedOperationException("Stack is empty");
		}
		else
		{
			return stackArray[topPointer];
		}
	}

	public int search(T element)
	{
		if(!isEmpty())
		{
			for(int i=0;i<=topPointer;i++)
			{
				if(stackArray[i].equals(element))
				{
					return size-i;
				}
			}
		}
		throw new NoSuchElementException("Element not found : "+element.toString());
	}
}
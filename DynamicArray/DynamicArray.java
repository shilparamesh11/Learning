import java.util.NoSuchElementException;

public class DynamicArray<T>
{
	int sizePointer=0;
	int size;
	int increment;
	T[] dynamicArray;

	public DynamicArray()
	{
		size=10;
		increment=size;
		dynamicArray=(T[]) new Object[size];
	}

	public DynamicArray(int size)
	{
		this.size=size;
		increment=size;
		dynamicArray=(T[]) new Object[size];
	}

	public void addElement(T element)
	{
		if(sizePointer==size)
		{
			increaseSize();
		}
		dynamicArray[sizePointer]=element;
		sizePointer++;
	}

	public void addElementAtNode(int index, T element)
	{
		if(index<sizePointer)
		{
			dynamicArray[index]=element;
		}
		else
		{
			addElement(element);
			throw new ArrayIndexOutOfBoundsException("index "+index+" is greater than the size of array "+(sizePointer-1)+" \nElement added to end of array..");
		}
	}

	private void increaseSize()
	{
		T[] tempArray=(T[]) new Object[size+increment];
		for(int i=0;i<sizePointer;i++)
		{
			tempArray[i]=dynamicArray[i];
		}
		dynamicArray=tempArray;
		size=sizePointer+increment;
	}

	public int searchElement(T element)
	{
		for(int i=0;i<sizePointer;i++)
		{
			if(dynamicArray[i].equals(element))
			{
				return i;
			}
		}
		throw new NoSuchElementException("Element not found : "+element.toString());
	}

	public T getElementAtIndex(int index)
	{
		if(index<sizePointer)
		{
			return dynamicArray[index];
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException("index "+index+" is greater than the size of array "+sizePointer);
		}
	}

	public void removeElement(T element)
	{
		int index=searchElement(element);
		if(index>0)
		{
			removeElementAtIndex(index);
		}
	}

	public void removeElementAtIndex(int index)
	{
		if(index<sizePointer)
		{
			for(int i=index;i<sizePointer-1;i++)
			{
				dynamicArray[index]=dynamicArray[index+1];
			}
			dynamicArray[sizePointer-1]=null;
			sizePointer--;
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException("index "+index+" is greater than the size of array "+sizePointer);
		}
	}

	public int size()
	{
		return sizePointer;
	}

}




import java.util.Iterator;

/**
 * A linked list.
 * @param <T> Generic object type.
 */
public class SinglyLinkedList<T extends Comparable<T>> implements Iterable<T>
{
    /**
     * Size of the list.
     */
    private int size;
    /**
     * Head of the list.
     */
    private Node head;
    /**
     * Tail of the list.
     */
    private Node tail;

    /**
     * Constructs the LinkedList.
     */
    public SinglyLinkedList()
    {
        size = 0;
        head = null;
        tail = null;
    }
    
    /**
    * Adds a value to the end of the list.
    * @param value Value to be added.
    */
    public void add(T value)
    {
        Node newNode = new Node(value);
        if(size != 0)
        {
            tail.next = newNode;
        }
        else
        {
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    /**
     * Inserts a value to the proper location in the list so that the list order is preserved (in descending order).
     * @param newValue Value to be added.
     */
    public void insert(T newValue)
    {
        Node newNode = new Node(newValue);
        if(size == 0)
        {
            head = newNode;
            tail = newNode;
        }
        else if(head.value.compareTo(newValue) < 0)
        {
            newNode.next = head;
            head = newNode;
        }
        else if(tail.value.compareTo(newValue) >= 0)
        {
            tail.next = newNode;
            tail = newNode;
        }
        else
        {
            Node curNode = head;
            for(int i=0; i<size; i++)
            {
                if(curNode.next.value.compareTo(newValue) < 0)
                {
                    newNode.next = curNode.next;
                    curNode.next = newNode;
                    break;
                }
                else
                {
                    curNode = curNode.next;
                }
            }
        }
        size++;
    }

    /**
     * Removes a single item from the list based on its index.
     * @param index The index at which the item is to be removed.
     * @return Returns removed item.
     */
    public T remove(int index)
    {
        if(index < 0 || index >= size)
            throw new RuntimeException("Invalid index: " + index);

        Node removed;
        
        if(size == 1)
        {
            removed = head;
            head = null;
            tail = null;
        }
        else
        {
            if(index == 0)
            {
                removed = head;
                head = head.next;
            }
            else
            {
                Node curNode = head;
                for(int i=0; i<index-1; i++)
                {
                    curNode = curNode.next;
                }
                removed = curNode.next;
                curNode.next = curNode.next.next;
            }
        }
        size--;
        return removed.value;
        
    }

    /**
     * Returns a single item from the list based on its index.
     * @param index The index where the item is.
     * @return Returns the item.
     */
    public T get(int index)
    {
        if(index < 0 || index >= size)
            throw new RuntimeException("Invalid index: " + index);
        
        Node result = head;
        for(int i=0; i<index; i++)
        {
            result = result.next;
        }
        return result.value;
    }

    /**
     * Size of the list.
     * @return Returns the size.
     */
    public int size()
    {
        return size;
    }

    /**
     * Whether the list is empty and has a size of 0.
     * @return Returns true if empty, false otherwise.
     */
    public boolean isEmpty()
    {
        if(size == 0)
            return true;
        else
            return false;
    }

    /**
     * An iterator to iterate through the list.
     * @return Returns an iterator that iterates through the list.
     */
    @Override
    public Iterator<T> iterator() 
    {
        return new Iterator<T>() 
        {
            Node current = head;
            int index = 0;

            @Override
            public boolean hasNext() 
            {
                if(index < size)
                    return true;
                else
                    return false;
            }

            @Override
            public T next() 
            {
                Node node = current;
                current = current.next;
                index++;
                return node.value;
            }
        };
    }

    /**
     * Inner class representing a node in a linked list.
     */
    private class Node
    {
        /**
         * The value contained in the node.
         */
        T value;
        /**
         * The next node in the list.
         */
        Node next;
        /**
         * Constructor for the node.
         * @param v The value to assign for the node.
         */
        public Node(T v)
        {
            value = v;
        }
    }
}

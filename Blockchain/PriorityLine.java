import java.util.Iterator;

/**
 * Implements a priority queue.
 * @param <T> Generic object type.
 */
public class PriorityLine<T extends Comparable<T>> implements Iterable<T>
{
    /**
     * Linked list that we use for the queue.
     */
    private SinglyLinkedList<T> singlyLinkedList;

    /**
     * Constructor for the queue.
     */
    public PriorityLine()
    {
        singlyLinkedList = new SinglyLinkedList<>();
    }

    /**
     * Inserts the element given into its proper location based on its value (Descending order).
     * @param element The element to insert.
     */
    public void enqueue(T element)
    {
        singlyLinkedList.insert(element);
    }

    /**
     * Dequeues the first element in the queue.
     * @return Returns the removed element.
     */
    public T dequeue()
    {
        return singlyLinkedList.remove(0);
    }

    /**
     * The size of the queue.
     * @return Returns the size.
     */
    public int size()
    {
        return singlyLinkedList.size();
    }

    /**
     * Determines whether the queue is empty or not.
     * @return Returns true if it's empty, false otherwise.
     */
    public boolean isEmpty()
    {
        return singlyLinkedList.isEmpty();
    }

    /**
     * Retreives the first element in the queue that will be removed when dequeue is called.
     * @return Retunrs the element to be dequeued.
     */
    public T peek()
    {
        return singlyLinkedList.get(0);
    }

    /**
     * Iterates through the queue.
     * @return Retunrs an iterator thats used to iterate through the queue.
     */
    @Override
    public Iterator<T> iterator() 
    {
        return singlyLinkedList.iterator();
    }
}

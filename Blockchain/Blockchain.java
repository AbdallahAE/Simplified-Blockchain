import java.util.Iterator;

/**
 * The class represents the entire blockchain.
 */
public class Blockchain implements Iterable<Block>
{
    /**
     * List that contains the blocks for the blockchain.
     */
    private SinglyLinkedList<Block> blockchain;

    /**
     * The concstructor takes a priority queue and creates the linked list of blocks.
     * The block must contain the minimum number of transactions that satisfy the threshold criterion.
     * @param queue The queue that contains the transactions.
     * @param threshold is the minimum amount of cumulative fees that is required to create a new block.
     */
    public Blockchain(PriorityLine<Transaction> queue, int threshold)
    {
        blockchain = new SinglyLinkedList<>();
        Block block = new Block();
        int fees = 0;
        while(!queue.isEmpty())
        {
            if(queue.peek().getFee() + fees < threshold)
            {
                fees += queue.peek().getFee();
                block.addTransaction(queue.dequeue());
            }
            else
            {
                block.addTransaction(queue.dequeue());
                blockchain.add(block);
                block = new Block();
                fees = 0;
            }
        }
        if(block.numOfTransactions() > 0)
            blockchain.add(block);
    }

    /**
     * An iterator to iterate through the Blockchain.
     * @return Returns an iterator that iterates through the Blockchain.
     */
    @Override
    public Iterator<Block> iterator()
    {
        return blockchain.iterator();
    }

}

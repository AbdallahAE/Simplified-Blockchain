import java.util.Iterator;

/**
 * Represents a single block in the blockchain.
 */
public class Block implements Comparable<Block>, Iterable<Transaction>
{
    /**
     * The roothash of the block from the merkle tree.
     */
    private String rootHash;
    /**
     * The list that contains the Transactions in the block.
     */
    private SinglyLinkedList<Transaction> block;

    /**
     * Constructor for creating a new Block instance.
     */
    public Block()
    {
        block= new SinglyLinkedList<>();
    }

    /**
     * Adds a new transaction to the block.
     * @param t The transaction to be added.
     */
    public void addTransaction(Transaction t)
    {
        block.add(t);
    }

    /**
     * Returns the number of transactions in the block.
     * @return The number of transactions in the block.
     */
    public int numOfTransactions()
    {
        return block.size();
    }

    /**
     * Retrieves the root hash of the block.
     * @return The root hash of the block.
     */
    public String getRootHash()
    {
        return rootHash;
    }

    /**
     * Sets the root hash of the block.
     * @param hashCode The hash code to set as the root hash.
     */
    public void setRootHash(String hashCode)
    {
        rootHash = hashCode;
    }

    /**
     * An iterator to iterate through the Block.
     * @return Returns an iterator that iterates through the Block.
     */
    @Override
    public Iterator<Transaction> iterator() {
        return block.iterator();
    }

    /**
     * Comparison between two blocks is based on the number of transactions they contain. The block that has more transactions is considered larger.
     * @return Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Block o) {
        return numOfTransactions() - o.numOfTransactions();
    }
}

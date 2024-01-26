/**
 * The Transaction object that contains information regarding a specific transaction.
 */
public class Transaction implements Comparable<Transaction>
{
    /**
     * String representing who's sending the transaction.
     */
    private String sender;
    /**
     * String representing who's recieving the transaction.
     */
    private String receiver;
    /**
     * Integer representing the amount of money being sent.
     */
    private int amount;
    /**
     * Integer repersenting the amount of fees being collected.
     */
    private int fee;
    
    /**
     * Constructs the Transaction object.
     * @param sender String representing who's sending the transaction.
     * @param receiver String representing who's recieving the transaction.
     * @param amount Integer representing the amount of money being sent.
     * @param fee Integer repersenting the amount of fees being collected.
     */
    public Transaction(String sender, String receiver, int amount, int fee)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.fee = fee;
    }

    /**
     * Converts the Transaction object into a string.
     * @return String format: "sender recieiver amount fee".
     */
    public String toString()
    {
        return String.format("%s %s %d %d", sender, receiver, amount, fee);
    }

    /**
     * Gets the fee for the Transaction.
     * @return Int fee.
     */
    public int getFee()
    {
        return fee;
    }

    /*
     * @param t Transaction to compare to.
     * @return Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Transaction t) 
    {
        return fee - t.fee;
    }
}

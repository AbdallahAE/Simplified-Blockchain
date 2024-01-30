import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 * A Utilities class that contains methods useful towards creating a blockchian.
 */
public final class Utilities
{
    /**
     * Reads the transactions from a text file and adds them to a priority queue.
     * @param pgmFile The filename of the text file.
     * @return Returns a queue of the Transactions.
     */
    public static PriorityLine<Transaction> loadTransactions(String pgmFile)
    {
        PriorityLine<Transaction> priorityLine = new PriorityLine<>();
        try
        {
            File textFile = new File(pgmFile);
            Scanner scnr = new Scanner(textFile);

            while(scnr.hasNext())
            {
                Transaction transaction = new Transaction(scnr.next(), scnr.next(), scnr.nextInt(), scnr.nextInt());
                
                priorityLine.enqueue(transaction);
            }

            scnr.close();
        } 
        catch (FileNotFoundException e) 
        {
            throw new RuntimeException("File: " + pgmFile + " not found");
        }

        return priorityLine;
    }

    /**
     * Verifies if the given transaction when hashed with all the hashes in the proof equals the rootHash.
     * @param t The transaction that we want to verify it's contained in a certain block.
     * @param proof The list of hashes extracted with the method extractProof.
     * @param blockRootHash The root hash code stored in the respective block.
     * @return Returns true if the transaction is verified, false otherwise.
     */
    public static boolean verifyTransaction(Transaction t, SinglyLinkedList<String> proof, String blockRootHash)
    {
        if(t == null || proof == null || blockRootHash == null)
            throw new RuntimeException();
        
        String transactionHash = cryptographicHashFunction(t.toString());

        for(String hash : proof)
        {
            transactionHash = cryptographicHashFunction(transactionHash, hash);
        }

        if(transactionHash.equals(blockRootHash))
            return true;
        else
            return false;
    }

    /**
     * SHA-256 cryptographic hash function for a single input.
     * @param input String to hash.
     * @return Returns the hashed string.
     */
    public static String cryptographicHashFunction(String input)
    {
        StringBuilder hexString = null;

        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++)
            {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }

        return hexString.toString();
    }

    /**
     * SHA-256 cryptographic hash function for a pair of inputs.
     * It uses the XOR bitwise operator to merge the two hash codes.
     * @param input1 String to hash
     * @param input2 String to hash
     * @return Returns the hashed string
     */
    public static String cryptographicHashFunction(String input1, String input2)
    {
        StringBuilder hexString = null;

        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash1 = digest.digest(input1.getBytes(StandardCharsets.UTF_8));
            byte[] encodedhash2 = digest.digest(input2.getBytes(StandardCharsets.UTF_8));
            hexString = new StringBuilder(2 * encodedhash1.length);
            for (int i = 0; i < encodedhash1.length; i++)
            {
                String hex = Integer.toHexString(0xff & (encodedhash1[i] ^ encodedhash2[i]) );
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }

        return hexString.toString();
    }

}

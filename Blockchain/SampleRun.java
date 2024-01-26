/**
 * Tester class.
 */
public class SampleRun
{
    /**
     * Main method.
     * Usage: java SampleRun filename cumulative fee threshold.
     * @param args Takes in two arguments.
     */
    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.err.println("Usage: java P3 <filename> <cumulative fee threshold>");
            return;
        }

        PriorityLine<Transaction> pq = Utilities.loadTransactions(args[0]);

        System.out.println("\nContents of the priority queue:");
        for(Transaction t : pq)
            System.out.println(t);

        Blockchain chain = new Blockchain(pq, Integer.parseInt(args[1]));

        int count = 1;
        for(Block b : chain)
        {
            System.out.println(String.format("\nBlock %d:", count++));
            for (Transaction t : b)
                System.out.println(t);

            
            System.out.println("\nMerkle Tree:");
            MerkleTree mt = new MerkleTree(b);
            System.out.println(String.format("\nHeight: %d\nInnerNodes: %d\nRoot Hash Code: %s", mt.height(), mt.innerNodes(), b.getRootHash()));

            
            SinglyLinkedList<String> walk = mt.depthFirstTraversal(Order.INORDER);

            System.out.println("\nIn-order traversal of Merkle tree");
            for(String s : walk)
                System.out.println(s);

            Transaction lookupExisting = new Transaction("sender8", "receiver8", 12305, 4);
            Transaction lookupNonExisting = new Transaction("sender8", "receiver8", 12305, 5);

            System.out.println("\nExisting transaction for lookup: " + lookupExisting);
            System.out.println("\nNon-existing transaction for lookup: " + lookupNonExisting);
            
            SinglyLinkedList<String> proof = mt.extractProof(lookupExisting);
            System.out.println("\nExtracted proof of the existing transaction:");
            for(String s : proof)
                System.out.println(s);
            System.out.println("\nVerification of the existing transaction: "+Utilities.verifyTransaction(lookupExisting, proof, b.getRootHash()));
            System.out.println("\nVerification of the non-existing transaction: "+Utilities.verifyTransaction(lookupNonExisting, proof, b.getRootHash()));
        }

    }
}

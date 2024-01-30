import java.util.Iterator;

/**
 * Represents the Merkle Tree of a single Block.
 */
public class MerkleTree
{
    /**
     * Root of the tree.
     */
    private Node root;
    public Node getRoot()
    {
        return root;
    }
    public Node getLeftChild(Node node)
    {
        return node.left;
    }
    public Node getRightChild(Node node)
    {
        return node.right;
    }
    /**
     * Number of leaves in the perfect tree, minimum is 2.
     */
    private int numOfLeaves = 2;
    /**
     * Height of the tree.
     */
    private int height = 0;
    /**
     * List for the traversal of the tree.
     */
    private SinglyLinkedList<String> list;
    /**
     * Queue for doing the level order traversal.
     */
    private SinglyLinkedList<Node> queue;

    /**
     * The constructor first creates the Merkle Tree in memory.
     * After the tree is constructed, the constructor sends the hash of the root to the block object by invoking the block.setRootHash() method.
     * @param block The Block that the Merkle Tree will be created for.
     */
    public MerkleTree(Block block) 
    {
        // Calculates how many leaf nodes we need to make a perfect tree.
        while(numOfLeaves < block.numOfTransactions())
            numOfLeaves *= 2;
        // Calculates the height of the tree.
        height = (int) (Math.log(numOfLeaves)/Math.log(2));
        
        // Uses the recursive helper method to make the tree and assign the last parent node to the root.
        root = genTree(block.iterator(), numOfLeaves);
        // Set the rootHash for the block.
        block.setRootHash(root.hash);
    }

    /**
     * Recursive helper method for generating the merkle tree.
     * @param transactions Takes in an iterator of the transactions.
     * @param numLeaves Provide it the number of leaves that would form the perfect tree.
     * @return Returns a Node that we assign in the method recursivly.
     */
    private Node genTree(Iterator<Transaction> transactions, int numLeaves) 
    {
        // We first check if the we reached a leaf node.
        if (numLeaves == 1)
        {
            // If there is no transactions left we make a dummy node.
            if (!transactions.hasNext())
                return new Node(Utilities.cryptographicHashFunction("DUMMY"));
            // Else we make a node based on the iterator.next.
            return new Node(Utilities.cryptographicHashFunction(transactions.next().toString()));
        }

        // We assign the left nodes of the tree.
        Node left = genTree(transactions, numLeaves / 2);
        // We assign the right node of the tree.
        Node right = genTree(transactions, numLeaves / 2);

        // The parent node is the combination of left and right.
        Node parent = new Node(Utilities.cryptographicHashFunction(left.hash, right.hash));
        parent.left = left;
        parent.right = right;

        // We make the parent node.
        return parent;
    }

    /**
     * The height of the tree.
     * @return Returns the height of the tree.
     */
    public int height()
    {
        return height;
    }

    /**
     * The number of inner nodes in the tree.
     * @return Returns the number of inner nodes.
     */
    public int innerNodes()
    {
        return numOfLeaves-1;
    }

    /**
     * Traverses the tree in level order.
     * @return Returns a list of the hash codes contained in the tree by walking the tree in a level-order.
     */
    public SinglyLinkedList<String> breadthFirstTraversal()
    {
        // List that stores the hashs.
        list = new SinglyLinkedList<>();
        // Queue that we use to traverse in level order.
        queue = new SinglyLinkedList<>();
        queue.add(root);
        // Recursive helper method.
        levelOrder(null);
        return list;
    }
    
    /**
     * Traverses the tree in either post, pre, or in order.
     * @param order Is an enumeration representing the three possible depth-first traversals.
     * @return Returns a list of the hash codes contained in the tree by walking the tree in a certain order.
     */
    public SinglyLinkedList<String> depthFirstTraversal(Order order)
    {
        // List that stores the hashes.
        list = new SinglyLinkedList<>();
        // Depending on the parameter which order to use.
        switch(order)
        {
            case INORDER:
                inOrder(root);
                break;
            case POSTORDER:
                postOrder(root);
                break;
            case PREORDER:
                preOrder(root);
                break;
        }
        return list;
    }

    /**
     * List of the hash codes that are required to prove that a transaction is contained in the block that this Merkle Tree encodes.
     * The head of the list is the deepest hash code and the tail of the list is the top-most hash code required for the proof.
     * The root hash code must NOT be added to this list because it's already stored inside each Block.
     * @param t Transaction to prove.
     * @return Returns the list of hashs need for the proof.
     */
    public SinglyLinkedList<String> extractProof(Transaction t)
    {
        // List that contains the proofs.
        list = new SinglyLinkedList<>();
        // Recursive helper method.
        proof(root, Utilities.cryptographicHashFunction(t.toString()));
        return list;
    }


    /**
     * Node in the tree.
     */
    private class Node implements Comparable<Node>
    {
        /**
         * Value of the node, in this case its the hash.
         */
        String hash;
        /**
         * Left child.
         */
        Node left;
        /**
         * Right child.
         */
        Node right;

        /**
         * Constructor for the node.
         * @param h Value to assign to the node.
         */
        public Node(String h)
        {
            hash = h;
        }
        /**
         * Compares nodes.
         */
        @Override
        public int compareTo(Node o) {
            return hash.compareTo(o.hash);
        }
    }

    /**
     * A field for the helper method that determines if we were on the left side of the tree.
     */
    private boolean proofLeft;
    /**
     * Recursive helper method for extracting proof.
     * @param root Takes in the root node.
     * @param hash Takes in the hash that we are proving.
     */
    private void proof(Node root, String hash)
    {
        // If we reached the end of the tree.
        if(root.left == null || root.right == null)
            return;
        // We go all the way o the left and say we are on the left side of the tree.
        proof(root.left, hash);
        proofLeft = true;
        // If we already found the transaction to prove.
        if(list.size() > 0)
        {
            // And we were on the left side, we add the right side, Else we provide the left side.
            if(proofLeft)
            {
                list.add(root.right.hash);
            }
            else
            {
                list.add(root.left.hash);
            }
            return;
        }
        // We go to each right node and say we are on the right side.
        proof(root.right, hash);
        proofLeft = false;
        // If we already found the transaction to prove.
        if(list.size() > 0)
        {
            // And we were on the left side, we add the right side, Else we provide the left side.
            if(proofLeft)
            {
                list.add(root.right.hash);
            }
            else
            {
                list.add(root.left.hash);
            }
            return;
        }
        // Else we just found the transaction on the left side, we provide the right side.
        else if(root.left.hash.equals(hash))
        {
            list.add(root.right.hash);
            return;
        }
        // Else we just found the transaction on the right side, we provide the left side.
        else if(root.right.hash.equals(hash))
        {
            list.add(root.left.hash);
            return;
        }
    }

    /**
     * Recursive helper method for levelOrder.
     * @param current Current node we are on in the iteration.
     */
    private void levelOrder(Node current)
    {
        // If we reached the end of the tree.
        if(queue.isEmpty())
        {
            return;
        }

        // we remove what was in the queue and add it to the list.
        current = queue.remove(0);
        list.add(current.hash);;

        // If the left side isn't null we add to the queue the left side.
        if(current.left != null)
            queue.add(current.left);
        // If the right side isn't null we add to the queue the right side.
        if(current.right != null)
            queue.add(current.right);
        // We recall the method to repeat all the till the end of the tree.
        levelOrder(null);
    }

    /**
     * Recursive helper method for preOrder.
     * @param root Root of the tree
     */
    private void preOrder(Node root)
    {
        if(root == null)
            return;
        list.add(root.hash);
        preOrder(root.left);
        preOrder(root.right);
    }

    /**
     * Recursive helper method for inOrder.
     * @param root Root of the tree
     */
    private void inOrder(Node root)
    {
        if(root == null)
            return;
        
        inOrder(root.left);
        list.add(root.hash);
        inOrder(root.right);
    }

    /**
     * Recursive helper method for postOrder.
     * @param root Root of the tree
     */
    private void postOrder(Node root)
    {
        if(root == null)
            return;
        
        postOrder(root.left);
        postOrder(root.right);
        list.add(root.hash);
    }
}

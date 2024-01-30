import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.Queue;

public class UI extends JFrame 
{
    private PriorityLine<Transaction> transactions;
    private Blockchain chain;
    Transaction transaction;
    SinglyLinkedList<String> proof;
    ArrayList<MerkleTree> merkleTrees = new ArrayList<>();
    int threshold = 20;

    public UI() {
        // Set up the main frame
        setTitle("Blockchain UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem loadFileMenuItem = new JMenuItem("Load Transactions");
        fileMenu.add(loadFileMenuItem);

        // Settings menu
        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);
        
        JMenuItem changeThresholdMenuItem = new JMenuItem("Change Threshold");
        settingsMenu.add(changeThresholdMenuItem);

        // Create the blockchain panel
        BlockchainPanel blockchainPanel = new BlockchainPanel();
        JScrollPane blockchainScrollPane = new JScrollPane(blockchainPanel);

        // Create the Proof Transactions Panel
        JPanel proofTransactionsPanel = new JPanel();
        proofTransactionsPanel.setBorder(BorderFactory.createTitledBorder("Prove Transactions"));
        proofTransactionsPanel.setLayout(new BorderLayout());

        // Create a panel for the top input boxes and button
        JPanel topPanel = new JPanel(new GridLayout(0, 2));
        JTextField inputBox1 = new JTextField();
        JTextField inputBox2 = new JTextField();
        JTextField inputBox3 = new JTextField();
        JTextField inputBox4 = new JTextField();
        JTextField inputBox5 = new JTextField();
        JButton extractProofButton = new JButton("Extract Proof");
        topPanel.add(new Label("Sender:"));
        topPanel.add(inputBox1);
        topPanel.add(new Label("Reciever:"));
        topPanel.add(inputBox2);
        topPanel.add(new Label("Amount:"));
        topPanel.add(inputBox3);
        topPanel.add(new Label("Fee:"));
        topPanel.add(inputBox4);
        topPanel.add(new Label("Merkle Tree Number"));
        topPanel.add(inputBox5);
        topPanel.add(extractProofButton);

        // Create a panel for the bottom input box and button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextField bottomInputBox = new JTextField();
        JButton verifyProofButton = new JButton("Verify");
        bottomPanel.add(new JLabel("Prove Transaction Exists In Block Number: "), BorderLayout.WEST);
        bottomPanel.add(bottomInputBox, BorderLayout.CENTER);
        bottomPanel.add(verifyProofButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(0, 1));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Proof"));
        JScrollPane centerScrollPane = new JScrollPane(centerPanel);
        

        // Add the top and bottom panels to the proofTransactionsPanel
        proofTransactionsPanel.add(topPanel, BorderLayout.NORTH);
        proofTransactionsPanel.add(bottomPanel, BorderLayout.SOUTH);
        proofTransactionsPanel.add(centerScrollPane, BorderLayout.CENTER);
        JScrollPane proofTransactionsScrollPane = new JScrollPane(proofTransactionsPanel);

        // Create Split panel form proof and blockchain panel
        JSplitPane subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blockchainScrollPane, proofTransactionsScrollPane);
        subSplitPane.setResizeWeight(0.8); // 50% for each panel

        // Create the Merkle tree panel
        MerkleTreePanel merkleTreePanel = new MerkleTreePanel();
        JScrollPane merkleTreeScrollPane = new JScrollPane(merkleTreePanel);

         // Create a split pane for the blockchain and Merkle tree
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, merkleTreeScrollPane);
        splitPane.setResizeWeight(0.4); // 50% for each panel

        // Set up the layout
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        // Add action listeners for menu items (you can implement functionality)
        loadFileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    transactions = Utilities.loadTransactions(selectedFile.getAbsolutePath());
                    chain = new Blockchain(transactions, threshold);
                    blockchainPanel.removeAll(); 
                    merkleTreePanel.removeAll();

                    int i=0;
                    for(Block b : chain)
                    {
                        BlockPanel block = new BlockPanel(b, i);
                        blockchainPanel.addBlock(block, i);

                        MerkleTreeComponent merkleTree = new MerkleTreeComponent(b, i);
                        merkleTreePanel.addMerkleTree(merkleTree, i);
                        merkleTrees.add(new MerkleTree(b));
                        i++;
                    }
                    blockchainPanel.revalidate();
                    blockchainPanel.repaint();
                    merkleTreePanel.revalidate();
                    merkleTreePanel.repaint();
                }
            }
        });

        changeThresholdMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String input = JOptionPane.showInputDialog("Current threshold: " + threshold + "\nEnter the new threshold value:");
                try 
                {
                    threshold = Integer.parseInt(input);        
                    JOptionPane.showMessageDialog(null, "New threshold set to: " + threshold, "Settings Changed", JOptionPane.INFORMATION_MESSAGE);
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer threshold value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        extractProofButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {                
                try
                {
                    String sender = inputBox1.getText();
                    String reciever = inputBox2.getText();
                    int amount = Integer.parseInt(inputBox3.getText());
                    int fee = Integer.parseInt(inputBox4.getText());
                    int mtNumber = Integer.parseInt(inputBox5.getText());
                    transaction = new Transaction(sender, reciever, amount, fee);
                    MerkleTree mt = merkleTrees.get(mtNumber);
                    proof = mt.extractProof(transaction);
                    centerPanel.removeAll();
                    for(String s : proof)
                    {
                        centerPanel.add(new JLabel(s));
                    }
                    centerPanel.revalidate();
                    centerPanel.repaint();
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Invalid inputs. Please enter valid and existing values.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        verifyProofButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String brh = "";
                int i = 0;
                try 
                {
                    i = Integer.parseInt(bottomInputBox.getText());
                    int j = 0;
                    for(Block b : chain)
                    {
                        if(i == j)
                            brh = b.getRootHash();
                        j++;
                    }
                    if(Utilities.verifyTransaction(transaction, proof, brh))
                    {
                        JOptionPane.showMessageDialog(null, "Transaction: " + transaction + "\nFound in block: " + i, "Verified", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Transaction: " + transaction + "\nNot found in block: " + i, "Not Verified", JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer block value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }

    private class MerkleTreePanel extends JPanel {

        public MerkleTreePanel() {
            setBorder(BorderFactory.createTitledBorder("Merkle Trees of Blocks"));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));  // Arrange components from left to right
        }
        public void addMerkleTree(MerkleTreeComponent merkleTreeComponent, int yPos) {
            /*
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = yPos;
            gbc.weightx = 1.0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            */
            add(merkleTreeComponent);
        }
    }
    private class MerkleTreeComponent extends JPanel {
        public MerkleTreeComponent(Block block, int blockNum) {
            setBorder(BorderFactory.createTitledBorder("Merkle Block Number: " + blockNum));
            setLayout(new GridLayout(0, 1));

            // Create a JTree to represent the Merkle tree
            MerkleTree tree = new MerkleTree(block);
            SinglyLinkedList<String> linkedList = tree.breadthFirstTraversal();
            Iterator<String> linkedListIterator = linkedList.iterator();
            
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(linkedListIterator.next());
            DefaultMutableTreeNode leftNode;
            DefaultMutableTreeNode rightNode;
            DefaultMutableTreeNode currentNode = rootNode;
            Queue<DefaultMutableTreeNode> queue = new LinkedList<>();
            while(linkedListIterator.hasNext())
            {
                leftNode = new DefaultMutableTreeNode(linkedListIterator.next());
                rightNode = new DefaultMutableTreeNode(linkedListIterator.next());
                currentNode.add(leftNode);
                currentNode.add(rightNode);
                queue.add(leftNode);
                queue.add(rightNode);
                currentNode = queue.remove();
            }


            JTree merkleTree = new JTree(rootNode);
            merkleTree.setAlignmentX(0.0f);
            merkleTree.setAlignmentY(0.0f);
            add(merkleTree);
        }
    }


    private class BlockchainPanel extends JPanel {
        public BlockchainPanel() {
            setBorder(BorderFactory.createTitledBorder("BlockChain of Transactions"));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }
        
        public void addBlock(BlockPanel blockPanel, int yPos) {
            /*
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = yPos;
            gbc.weightx = 1.0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            */
            add(blockPanel);
        }
    }
    private class BlockPanel extends JPanel 
    {
        public BlockPanel(Block block, int blockNum) 
        {
            setBorder(BorderFactory.createTitledBorder("Block Number: " + blockNum));
            setLayout(new GridLayout(0, 1));
            for (Transaction t : block) {
                JLabel label = new JLabel(t.toString());
                label.setAlignmentX(0.0f);
                label.setAlignmentY(0.0f);
                add(label);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UI ui = new UI();
                ui.setVisible(true);
            }
        });
    }
}

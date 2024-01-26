# Simplified Blockchain

This project serves as a simplified example of a blockchain system, focusing on the fundamental concepts of transaction processing, block creation, and the use of Merkle trees for efficient transaction tracking. In this model, incoming transactions are added to a queue, creating a sequential order for processing. Subsequently, blocks are formed by bundling a set of transactions from the queue, and these blocks are added to the blockchain.

One notable feature is the utilization of Merkle trees to maintain a secure and compact representation of transactions within each block. Merkle trees enable efficient verification of the integrity of the transactions by creating a hierarchical structure where each leaf node represents an individual transaction hash. The root of the Merkle tree, known as the Merkle root, is included in the block header, providing a concise and tamper-evident summary of all transactions within the block.

This simplified blockchain prototype aims to illustrate the core principles of decentralized ledger technology, showcasing how transactions are organized, verified, and added to the blockchain using a systematic approach. The use of Merkle trees enhances the security and efficiency of the system, ensuring a reliable and transparent record of transactions within the blockchain.

![Screenshot 2024-01-25 194938](https://github.com/AbdallahAE/Simplified-Blockchain/assets/106286861/749fbcb8-1651-4731-b24b-1c450bdfaa33)

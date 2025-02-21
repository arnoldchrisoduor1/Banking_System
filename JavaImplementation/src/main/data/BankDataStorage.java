package main.data;

import main.model.Account;
import java.util.*;

public class BankDataStorage {
    // Primary storage using a custom HashMap implementation
    private CustomHashMap<String, Account> accountMap;
    
    // Index for quick balance-based queries using AVL Tree
    private AVLTree<Double, List<String>> balanceIndex;
    
    public BankDataStorage() {
        this.accountMap = new CustomHashMap<>();
        this.balanceIndex = new AVLTree<>();
    }

    // Custom HashMap implementation
    private static class CustomHashMap<K, V> {
        private static final int INITIAL_CAPACITY = 16;
        private static final double LOAD_FACTOR = 0.75;
        
        private Entry<K, V>[] buckets;
        private int size;
        
        @SuppressWarnings("unchecked")
        public CustomHashMap() {
            buckets = new Entry[INITIAL_CAPACITY];
            size = 0;
        }
        
        private static class Entry<K, V> {
            K key;
            V value;
            Entry<K, V> next;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        
        public void put(K key, V value) {
            if ((double) size / buckets.length >= LOAD_FACTOR) {
                resize();
            }
            
            int index = Math.abs(key.hashCode() % buckets.length);
            Entry<K, V> entry = buckets[index];
            
            while (entry != null) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                    return;
                }
                entry = entry.next;
            }
            
            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = buckets[index];
            buckets[index] = newEntry;
            size++;
        }
        
        public V get(K key) {
            int index = Math.abs(key.hashCode() % buckets.length);
            Entry<K, V> entry = buckets[index];
            
            while (entry != null) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
                entry = entry.next;
            }
            return null;
        }
        
        @SuppressWarnings("unchecked")
        private void resize() {
            Entry<K, V>[] oldBuckets = buckets;
            buckets = new Entry[oldBuckets.length * 2];
            size = 0;
            
            for (Entry<K, V> bucket : oldBuckets) {
                Entry<K, V> entry = bucket;
                while (entry != null) {
                    put(entry.key, entry.value);
                    entry = entry.next;
                }
            }
        }
    }
    
    // AVL Tree implementation for balance-based indexing
    private static class AVLTree<K extends Comparable<K>, V> {
        private Node root;
        
        private class Node {
            K key;
            V value;
            Node left, right;
            int height;
            
            Node(K key, V value) {
                this.key = key;
                this.value = value;
                this.height = 1;
            }
        }
        
        public void insert(K key, V value) {
            root = insert(root, key, value);
        }
        
        private Node insert(Node node, K key, V value) {
            if (node == null) {
                return new Node(key, value);
            }
            
            int compare = key.compareTo(node.key);
            if (compare < 0) {
                node.left = insert(node.left, key, value);
            } else if (compare > 0) {
                node.right = insert(node.right, key, value);
            } else {
                node.value = value;
                return node;
            }
            
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
            
            int balance = getBalance(node);
            
            // Left Heavy
            if (balance > 1) {
                if (key.compareTo(node.left.key) < 0) {
                    return rightRotate(node);
                } else {
                    node.left = leftRotate(node.left);
                    return rightRotate(node);
                }
            }
            
            // Right Heavy
            if (balance < -1) {
                if (key.compareTo(node.right.key) > 0) {
                    return leftRotate(node);
                } else {
                    node.right = rightRotate(node.right);
                    return leftRotate(node);
                }
            }
            
            return node;
        }
        
        private Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            
            x.right = y;
            y.left = T2;
            
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            
            return x;
        }
        
        private Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            
            y.left = x;
            x.right = T2;
            
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
            
            return y;
        }
        
        private int getHeight(Node node) {
            return node == null ? 0 : node.height;
        }
        
        private int getBalance(Node node) {
            return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
        }
        
        public V search(K key) {
            Node node = root;
            while (node != null) {
                int compare = key.compareTo(node.key);
                if (compare == 0) {
                    return node.value;
                } else if (compare < 0) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return null;
        }
    }
    
    // Public methods for the banking system
    public void saveAccount(Account account) {
        accountMap.put(account.getAccountNumber(), account);
        
        // Update balance index
        List<String> accountNumbers = balanceIndex.search(account.getBalance());
        if (accountNumbers == null) {
            accountNumbers = new ArrayList<>();
        }
        accountNumbers.add(account.getAccountNumber());
        balanceIndex.insert(account.getBalance(), accountNumbers);
    }
    
    public Account getAccount(String accountNumber) {
        return accountMap.get(accountNumber);
    }
    
    public List<Account> getAccountsByBalance(double balance) {
        List<String> accountNumbers = balanceIndex.search(balance);
        if (accountNumbers == null) {
            return new ArrayList<>();
        }
        
        List<Account> accounts = new ArrayList<>();
        for (String accNumber : accountNumbers) {
            Account account = accountMap.get(accNumber);
            if (account != null) {
                accounts.add(account);
            }
        }
        return accounts;
    }
}
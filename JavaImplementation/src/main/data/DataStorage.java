import main.model.Account;
import java.util.*;

public class DataStorage {
    // Primary storage using a custom hashmap implementation.
    private CustomHashMap <String, Account> accountMap;

    // Index for quick balance-based queries using AVL trees.
    private AVLTree<Double, List<String>> balanceIndex;

    public DataStorage() {
        this.accountMap = new CustomHashMap();
        this.balanceIndex = new AVLTree();
    }

    // Custom Hashmap implementation.
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
            Entry <K, V> next;

            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        public void put(K key, V value) {
            if((double) size / buckets.length >= LOAD_FACTOR) {
                resize();
            }

            int index = Math.abs(key.hashCode() % buckets.length);
            Entry<K, V> entry = buckets[index];

            while (entry != null) {
                if (entry.key.equal(key)) {
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
}

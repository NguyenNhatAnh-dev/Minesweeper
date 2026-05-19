package dsa;

public class HashTable<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Object[] buckets;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR = 0.75;

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.buckets = new Object[capacity];
        this.size = 0;
    }

    private int hash(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode) % capacity;
    }

    @SuppressWarnings("unchecked")
    public void put(K key, V value) {
        if ((double) size / capacity >= LOAD_FACTOR) {
            rehash();
        }
        int index = hash(key);
        Entry<K, V> head = (Entry<K, V>) buckets[index];
        Entry<K, V> current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        buckets[index] = newEntry;
        size++;
    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = (Entry<K, V>) buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public boolean containsKey(K key) {
        int index = hash(key);
        Entry<K, V> current = (Entry<K, V>) buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> current = (Entry<K, V>) buckets[index];
        Entry<K, V> prev = null;
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        int newCapacity = capacity * 2;
        Object[] newBuckets = new Object[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = (Entry<K, V>) buckets[i];
            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex = Math.abs(current.key.hashCode()) % newCapacity;
                current.next = (Entry<K, V>) newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
        }
        buckets = newBuckets;
        capacity = newCapacity;
    }
}

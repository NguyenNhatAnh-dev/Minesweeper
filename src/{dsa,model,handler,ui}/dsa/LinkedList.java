package dsa;

public class LinkedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void insertFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void insertLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public T deleteFirst() {
        if (isEmpty()) {
            return null;
        }
        T data = head.data;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return data;
    }

    public T deleteLast() {
        if (isEmpty()) {
            return null;
        }
        T data = tail.data;
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] result = new Object[size];
        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            result[i] = current.data;
            current = current.next;
        }
        return (T[]) result;
    }

    public void insertionSort(java.util.Comparator<T> comparator) {
        if (size <= 1) {
            return;
        }
        Node<T> current = head.next;
        while (current != null) {
            T key = current.data;
            Node<T> prev = current.prev;
            while (prev != null && comparator.compare(prev.data, key) > 0) {
                prev.next.data = prev.data;
                prev = prev.prev;
            }
            if (prev == null) {
                head.data = key;
            } else {
                prev.next.data = key;
            }
            current = current.next;
        }
    }
}

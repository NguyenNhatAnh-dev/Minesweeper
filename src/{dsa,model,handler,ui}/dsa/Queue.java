package dsa;

public class Queue<T> {

    private Object[] array;
    private int front;
    private int rear;
    private int count;
    private int capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    public void enqueue(T item) {
        if (isFull()) {
            resize();
        }
        rear = (rear + 1) % capacity;
        array[rear] = item;
        count++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T item = (T) array[front];
        array[front] = null;
        front = (front + 1) % capacity;
        count--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peekFront() {
        if (isEmpty()) {
            return null;
        }
        return (T) array[front];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == capacity;
    }

    public int size() {
        return count;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            array[i] = null;
        }
        front = 0;
        rear = -1;
        count = 0;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < count; i++) {
            newArray[i] = array[(front + i) % capacity];
        }
        array = newArray;
        front = 0;
        rear = count - 1;
        capacity = newCapacity;
    }
}

package dsa;

public class Stack<T> {

    private Object[] array;
    private int top;
    private int capacity;

    public Stack(int capacity) {
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.top = -1;
    }

    public void push(T item) {
        if (isFull()) {
            resize();
        }
        array[++top] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T item = (T) array[top];
        array[top] = null;
        top--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) array[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == capacity - 1;
    }

    public int size() {
        return top + 1;
    }

    public void clear() {
        for (int i = 0; i <= top; i++) {
            array[i] = null;
        }
        top = -1;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < capacity; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
        capacity = newCapacity;
    }
}

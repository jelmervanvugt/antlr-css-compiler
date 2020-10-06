package nl.han.ica.datastructures;

public class HANQueue<T> implements IHANQueue<T> {

    private HANLinkedList<T> queue;

    public HANQueue(HANLinkedList<T> queue) {
        this.queue = queue;
    }

    public HANQueue() {
        this.queue = new HANLinkedList<>();
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void enqueue(T value) {
        queue.addLast(value);
    }

    @Override
    public T dequeue() {
        T value = queue.getFirst();
        queue.removeFirst();
        return value;
    }

    @Override
    public T peek() {
        return queue.getFirst();
    }

    @Override
    public int getSize() {
        return queue.getSize();
    }
}

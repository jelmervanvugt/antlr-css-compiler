package nl.han.ica.datastructures;

import nl.han.ica.Exceptions.StackIsEmptyException;

public class HANStack<T> implements IHANStack<T> {

    private HANLinkedList<T> stack;

    public HANStack(HANLinkedList<T> stack) {
        this.stack = stack;
    }

    public HANStack() {
        stack = new HANLinkedList<T>();
    }

    @Override
    public void push(T value) {
        stack.addFirst(value);
    }

    @Override
    public T pop() {
        if (stack.isEmpty()) throw new StackIsEmptyException("Stack is empty");
        T value = stack.getFirst();
        stack.removeFirst();
        return value;
    }

    @Override
    public T peek() {
        if (stack.isEmpty()) throw new StackIsEmptyException("Stack is empty");
        return stack.getFirst();
    }
}

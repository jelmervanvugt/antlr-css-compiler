package nl.han.ica.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;

    private int size = 0;



    @Override
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
            tail = node;
            return;
        }
        tail.setNext(node);
        tail = node;
        size++;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public void insert(int pos, T value) {
        OwnGenericLinkedListIterator iterator = new OwnGenericLinkedListIterator();
        int index = 0;

        Node<T> nToAdd= new Node<>(value);

        Node<T> nBefore = null;
        Node<T> nAfter = null;

        while (index < pos) {
            if (iterator.hasNext()) {
                iterator.next();
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }

            if (index == pos - 1) {
                nBefore = iterator.current;
            } else if (index == pos) {
                nAfter = iterator.current;
            }
            index++;
        }

        if(nBefore == null) throw new ArrayIndexOutOfBoundsException();

        nBefore.setNext(nToAdd);
        nToAdd.setNext(nAfter);
    }

    @Override
    public void delete(int pos) {
        OwnGenericLinkedListIterator iterator = new OwnGenericLinkedListIterator();
        int index = 0;

        Node<T> nBefore = null;
        Node<T> nToRemove = null;
        Node<T> nAfter = null;

        while (index < pos + 1) {
            if (iterator.hasNext()) {
                iterator.next();
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }

            if (index == pos - 1) {
                nBefore = iterator.current;
            } else if (index == pos) {
                nToRemove = iterator.current;
            } else if (index == pos + 1) {
                nAfter = iterator.current;
            }
            index++;
        }

        if(nToRemove == null || nBefore == null) {
            throw new ArrayIndexOutOfBoundsException();
        }

        nToRemove.setNext(null);
        nBefore.setNext(nAfter);

    }

    @Override
    public T get(int pos) {
        OwnGenericLinkedListIterator iterator = new OwnGenericLinkedListIterator();
        T value = null;
        int i = 0;
        while (i < pos) {
            if (iterator.hasNext()) {
                value = iterator.next();
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
            i++;
        }
        return value;
    }

    @Override
    public void removeFirst() {
        if (head != null && head.getNext() != null) {
            head = head.getNext();
            size--;
        }
    }


    @Override
    public T getFirst() {
        return head.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new OwnGenericLinkedListIterator();
    }


    class OwnGenericLinkedListIterator implements Iterator<T> {

        Node<T> current = head;

        @Override
        public boolean hasNext() {
            if (current == null && head != null) {
                return true;
            } else if (current != null) {
                return current.getNext() != null;
            }
            return false;
        }

        @Override
        public T next() {
            if (current == null && head != null) {
                current = head;
                return head.getValue();
            } else if (current != null) {
                current = current.getNext();
                return current.getValue();
            }
            throw new NoSuchElementException();
        }
    }

}

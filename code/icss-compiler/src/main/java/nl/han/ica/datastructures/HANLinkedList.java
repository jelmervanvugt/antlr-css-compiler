package nl.han.ica.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;

    private int size = 0;

    @Override
    public void addFirst(T value) {
        add(value, true);
    }

    public void addLast(T value) {
        add(value, false);
    }

    private void add(T value, boolean first) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            if (first) {
                node.setNext(head);
                head = node;
            } else {
                tail.setNext(node);
                tail = node;
            }
        }
        size++;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void insert(int pos, T value) {
        if (pos == 0) {
            addFirst(value);
            return;
        } else if (pos == size - 1) {
            addLast(value);
            return;
        }

        OwnGenericLinkedListIterator iterator = new OwnGenericLinkedListIterator();
        int index = 0;

        Node<T> nToAdd = new Node<>(value);

        Node<T> nBefore = null;
        Node<T> nAfter = null;

        while (index <= pos) {

            if (index == pos - 1) {
                nBefore = iterator.current;
            } else if (index == pos) {
                nAfter = iterator.current;
            }

            if (iterator.hasNext()) {
                iterator.next();
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }

            index++;
        }

        if (nBefore == null) throw new ArrayIndexOutOfBoundsException();

        nBefore.setNext(nToAdd);
        nToAdd.setNext(nAfter);
        size++;
    }

    @Override
    public void delete(int pos) {
        if (pos == 0) {
            removeFirst();
            return;
        }

        OwnGenericLinkedListIterator iterator = new OwnGenericLinkedListIterator();
        int index = 0;

        Node<T> nBefore = iterator.current;
        Node<T> nToRemove = null;
        Node<T> nAfter = null;

        while (index < pos) {
            index++;
            if (iterator.hasNext()) iterator.next();
            else {
                throw new ArrayIndexOutOfBoundsException();
            }

            if (index == pos - 1) {
                nBefore = iterator.current;
            } else if (index == pos) {
                nToRemove = iterator.current;
            }
        }

        if (iterator.hasNext()) nAfter = iterator.current.getNext();

        if (nBefore == null || nToRemove == null) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (nAfter == null) {
            tail = nBefore;
            tail.setNext(null);
        } else {
            nToRemove.setNext(null);
            nBefore.setNext(nAfter);
        }
        size--;
    }

    @Override
    public T get(int pos) {
        OwnGenericLinkedListIterator iterator = new OwnGenericLinkedListIterator();
        T value = head.getValue();
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
        } else if (head != null && head.getNext() == null) {
            head = null;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
        size--;
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
            if (current == null) {
                return false;
            } else {
                return current.getNext() != null;
            }
        }

        @Override
        public T next() {
            if (current == null || current.getNext() == null) {
                throw new NoSuchElementException();
            }
            current = current.getNext();
            return current.getValue();
        }

    }
}

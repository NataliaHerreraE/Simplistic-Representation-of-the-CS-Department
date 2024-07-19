package datastructures;

import java.util.List;

public class MyGenericCircularQueueL<T> {
    protected  MyNode<T> head;
    protected  MyNode<T> tail;
    protected  int size;

    public MyGenericCircularQueueL() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public MyGenericCircularQueueL(List<T> list) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        for (T item : list) {
            enqueue(item);
        }
    }

    //since is a list we dont need itFull method
    public boolean isEmpty() {
        return head == null;
    }

    public void enqueue(T element) {
        MyNode<T> newNode = new MyNode<>(element);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            tail.setNext(head); // Point tail to head to make the queue circular
        } else {
            tail.setNext(newNode); // Link the current tail to the new node
            tail = newNode; // Update the tail to the new node
            tail.setNext(head); // Point the new tail's next to the head
        }
        size++;
    }
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty, cannot dequeue.");
            return null;
        }
        T element = head.getData();
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.getNext(); // Move head to the next element
            tail.setNext(head); // Update tail's next to the new head
        }
        size--;
        return element;
    }

    public int getSize() {
        return size;
    }

    public void displayAllElements() {
        if (isEmpty()) {
            System.out.println("Queue is empty, nothing to display.");
            return;
        }
        MyNode<T> current = head;
        do {
            System.out.println("Element: " + current.getData());
            current = current.getNext();
        } while (current != head);
    }

}
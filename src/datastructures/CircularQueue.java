package datastructures;

/**
 * A generic Circular Queue data structure that extends Comparable.
 * The queue is implemented as an array and uses front and rear pointers to keep track of elements.
 * The queue automatically resizes itself when it becomes full.
 *
 * @param <T> the type of elements held in this queue.
 */
public class CircularQueue<T extends Comparable<T>>{
    protected T[] theArrayCircularQueue;
    protected int front;
    protected int rear;
    protected int size;

    /**
     * Default constructor. Initializes an empty queue with a capacity of 20.
     */
    public CircularQueue(){
        this.size = 0;
        theArrayCircularQueue = (T[]) new Comparable[20];
        front = 0;
        rear = -1;
    }

    /**
     * Constructor with size. Initializes an empty queue with a specified capacity.
     *
     * @param maxSize the initial capacity of the queue.
     */
    public CircularQueue(int maxSize) {
        this.theArrayCircularQueue = (T[]) new Comparable[maxSize];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * Checks if the queue is full.
     *
     * @return true if the queue is full, false otherwise.
     */
    public boolean isFull() {
        return size == theArrayCircularQueue.length;
    }

    /**
     * Adds an item to the rear of the queue.
     * If the queue is full, it will be resized before adding the item.
     *
     * @param item the item to add to the queue.
     */
    public void enqueue(T item) {
        if (isFull()) {
            resize();
        }
        rear = (rear + 1) % theArrayCircularQueue.length; // Actualiza rear de forma circular
        theArrayCircularQueue[rear] = item;
        size++;
    }

    /**
     * Removes an item from the front of the queue.
     *
     * @return the item that was removed.
     * @throws IllegalStateException if the queue is empty.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T item = theArrayCircularQueue[front]; // Obtiene el elemento en el frente
        theArrayCircularQueue[front] = null; // Elimina la referencia al elemento en el frente
        front = (front + 1) % theArrayCircularQueue.length; // Actualiza front de forma circular
        size--;
        return item;
    }

    /**
     * Resizes the queue to twice its current size.
     */
    public void resize() {
        int newSize = size * 2;
        T[] newArray = (T[]) new Object[newSize];
        int i = 0;
        int j = front;
        while (j != rear) {
            newArray[i++] = theArrayCircularQueue[j];
            j = (j + 1) % size;
        }
        newArray[i] = theArrayCircularQueue[rear]; //seize-1
        front = 0;
        rear = i; // size-1
        theArrayCircularQueue = newArray;
        //size = newSize;//

    }

}
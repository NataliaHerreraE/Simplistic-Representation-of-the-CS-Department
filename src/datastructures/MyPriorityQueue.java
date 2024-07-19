package datastructures;

/**
 * A priority queue data structure that extends CircularQueue.
 * The queue is implemented as an array and uses front and rear pointers to keep track of elements.
 * The queue automatically resizes itself when it becomes full.
 * Elements are inserted in the queue based on their priority.
 *
 * @param <T> the type of elements held in this queue.
 */
public class MyPriorityQueue<T extends Comparable<T>> extends CircularQueue<T> {

    /**
     * Default constructor. Initializes an empty queue with a capacity of 20.
     */
    public MyPriorityQueue() {
        super();
    }


    /**
     * Adds an item to the queue based on its priority.
     * If the queue is full, it will be resized before adding the item.
     *
     * @param item the item to add to the queue.
     */
    @Override
    public void enqueue(T item) {
        if (isFull()) {
            resize();
        }
        if (isEmpty()) {
            super.enqueue(item);
        } else {
            int insertIndex = rear;
            while (insertIndex >= front && item.compareTo(theArrayCircularQueue[insertIndex]) > 0) {
                // Cada elemento desde el rear hasta el insertIndex se mueve una posición hacia atrás en el arreglo.
                // Esto se realiza en un bucle que empieza en el rear y se mueve hacia el lugar identificado (insertIndex).
                theArrayCircularQueue[(insertIndex + 1) % theArrayCircularQueue.length] = theArrayCircularQueue[insertIndex];
                insertIndex--;
            }
            theArrayCircularQueue[(insertIndex + 1) % theArrayCircularQueue.length] = item;
            rear = (rear + 1) % theArrayCircularQueue.length;
            size++;
        }
    }

}
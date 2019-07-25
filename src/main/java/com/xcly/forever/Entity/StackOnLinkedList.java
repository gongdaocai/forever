package com.xcly.forever.Entity;

public class StackOnLinkedList<T> {

    private Node<T> top;
    private Integer size;


    public StackOnLinkedList() {
        this.top = null;
        this.size = 0;
    }

    public StackOnLinkedList(T element) {
        Node<T> node = new Node(element, null);
        top = node;
        size++;
    }

    public synchronized void push(T element) {
        Node<T> node = new Node(element, top);
        top = node;
        size++;
    }

    public synchronized T pop() {
        T old = top.getElement();
        top = top.getNext();
        size--;
        return old;
    }

    public synchronized Integer getSize() {
        return this.size;
    }
}


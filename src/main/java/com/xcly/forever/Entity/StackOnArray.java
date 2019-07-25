package com.xcly.forever.Entity;

import java.util.Arrays;

public class StackOnArray<T> {
    private static final Integer DEFAULT_COMPACITY = 3;
    private T[] array;
    private Integer index;

    public StackOnArray() {
        this(DEFAULT_COMPACITY);
    }

    //指定栈大小
    public StackOnArray(Integer size) {
        array = (T[]) new Object[size];
        index = -1;
    }

    //弹栈
    public T pop() {
        if (index <= -1) {
            throw new RuntimeException("NullStackException");
        }
        if (index > array.length - 1) {
            throw new RuntimeException("OutOfBoundException");
        }
        T oldValue = array[index];
        array[index] = null;
        index--;
        return oldValue;
    }

    //压栈
    public void push(T element) {
        if (array == null) {
            throw new RuntimeException("NullStackException");
        }
        //栈容量不够需要扩容
        if (index >= array.length - 1) {
            array = Arrays.copyOf(array, array.length * 2);
        }
        array[index + 1] = element;
        index++;
    }
}

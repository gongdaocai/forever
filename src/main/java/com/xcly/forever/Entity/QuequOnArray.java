package com.xcly.forever.Entity;

import java.util.Arrays;

public class QuequOnArray<T> {

    private static final int DEFAULT_LENGTH = 5;

    private T[] elementData;

    private int font;

    private int behind;


    public QuequOnArray() {
        this(DEFAULT_LENGTH);
    }

    public QuequOnArray(int elementLength) {
        elementData = (T[]) new Object[elementLength];
        font = 0;
        behind = 0;
    }


    //入队

    public void push(T element) {
        //队列是否为空
        if (elementData.length > 0) {
            //是否满队
            if (font == (behind + 1) % elementData.length) {
                //动态扩容
//                0,behind ,elementData.length
                int j = 0;
                if (behind < font) {
                    //两端copy  0,behind  font,length
                    T[] newElement = (T[]) new Object[elementData.length * 2];
                    for (int i = font; i < elementData.length; i++) {
                        if (elementData[i] != null) {
                            newElement[j] = elementData[i];
                            j++;
                        }
                    }
                    for (int i = 0; i < behind; i++) {
                        if (elementData[i] != null) {
                            newElement[j] = elementData[i];
                            j++;
                        }

                    }
                    elementData = newElement;
                } else {
                    j=elementData.length-2;
                    //从0copy到length
                    elementData = Arrays.copyOf(elementData, elementData.length * 2);
                }
                elementData[j] = element;
                font = 0;
                behind = j + 1;
            } else {
                elementData[behind] = element;
                behind = (behind + 1) % elementData.length;
            }

        }
    }

    public T pop() {
        T e = elementData[font];
        elementData[font]=null;
        font = (font + 1) % elementData.length;
        return e;
    }

}

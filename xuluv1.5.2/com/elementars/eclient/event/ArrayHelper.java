// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event;

import java.util.Iterator;

public class ArrayHelper<T> implements Iterable<T>
{
    private T[] elements;
    
    public ArrayHelper(final T[] array) {
        this.elements = array;
    }
    
    public ArrayHelper() {
        this.elements = (T[])new Object[0];
    }
    
    public void add(final T t) {
        if (t != null) {
            final Object[] array = new Object[this.size() + 1];
            for (int i = 0; i < array.length; ++i) {
                if (i < this.size()) {
                    array[i] = this.get(i);
                }
                else {
                    array[i] = t;
                }
            }
            this.set(array);
        }
    }
    
    public boolean contains(final T t) {
        Object[] array;
        for (int lenght = (array = this.array()).length, i = 0; i < lenght; ++i) {
            final T entry = (T)array[i];
            if (entry.equals(t)) {
                return true;
            }
        }
        return false;
    }
    
    public void remove(final T t) {
        if (this.contains(t)) {
            final Object[] array = new Object[this.size() - 1];
            boolean b = true;
            for (int i = 0; i < this.size(); ++i) {
                if (b && this.get(i).equals(t)) {
                    b = false;
                }
                else {
                    array[b ? i : (i - 1)] = this.get(i);
                }
            }
            this.set(array);
        }
    }
    
    public T[] array() {
        return this.elements;
    }
    
    public int size() {
        return this.array().length;
    }
    
    public void set(final T[] array) {
        this.elements = array;
    }
    
    public T get(final int index) {
        return this.array()[index];
    }
    
    public void clear() {
        this.elements = (T[])new Object[0];
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < ArrayHelper.this.size() && ArrayHelper.this.get(this.index) != null;
            }
            
            @Override
            public T next() {
                return ArrayHelper.this.get(this.index++);
            }
            
            @Override
            public void remove() {
                ArrayHelper.this.remove(ArrayHelper.this.get(this.index));
            }
        };
    }
}

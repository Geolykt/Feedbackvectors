package de.geolykt.feedbackvectors.collections;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class ListEnumeration<E> implements Enumeration<E> {

    protected final List<E> list;
    protected int index = 0;

    public ListEnumeration(List<E> list) {
        this.list = list;
    }

    @Override
    public boolean hasMoreElements() {
        return index < list.size();
    }

    @Override
    public E nextElement() {
        return list.get(index++);
    }

    @Override
    public Iterator<E> asIterator() {
        return list.iterator();
    }
}

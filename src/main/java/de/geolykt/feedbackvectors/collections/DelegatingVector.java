package de.geolykt.feedbackvectors.collections;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DelegatingVector<E> extends Vector<E> implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    protected final List<E> delegate;

    public DelegatingVector(List<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public final boolean add(E e) {
        this.add(size(), e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        delegate.add(index, element);
    }

    @Override
    public final boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        } else {
            addAll(size(), c);
            return true;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return delegate.addAll(index, c);
    }

    @Override
    public final void addElement(E obj) {
        this.add(size(), obj);
    }

    @Override
    public final int capacity() {
        return this.size();
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Object clone() {
        return new DelegatingVector<>(new CopyOnWriteArrayList<>(delegate));
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public void copyInto(Object[] anArray) {
        if (anArray == null) {
            throw new NullPointerException("anArray is null.");
        }
        if (anArray.length < delegate.size()) {
            throw new IndexOutOfBoundsException("anArray cannot hold the vector data as it isn't large enough.");
        }
        delegate.toArray(anArray);
    }

    @Override
    public final E elementAt(int index) {
        return get(index);
    }

    @Override
    public Enumeration<E> elements() {
        return new ListEnumeration<>(delegate);
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if (o instanceof DelegatingVector) {
            return ((DelegatingVector) o).delegate.equals(delegate);
        } else if (o instanceof List) {
            return o.equals(delegate);
        } else {
            return false;
        }
    }

    @Override
    public final E firstElement() {
        return this.get(0);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        delegate.forEach(action);
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int indexOf(Object o, int index) {
        Iterator<E> iter = delegate.listIterator(index);
        while (iter.hasNext()) {
            if (Objects.equals(o, iter.next())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public final void insertElementAt(E obj, int index) {
        this.add(index, obj);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public final Iterator<E> iterator() {
        return delegate.listIterator();
    }

    @Override
    public final E lastElement() {
        return this.get(delegate.size() - 1);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public int lastIndexOf(Object o, int index) {
        ListIterator<E> iter = delegate.listIterator(index);
        while (iter.hasPrevious()) {
            if (Objects.equals(o, iter.previous())) {
                return index;
            }
            index--;
        }
        return -1;
    }

    @Override
    public final ListIterator<E> listIterator() {
        return delegate.listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public Stream<E> parallelStream() {
        return delegate.parallelStream();
    }

    @Override
    public E remove(int index) {
        return delegate.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public final void removeAllElements() {
        delegate.clear();
    }

    @Override
    public final boolean removeElement(Object obj) {
        return delegate.remove(obj);
    }

    @Override
    public final void removeElementAt(int index) {
        delegate.remove(index);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return delegate.removeIf(filter);
    }

    @Override
    protected final void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not public API.");
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        delegate.replaceAll(operator);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public E set(int index, E element) {
        return delegate.set(index, element);
    }

    @Override
    public final void setElementAt(E obj, int index) {
        this.set(index, obj);
    }

    @SuppressWarnings("null")
    @Override
    public void setSize(int newSize) {
        int current = delegate.size();
        if (newSize == current) {
            return;
        } else if (newSize < current) {
            for (; newSize < current; current--) {
                delegate.remove(current);
            }
        } else {
            for (; newSize > current; current++) {
                delegate.add(null);
            }
        }
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void sort(Comparator<? super E> c) {
        delegate.sort(c);
    }

    @Override
    public Spliterator<E> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return delegate.stream();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return delegate.toArray(generator);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public final String toString() {
        return Arrays.toString(delegate.toArray());
    }

    @Override
    public void trimToSize() {
        // NOP
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new NotSerializableException("This object should be serialized as a java.util.Vector for maximum effect.");
    }
}

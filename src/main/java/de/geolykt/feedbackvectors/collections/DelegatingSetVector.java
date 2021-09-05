package de.geolykt.feedbackvectors.collections;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class DelegatingSetVector<E> extends DelegatingVector<E> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3967187959182481962L;

    protected final Set<E> set;

    public DelegatingSetVector(List<E> delegate, Set<E> delegateSet) {
        super(delegate);
        this.set = delegateSet;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        set.add(element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            set.addAll(c);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        set.clear();
    }

    @Override
    public Object clone() {
        return new DelegatingSetVector<>(new CopyOnWriteArrayList<>(delegate), new LinkedHashSet<>(set));
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public Enumeration<E> elements() {
        return new ListEnumeration<>(this);
    }

    @Override
    public E remove(int index) {
        E o = super.remove(index);
        set.remove(o);
        return o;
    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            super.remove(o);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (set.removeAll(c)) {
            super.removeAll(c);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Iterator<E> e = this.iterator();
        boolean modified = false;
        while (e.hasNext()) {
            E object = e.next();
            if (filter.test(object)) {
                e.remove();
                set.remove(object);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        for (int i = 0; i < size(); i++) {
            E old = get(i);
            E newObject = operator.apply(old);
            if (old != newObject) {
                set(i, newObject);
            }
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (set.retainAll(c)) {
            super.retainAll(c);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E set(int index, E element) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException("Index " + index + " to large for list of size " + size());
        } else {
            E old = get(index);
            if (element == old) {
                return old;
            }
            set.remove(old);
            set.add(element);
            delegate.set(index, element);
            return old;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new NotSerializableException("This object should be serialized as a java.util.Vector for maximum effect.");
    }
}

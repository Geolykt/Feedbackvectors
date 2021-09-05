package de.geolykt.feedbackvectors.collections;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Identifiable;

public class IdentityLookupVector<E extends Identifiable> extends DelegatingVector<E> implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 7411117507784542610L;

    protected final Map<Integer, E> lookupMap = new ConcurrentHashMap<>();

    public IdentityLookupVector() {
        super(new CopyOnWriteArrayList<>());
    }

    public IdentityLookupVector(Collection<? extends E> c) {
        super(new CopyOnWriteArrayList<>());
        addAll(c);
    }

    public IdentityLookupVector(List<E> delegate, @Nullable Collection<? extends E> c) {
        super(delegate);
        if (c != null) {
            addAll(c);
        }
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        lookupMap.put(element.getUID(), element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            c.forEach(element -> {
                lookupMap.put(element.getUID(), element);
            });
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        lookupMap.clear();
    }

    @Override
    public Object clone() {
        return new IdentityLookupVector<>(delegate);
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Identifiable)) {
            return false;
        }
        return lookupMap.containsKey(((Identifiable) o).getUID());
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final Enumeration<E> elements() {
        return new ListEnumeration<>(this);
    }

    public E getById(int id) {
        return lookupMap.get(id);
    }

    @Override
    public E remove(int index) {
        E o = super.remove(index);
        lookupMap.remove(o.getUID());
        return o;
    }

    @Override
    public boolean remove(Object o) {
        if (lookupMap.remove(((Identifiable) o).getUID()) != null) {
            super.remove(o);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    public void removeById(int id) {
        remove(lookupMap.get(id));
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Iterator<E> e = this.iterator();
        boolean modified = false;
        while (e.hasNext()) {
            E object = e.next();
            if (filter.test(object)) {
                e.remove();
                lookupMap.remove(object.getUID());
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public final void replaceAll(UnaryOperator<E> operator) {
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
        if (super.retainAll(c)) {
            lookupMap.clear();
            for (E e : this) {
                lookupMap.put(e.getUID(), e);
            }
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
            lookupMap.remove(old.getUID());
            lookupMap.put(element.getUID(), element);
            delegate.set(index, element);
            return old;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new NotSerializableException("This object should be serialized as a java.util.Vector for maximum effect.");
    }
}

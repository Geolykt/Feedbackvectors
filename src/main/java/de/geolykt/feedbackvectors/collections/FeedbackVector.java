package de.geolykt.feedbackvectors.collections;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import de.geolykt.feedbackvectors.events.FeedbackCollection;
import de.geolykt.feedbackvectors.events.FeedbackConsumer;

public class FeedbackVector<E> extends DelegatingSetVector<E> implements FeedbackCollection<E>, Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -6136858343622393088L;

    public final LinkedList<FeedbackConsumer<E>> feedbackConsumers = new LinkedList<>();

    public FeedbackVector() {
        super(new LinkedList<>(), new HashSet<>());
    }

    public FeedbackVector(Collection<? extends E> c) {
        this();
        super.addAll(c);
    }

    public FeedbackVector(List<E> delegate, Set<E> delegateSet, @Nullable Collection<? extends E> c) {
        super(delegate, delegateSet);
        if (c != null) {
            super.addAll(c);
        }
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        feedbackAdd(element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(c)) {
            c.forEach(this::feedbackAdd);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        feedbackClear();
    }

    @Override
    public Object clone() {
        return new FeedbackVector<>(delegate);
    }

    protected void feedbackAdd(E element) {
        feedbackConsumers.forEach(consumer -> consumer.added(this, element));
    }

    protected void feedbackClear() {
        feedbackConsumers.forEach(consumer -> consumer.clear(this));
    }

    protected void feedbackRemove(E element) {
        feedbackConsumers.forEach(consumer -> consumer.removed(this, element));
    }

    @Override
    public E remove(int index) {
        E ret = super.remove(index);
        feedbackRemove(ret);
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            feedbackRemove((E) o);
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
                feedbackRemove(object);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (!c.contains(element)) {
                modified = true;
                iterator.remove();
                feedbackRemove(element);
                set.remove(element);
            }
        }
        return modified;
    }

    @Override
    public E set(int index, E element) {
        E old = super.set(index, element);
        if (old != element) {
            feedbackRemove(old);
            feedbackAdd(element);
        }
        return old;
    }

    @Override
    public void addConsumer(FeedbackConsumer<E> consumer) {
        feedbackConsumers.add(consumer);
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new NotSerializableException("This object should be serialized as a java.util.Vector for maximum effect.");
    }
}

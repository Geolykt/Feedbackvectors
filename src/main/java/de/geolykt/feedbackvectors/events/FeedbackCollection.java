package de.geolykt.feedbackvectors.events;

import java.util.Collection;

public interface FeedbackCollection<T> extends Collection<T> {
    public void addConsumer(FeedbackConsumer<T> consumer);
}

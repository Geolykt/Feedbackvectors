package de.geolykt.feedbackvectors.events;

import org.jetbrains.annotations.NotNull;

public interface FeedbackConsumer<E> {
    public void added(@NotNull FeedbackCollection<E> vector, E added);
    public void clear(@NotNull FeedbackCollection<E> vector);
    public void removed(@NotNull FeedbackCollection<E> vector, E removed);
}

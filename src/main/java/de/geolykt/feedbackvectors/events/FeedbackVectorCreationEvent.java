package de.geolykt.feedbackvectors.events;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.event.Event;

public class FeedbackVectorCreationEvent extends Event {

    public enum VectorType {
        // GU prefix = anything in the galimulator unsafe class
        GU_ACTIVE_EMPIRES,
        GU_ACTORS,
        GU_ALLIANCES,
        GU_ARTIFACTS,
        GU_COOPERATIONS,
        GU_DISTRUPTED_STARS,
        GU_FOLLOWED_PEOPLE,
        GU_PEOPLE,
        GU_QUESTS,
        GU_STARS,
        GU_WARS;
    }

    @NotNull
    protected final FeedbackCollection<?> collection;

    @NotNull
    protected final VectorType type;

    public FeedbackVectorCreationEvent(@NotNull VectorType type, @NotNull FeedbackCollection<?> collection) {
        this.type = Objects.requireNonNull(type, "type is null!");
        this.collection = Objects.requireNonNull(collection, "collection is null!");
    }

    @NotNull
    public FeedbackCollection<?> getCollection() {
        return collection;
    }

    @NotNull
    public VectorType getVectorType() {
        return type;
    }
}

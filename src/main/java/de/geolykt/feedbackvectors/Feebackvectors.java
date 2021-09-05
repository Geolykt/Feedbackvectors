package de.geolykt.feedbackvectors;

import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.mod.Extension;

public class Feebackvectors extends Extension {

    @Override
    public void initialize() {
        EventManager.registerListener(new FeedbackvectorsListener());
    }
}

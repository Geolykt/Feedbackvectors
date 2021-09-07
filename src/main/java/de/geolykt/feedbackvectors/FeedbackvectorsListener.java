package de.geolykt.feedbackvectors;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Vector;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.War;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.EventHandler;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.Listener;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEvent;
import de.geolykt.starloader.api.event.lifecycle.LogicalTickEvent;

import de.geolykt.feedbackvectors.collections.FeedbackVector;
import de.geolykt.feedbackvectors.collections.IdentityFeedbackVector;
import de.geolykt.feedbackvectors.events.FeedbackVectorCreationEvent;
import de.geolykt.feedbackvectors.events.FeedbackVectorCreationEvent.VectorType;

/**
 * Listener that tries to convert the internal vectors to Feedback vectors as fast as possible and is also
 * responsible of converting feedback vectors back into normal vectors during the save process and undoing this
 * operating afterwards. This class is full of Galimulator.Unsafe uses, so this class is not something for someone
 * that doesn't like to see the use of deprecated methods.
 *<p>
 * Either way I will eventually remove the deprecation of the unsafe class in favour of deprecating the getters when
 * the class is reasonably well stabilised.
 */
public class FeedbackvectorsListener implements Listener {

    private Vector<ActorSpec> actors;
    private Vector<Alliance> alliances;
    private Vector<?> artifacts;
    private Vector<?> cooperations;
    private Vector<Star> disruptedStars;
    private Vector<ActiveEmpire> empires;
    private Vector<DynastyMember> followedPeople;
    private Vector<DynastyMember> people;
    private Vector<?> quests;
    private Vector<Star> stars;
    private Vector<War> wars;

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onTick(LogicalTickEvent e) {
        if (e.getPhase() == LogicalTickEvent.Phase.PRE_GRAPHICAL) {
            Galimulator.Unsafe galimulatorUnsafe = Galimulator.getImplementation().getUnsafe();
            Vector<ActorSpec> actors = galimulatorUnsafe.getActorsUnsafe();
            Vector<Alliance> alliances = galimulatorUnsafe.getAlliancesUnsafe();
            Vector<?> artifacts = galimulatorUnsafe.getArtifactsUnsafe();
            Vector<?> cooperations = galimulatorUnsafe.getCooperationsUnsafe();
            Vector<Star> disruptedStars = galimulatorUnsafe.getDisruptedStarsUnsafe();
            Vector<ActiveEmpire> empires = galimulatorUnsafe.getEmpiresUnsafe();
            Vector<DynastyMember> followedPeople = galimulatorUnsafe.getFollowedPeopleUnsafe();
            Vector<DynastyMember> people = galimulatorUnsafe.getPeopleUnsafe();
            Vector<?> quests = galimulatorUnsafe.getQuestsUnsafe();
            Vector<Star> stars = galimulatorUnsafe.getStarsUnsafe();
            Vector<War> wars = galimulatorUnsafe.getWarsUnsafe();

            // Lookup by identity / UID
            if (!(actors instanceof IdentityFeedbackVector)) {
                IdentityFeedbackVector<ActorSpec> actors2 = new IdentityFeedbackVector<>(new LinkedList<>(), actors);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_ACTORS, actors2));
                galimulatorUnsafe.setActorsUnsafe(actors2);
            }
            if (!(disruptedStars instanceof IdentityFeedbackVector)) {
                IdentityFeedbackVector<Star> vect = new IdentityFeedbackVector<>(new LinkedList<>(), disruptedStars);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_DISTRUPTED_STARS, vect));
                galimulatorUnsafe.setDisruptedStarsUnsafe(vect);
            }
            if (!(empires instanceof IdentityFeedbackVector)) {
                IdentityFeedbackVector<ActiveEmpire> vect = new IdentityFeedbackVector<>(new LinkedList<>(), empires);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_ACTIVE_EMPIRES, vect));
                galimulatorUnsafe.setEmpiresUnsafe(vect);
            }
            if (!(followedPeople instanceof IdentityFeedbackVector)) {
                IdentityFeedbackVector<DynastyMember> vect = new IdentityFeedbackVector<>(new LinkedList<>(), followedPeople);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_FOLLOWED_PEOPLE, vect));
                galimulatorUnsafe.setFollowedPeopleUnsafe(vect);
            }
            if (!(people instanceof IdentityFeedbackVector)) {
                IdentityFeedbackVector<DynastyMember> vect = new IdentityFeedbackVector<>(new LinkedList<>(), people);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_PEOPLE, vect));
                galimulatorUnsafe.setPeopleUnsafe(vect);
            }
            if (!(stars instanceof IdentityFeedbackVector)) {
                IdentityFeedbackVector<Star> vect = new IdentityFeedbackVector<>(new LinkedList<>(), stars);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_STARS, vect));
                galimulatorUnsafe.setStarsUnsafe(vect);
            }

            // No lookup based on UID, instead using HashCode for contains operations
            if (!(alliances instanceof FeedbackVector)) {
                FeedbackVector<Alliance> vect = new FeedbackVector<>(new LinkedList<>(), new LinkedHashSet<>(), alliances);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_ALLIANCES, vect));
                galimulatorUnsafe.setAlliancesUnsafe(vect);
            }
            if (!(artifacts instanceof FeedbackVector)) {
                FeedbackVector<?> vect = new FeedbackVector<>(new LinkedList<>(), new LinkedHashSet<>(), artifacts);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_ARTIFACTS, vect));
                galimulatorUnsafe.setArtifactsUnsafe(vect);
            }
            if (!(cooperations instanceof FeedbackVector)) {
                FeedbackVector<?> vect = new FeedbackVector<>(new LinkedList<>(), new LinkedHashSet<>(), cooperations);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_COOPERATIONS, vect));
                galimulatorUnsafe.setCooperationsUnsafe(vect);
            }
            if (!(quests instanceof FeedbackVector)) {
                FeedbackVector<?> vect = new FeedbackVector<>(new LinkedList<>(), new LinkedHashSet<>(), quests);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_QUESTS, vect));
                galimulatorUnsafe.setQuestsUnsafe(vect);
            }
            if (!(wars instanceof FeedbackVector)) {
                FeedbackVector<War> vect = new FeedbackVector<>(new LinkedList<>(), new LinkedHashSet<>(), wars);
                EventManager.handleEvent(new FeedbackVectorCreationEvent(VectorType.GU_WARS, vect));
                galimulatorUnsafe.setWarsUnsafe(vect);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSaveStart(GalaxySavingEvent event) {
        Galimulator.Unsafe galimulatorUnsafe = Galimulator.getImplementation().getUnsafe();
        actors = galimulatorUnsafe.getActorsUnsafe();
        alliances = galimulatorUnsafe.getAlliancesUnsafe();
        artifacts = galimulatorUnsafe.getArtifactsUnsafe();
        cooperations = galimulatorUnsafe.getCooperationsUnsafe();
        disruptedStars = galimulatorUnsafe.getDisruptedStarsUnsafe();
        empires = galimulatorUnsafe.getEmpiresUnsafe();
        followedPeople = galimulatorUnsafe.getFollowedPeopleUnsafe();
        people = galimulatorUnsafe.getPeopleUnsafe();
        quests = galimulatorUnsafe.getQuestsUnsafe();
        stars = galimulatorUnsafe.getStarsUnsafe();
        wars = galimulatorUnsafe.getWarsUnsafe();
        galimulatorUnsafe.setActorsUnsafe(new Vector<>(actors));
        galimulatorUnsafe.setAlliancesUnsafe(new Vector<>(alliances));
        galimulatorUnsafe.setArtifactsUnsafe(new Vector<>(artifacts));
        galimulatorUnsafe.setCooperationsUnsafe(new Vector<>(cooperations));
        galimulatorUnsafe.setDisruptedStarsUnsafe(new Vector<>(disruptedStars));
        galimulatorUnsafe.setEmpiresUnsafe(new Vector<>(empires));
        galimulatorUnsafe.setFollowedPeopleUnsafe(new Vector<>(followedPeople));
        galimulatorUnsafe.setPeopleUnsafe(new Vector<>(people));
        galimulatorUnsafe.setQuestsUnsafe(new Vector<>(quests));
        galimulatorUnsafe.setStarsUnsafe(new Vector<>(stars));
        galimulatorUnsafe.setWarsUnsafe(new Vector<>(wars));
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSaveEnd(GalaxySavingEndEvent event) {
        Galimulator.Unsafe galimulatorUnsafe = Galimulator.getImplementation().getUnsafe();
        galimulatorUnsafe.setActorsUnsafe(NullUtils.requireNotNull(actors));
        galimulatorUnsafe.setAlliancesUnsafe(NullUtils.requireNotNull(alliances));
        galimulatorUnsafe.setArtifactsUnsafe(NullUtils.requireNotNull(artifacts));
        galimulatorUnsafe.setCooperationsUnsafe(NullUtils.requireNotNull(cooperations));
        galimulatorUnsafe.setDisruptedStarsUnsafe(NullUtils.requireNotNull(disruptedStars));
        galimulatorUnsafe.setEmpiresUnsafe(NullUtils.requireNotNull(empires));
        galimulatorUnsafe.setFollowedPeopleUnsafe(NullUtils.requireNotNull(followedPeople));
        galimulatorUnsafe.setPeopleUnsafe(NullUtils.requireNotNull(people));
        galimulatorUnsafe.setQuestsUnsafe(NullUtils.requireNotNull(quests));
        galimulatorUnsafe.setStarsUnsafe(NullUtils.requireNotNull(stars));
        galimulatorUnsafe.setWarsUnsafe(NullUtils.requireNotNull(wars));
    }
}

package org.hyperic.hq.events.ext;

import java.util.Collection;
/**
 * Repository of in-memory representations of alert triggers
 * @author jhickey
 *
 */
public interface RegisterableTriggerRepository {

    /**
     *
     * @param eventClass The event class
     * @param instanceId The id of the source instance of the event
     * @return The {@link RegisterableTriggerInterface}s interested in the event
     */
    Collection getInterestedTriggers(Class eventClass, Integer instanceId);

    /**
     *
     * @param trigger The trigger to add to the repository
     */
    void addTrigger(RegisterableTriggerInterface trigger);

    /**
     *
     * @param triggerId The trigger to remove from the repository
     */
    void removeTrigger(Integer triggerId);

    /**
     * Enables or disables triggers.  Should be called when the alert definition is enabled or disabled.
     * Disabled triggers will not be returned from calls to getInterestedTriggers, and therefore will not receive events from the RegisteredDispatcher
     * @param triggerIds  The triggers that should be enabled or disabled
     * @param enabled true if triggers should be enabled
     */
    void setTriggersEnabled(Collection triggerIds, boolean enabled);


}

package symposium.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The class Constraints is an abstract class and the main parent for all constraint classes.
 * Each Constraint has a given priority (enum), which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
 * The class holds a cache which stores a map of venueTimes and their violations.
 * The class also has methods to test if constraints are violated or not.
 * Please refer to each constraint class for their documentation.
 */

public abstract class Constraint {
    public final ConstraintPriority PRIORITY;
    public final Panel PANEL;
    Map<VenueTime, Boolean> cache = new HashMap<VenueTime, Boolean>();

    /**
     * Constructs for the Constraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     */
    public Constraint(ConstraintPriority priority, Panel panel) {  //Refering to ScheduleData globally?
        this.PRIORITY = priority;
        this.PANEL = panel;
    }

    /**
     * <b>Dependencies:</b>  isConstraintViolated method, VenueTime class
     *
     * The method checks the cache for each VenueTime and returns the violation
     * If the cache is empty populate it with the violation through isConstraintViolated()
     *
     * @param venueTime The time being checked
     * @return boolean; The violation value for the venueTime. True if constraint is violated at prospective venueTime, false otherwise.
     */
    public boolean checkViolationCache(VenueTime venueTime) {
        if (cache.get(venueTime) == null) {
            boolean violated = isConstraintViolated(venueTime);
            cache.put(venueTime, violated);
            return violated;
        }
        else {
            return cache.get(venueTime);
        }
    }

    //ToDo: This method needs to be refactored!

    /**
     *
     * <b>Dependencies:</b>  isConstraintViolated method, VenueTime class
     *
     * The method checks the cache for each VenueTime and returns the violation
     * If the cache is empty populate it with the violation through isConstraintViolated()
     *
     * @return boolean; The violation value for the venueTime. True if constraint is violated at prospective venueTime, false otherwise.
     */

    public boolean checkViolationCache() {
        VenueTime venueTime = PANEL.getVenueTime();
        if (cache.get(venueTime) == null) {
            boolean violated = isConstraintViolated(venueTime);
            return violated;
        } else {
            return cache.get(venueTime);
        }
    }

    /**
     * <b>Dependencies:</b> VenueTime class
     * Method to be implemented in the child classes.
     *
     * Depending on the constraint this method implementation will change. The general idea is to test if the constraint is violated or not
     * @param venueTime The time being checked
     * @return boolean; If the constraint is violated returns true, otherwise, returns false.
     */
    public abstract boolean isConstraintViolated(VenueTime venueTime);


    /**
     * <b>Dependencies:</b> VenueTime class
     * Similar to isConstraintViolated it tests a specific panel constraint.
     *
     * The method test the comparison of the venueTime for the given panel with all the other panels scheduled at that time.
     * @return boolean; If the constraint is violated returns true, otherwise, returns false.
     */

    public boolean isConstraintViolated() {
        if (PANEL.getVenueTime() == null) {
            return false;
        }
        else {
            return this.isConstraintViolated(PANEL.getVenueTime());
        }
    }

    /**
     * Removes the cached bool for the specified venueTime.
     * @param venueTime The time being checked
     */
    public void removeCachedValue(VenueTime venueTime) {

        cache.remove(venueTime);
    }

    /**
     * Clears the entire cache, starts anew cache.
     */
    public void clearCache() {
        cache = new HashMap<VenueTime, Boolean>();
    }

    /**
     * The toString method will differ for each constraint.
     * The method specifies what is meant when a constraint is violated
     * Each constraint will return the message associated with it when it is violated.
     * @return String message of the violation.
     */

    public String toString() {
        return "Constraint is violated";
    }
}
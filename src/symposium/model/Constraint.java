package symposium.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Constraint {
    public final ConstraintPriority PRIORITY;
    public final Panel PANEL;
    Map<VenueTime, Boolean> cache = new HashMap<VenueTime, Boolean>();

    /**
     * Constructs constraint.
     * Dependencies: Panel class, Parser class
     *
     * @param priority enum determining whether constraint is required, important, or desirable.
     * @param p        the Panel that the constraint is part of.
     */
    public Constraint(ConstraintPriority priority, Panel p) {  //Refering to ScheduleData globally?
        this.PRIORITY = priority;
        this.PANEL = p;
    }

    /**
     * Checks cach to see if bool stored for particular venue time for this constraint
     * If not, runs isConstraintViolated on prospective venueTime
     * If stored, returns cached bool.
     * Dependencies: isConstraintViolated method, VenueTime class
     * @param venueTime
     * @return True is constraint is violated at prospective venueTime, false otherwise.
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

    /**
     * Checks cache to see if bool stored for particular venue time for this constraint
     * If not stored, runs isConstraintViolated on PANEL's current venueTime
     * If stored, returns the cached bool.
     * Dependencies: isConstraintViolated method, VenueTime class, PANEL.getVenueTime method
     *
     * @return True if constraint is violated at current venueTime, false otherwise.
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
     * Dependencies: VenueTime class
     *
     * @param venueTime
     * @return True if costraint is violated at prospective venueTime, false otherwise.
     */
    public abstract boolean isConstraintViolated(VenueTime venueTime);

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
     * @param venueTime
     */
    public void removeCachedValue(VenueTime venueTime) {
        cache.remove(venueTime);
    }

    /**
     * Clears the entire cache, starts anew.
     */
    public void clearCache() {
        cache = new HashMap<VenueTime, Boolean>();
    }
}
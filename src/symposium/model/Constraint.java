package symposium.model;

public abstract class Constraint {
    public short final PRIORITY;
    Panel panel;
    Map<VenueTime, boolean> cache = new Map<VenueTime, boolean>;

    /**
     * Constructs constraint.
     * Dependencies: Panel class, Parser class
     *
     * @param priority enum determining whether constraint is required, important, or desirable.
     * @param p        the Panel that the constraint is part of.
     */
    public Constraint(short priority, Panel p) {  //Refering to ScheduleData globally?
        PRIORITY = priority;
        panel = p;
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
     * If not stored, runs isConstraintViolated on panel's current venueTime
     * If stored, returns the cached bool.
     * Dependencies: isConstraintViolated method, VenueTime class, panel.getVenueTime method
     *
     * @return True if constraint is violated at current venueTime, false otherwise.
     */
    public boolean checkViolationCache() {
        venueTime = panel.getVenueTime();
        if (cache.get(venueTime) == null) {
            boolean violated = isConstraintViolated(venueTime);
            cache.put(venueTime, violated);
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
    public boolean isConstraintViolated(VenueTime venueTime);

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
        cache = new Map<VenueTime, Boolean>;
    }
}
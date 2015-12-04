package symposium.model;

abstract class NoOverlapConstraint extends Constraint {

    public NoOverlapConstraint(short priority, Panel p) {
        super(priority, p);
    }
    /**
     * Dependencies: (Additionally) doesOvelap method
     * @param venueTime The time being checked by doesOverlap
     * @return If there is a category or panelist scheduled at the same time, returns true, otherwise returns false.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (doesOverlap(venueTime)) {
            violated = true;
        }
        else {
            violated = false;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    abstract boolean doesOverlap(VenueTime venueTime);
}
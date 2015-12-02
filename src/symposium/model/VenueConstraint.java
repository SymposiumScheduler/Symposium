package symposium.model;

class VenueConstraint extends Constraint {
    Venue venue;

    /**
     * Dependencies: (Additionally) Venue class
     * @param v The venue the panel must appear in.
     */
    @Override
    public VenueConstraint(short priority, Panel p, Venue v) {
        PRIORITY = priority;
        panel = p;
        venue = v;
    }

    /**
     * Dependencies: (Additionally) venueTime.getAssignedPanel method
     * @param venueTime
     * @return If the panel is assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (venueTime.getAssignedPanel() != venue) {
            violated = true;
        }
        else {
            violated = false;
        }
        cache.put(venueTime, violated);
        return violated;
    }
}
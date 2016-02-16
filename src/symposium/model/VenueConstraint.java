package symposium.model;

public class VenueConstraint extends Constraint {
    Venue venue;

    /**
     * Dependencies: (Additionally) Venue class
     * @param v The venue the panel must appear in.
     */
    public VenueConstraint(ConstraintPriority priority, Panel p, Venue v) {
        super(priority, p);
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
        if (venueTime.VENUE != venue) {
            violated = true;
        }
        else {
            violated = false;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    @Override
    public String toString() {
        return "Venue Constraint Violated: Venue name: " +this.venue.NAME;
    }
}
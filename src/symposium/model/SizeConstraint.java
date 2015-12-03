package symposium.model;

class SizeConstraint extends Constraint {

    short minSize;

    /**
     *
     * @param mSize The minimum size necessary for the panel to fit.
     */
    public SizeConstraint(short priority, Panel p, short mSize) {
        super(priority, p);
        minSize = mSize;
    }

    /**
     * Dependencies: (Additionally) venueTime.VENUE.SIZE variable
     * @param venueTime
     * @return true if the size is smaller than the minimum size required, false otherwise.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (venueTime.VENUE.SIZE >= minSize) {
            violated = false;
        }
        else {
            violated = true;
        }
        cache.put(venueTime, violated);
        return violated;
    }
}

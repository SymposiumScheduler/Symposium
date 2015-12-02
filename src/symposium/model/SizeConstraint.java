package symposium.model;

class SizeConstraint extends Constraint {

    short minSize;

    /**
     *
     * @param mSize The minimum size necessary for the panel to fit.
     */
    @Override
    public SizeConstraint(short priority, Panel p, short mSize) {
        PRIORITY = priority;
        panel = p;
        minSize = mSize;
    }

    /**
     * Dependencies: (Additionally) venueTime.VENUE.SIZE variable
     * @param venueTime
     * @return true if the size is smaller than the minimum size required, false otherwise.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if venueTime.VENUE.SIZE >= minSize {
            return false;
        }
        else {
            return true;
        }
    }
}

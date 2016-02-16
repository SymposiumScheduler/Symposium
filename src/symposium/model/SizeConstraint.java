package symposium.model;

public class SizeConstraint extends Constraint {

    int minSize;
    VenueTime vt;

    /**
     *
     * @param mSize The minimum size necessary for the panel to fit.
     */
    public SizeConstraint(ConstraintPriority priority, Panel p, int mSize) {
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
            vt = venueTime;
            violated = true;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    public int getMinSize(){
        return this.minSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Size Constraint Violated: (Venue must be able to fit panelists)").append("\n");
        sb.append("\t\t\tSize needed").append(minSize).append("Size Given ").append(vt.VENUE.SIZE);

        return sb.toString();
    }
}

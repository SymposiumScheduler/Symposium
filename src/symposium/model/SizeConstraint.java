package symposium.model;

/**
 * The class SizeConstraint inherits from Constraints, @see Constraint for documentation.
 * Size Constraint is violated when a panel is given a venue smaller than the minimum size required,
 * in other words when the venue does not fit the panel.
 *
 */

public class SizeConstraint extends Constraint {

    int minSize;
    VenueTime venueTimeViolating;

    /**
     * Constructs for the SizeConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     * @param mSize    The minimum size necessary for the panel to fit in.
     */

    public SizeConstraint(ConstraintPriority priority, Panel panel, int mSize) {
        super(priority, panel);
        minSize = mSize;
    }

    /**
     * <b>Dependencies:</b> venueTime.VENUE.SIZE variable
     *
     * The method tests if the venue given to a panel is smaller than the minimum size required.
     * The method uses the minSize of the panel, which is passed through the constructor.
     *
     * When the method is violated the the venue is assigned to venueTimeViolating so we can report what size was required.
     *
     * @param venueTime The time being checked
     * @return boolean; True if the size is smaller than the minimum size required, otherwise, false.
     */

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (venueTime.VENUE.SIZE >= minSize) {
            violated = false;
        }
        else {
            venueTimeViolating = venueTime;
            violated = true;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    /**
     *
     * @return the minSize of the given panel
     */

    public int getMinSize() {

        return this.minSize;
    }

    /**
     * @return String of the violation message
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Size Constraint Violated: (Venue must be able to fit panel)").append("\n");
        sb.append("\t\t\tSize needed").append(minSize).append("Size Given ").append(venueTimeViolating.VENUE.SIZE);

        return sb.toString();
    }
}

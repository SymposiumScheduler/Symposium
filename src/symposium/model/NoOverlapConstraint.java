package symposium.model;

/**
 * The class NoOverlapConstraint is an abstract class which inherits from Constraints, @see Constraint for documentation.
 * The class is the parent class, which represents any constraints that tests the overlap of any panels constraints.
 * For example, the category constraints is an overlapping constraint, such as we compare whether two panels overlap or not on their category.
 * Constraint is violated when two panels are scheduled at the same time and have an overlap of a certain constraint.
 */
public abstract class NoOverlapConstraint extends Constraint {

    /**
     * Constructs for the NoOverlapConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     */
    public NoOverlapConstraint(ConstraintPriority priority, Panel panel) {
        super(priority, panel);
    }

    /**
     * <b>Dependencies:</b> doesOverlap method
     *
     * The method tests the comparison of the venueTime for the given panel with all the other panels scheduled at that time.
     * @param venueTime The time being checked by doesOverlap
     * @return bool If there are two panels scheduled at same time and have an overlap of a certain constraint, return true, otherwise returns false.
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

    /**
     * Method to be implemented in the child classes.
     * The method compares the venueTime for the given panel with all the other panels scheduled at that time.
     * @param venueTime The venueTime being checked by doesOverlap
     * @return bool If there are two panels scheduled at same time and have an overlap of a certain constraint, returns true, otherwise returns false.
     */
    abstract boolean doesOverlap(VenueTime venueTime);
}
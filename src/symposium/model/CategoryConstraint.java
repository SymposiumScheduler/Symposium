package symposium.model;

/**
 * The class CategoryConstraint inherits from {@link symposium.model.Constraint}.
 * Category Constraint is violated when two panels of same category are scheduled at same time.
 *
 */
public class CategoryConstraint extends NoOverlapConstraint {

    /**
     * Constructs for the CategoryConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     */
    public CategoryConstraint(ConstraintPriority priority, Panel panel) {
        super(priority, panel);
    }

    /**
     * <b>Dependencies:</b> ScheduleData class, ScheduleData.isAssigned method, Panel.CATEGORY variable
     *
     * The method compares the venueTime for the given panel with all the other panels scheduled at that time.
     * The method uses isAssignedCategories() to test if there is another panel with the same category at that time
     *
     * @param venueTime The venueTime to check the Category against.
     * @return boolean; If any other panel with the same category is scheduled at the same time as venueTime, return true,  otherwise, return false.
     *
     */
    @Override
    boolean doesOverlap(VenueTime venueTime) {
        if (ScheduleData.instance().isAssignedCategories(venueTime, PANEL.CATEGORIES)) { //A variant of the above function, written to check category instead of panelists
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @return String of the violation message
     */
    @Override
    public String toString() {
        return "Category Constraint is violated (Two panels of same category can't be scheduled at same time)";
    }
}
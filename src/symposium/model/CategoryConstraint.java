package symposium.model;

import java.util.List;

public class CategoryConstraint extends NoOverlapConstraint {
    private static List<String> categoryViolating;

    public CategoryConstraint(ConstraintPriority priority, Panel p) {
        super(priority, p);
    }
    /**
     * Dependencies: ScheduleData class, ScheduleData.isAssigned method, Panel.CATEGORY variable
     * @param venueTime The venueTime to check the Category against.
     * @return If any other panel with the same category is scheduled at the same time as venueTime, return true.  Otherwise, return false.
     */
    @Override
    boolean doesOverlap(VenueTime venueTime) {
        if (ScheduleData.instance().isAssignedCategories(venueTime, PANEL.CATEGORIES)) { //A variant of the above function, written to check category instead of panelists
            categoryViolating = PANEL.CATEGORIES;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category Constraint is violated (Two panels of same category can't be scheduled at same time) ").append("\n");
        sb.append("\t\t\t").append("The Categories are: ").append(categoryViolating);


        return sb.toString();
    }
}
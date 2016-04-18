package symposium.model;

import java.util.List;

/**
 * PanelistConstraint inherits from {@link symposium.model.NoOverlapConstraint NoOverlapConstraint}.
 * Panelist Constraint is violated when a panelist belonging to this panel is on another panel that has an overlapping schedule.
 */
public class PanelistConstraint extends NoOverlapConstraint {
    public static List<String> panelistsViolating;

    /**
     * Constructs for the PanelistConstraint class.
     *
     * @param priority enum that determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The panel the constraint belongs to.
     */
    public PanelistConstraint(ConstraintPriority priority, Panel panel) {
        super(priority,panel);
    }
    /**
     * <b>Dependencies:</b> ScheduleData class, ScheduleData.isAssignedPanelists method, Panel.PANELIST variable
     *
     * The method compares all panelists assigned to this panel to all other panels they are assigned to.
     *
     * @param venueTime The venueTime to check Panelists against
     * @return boolean; If at least one panelist is scheduled at the same time as venueTime, return true.  Otherwise, return false.
     */
    @Override
    boolean doesOverlap(VenueTime venueTime) {  // As written currently, assumes ScheduleData is a global singleton
        if( ScheduleData.instance().isAssignedPanelists(venueTime, PANEL.PANELISTS)) {
            panelistsViolating = PANEL.PANELISTS;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Panelist Constraint Violated: Panelist cannot appear outside of availability or be in two places at once)").append("\n");
        sb.append("\t\t\t").append("Panelists are: ").append(panelistsViolating);

        return sb.toString();
    }
}
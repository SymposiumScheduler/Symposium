package symposium.model;

import java.util.List;

public class PanelistConstraint extends NoOverlapConstraint {
    public static List<String> panelistsViolating;

    public PanelistConstraint(ConstraintPriority priority, Panel p) {
        super(priority,p);
    }
    /**
     * There's a (potential) logic error in the way this CURRENTLY works with checkViolationCache,
     * in that panelists will be assigned to the current venueTime
     * so it may return true instead of false if ScheduleData.isAssigned isn't written to take this into account.
     * Dependencies: ScheduleData class, ScheduleData.isAssigned method, Panel.PANELIST variable
     * @param venueTime The venueTime to check Panelists against
     * @return If at least one panelist is scheduled at the same time as venueTime, return true.  Otherwise, return false.
     */
    @Override
    boolean doesOverlap(VenueTime venueTime) {  // As written currently, assumes ScheduleData is a global singleton
        if( ScheduleData.instance().isAssignedPanelists(venueTime, PANEL.PANELISTS)) { //Unwritten function in ScheduleData, checks if any of the panelists are assigned at any overlapping time slot
            panelistsViolating = PANEL.PANELISTS;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Panelist Constraint Violated: Panelist cannot be in two places at once").append("\n");
        sb.append("\t\t\t").append("Panelists are: ").append(panelistsViolating);

        return sb.toString();
    }
}
package symposium.model;

public class PanelistConstraint extends NoOverlapConstraint {

    public PanelistConstraint(short priority, Panel p) {
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
            return true;
        }
        else {
            return false;
        }
    }
}
package symposium.model;

class CategoryConstraint extends NoOverlapConstraint {

    /**
     * Dependencies: ScheduleData class, ScheduleData.isAssigned method, Panel.CATEGORY variable
     * @param venueTime The venueTime to check the Category against.
     * @return If any other panel with the same category is scheduled at the same time as venueTime, return true.  Otherwise, return false.
     */
    @Override
    private boolean doesOverlap(VenueTime venueTime) {
        if ScheduleData.isAssigned(venueTime, panel.CATEGORY) { //A variant of the above function, written to check category instead of panelists
            return true
        }
        else {
            return false
        }
    }
}
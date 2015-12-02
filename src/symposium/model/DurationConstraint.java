package symposium.model;

/**
 * Constraint class; its presence in a panel means that the slot it takes up should be short.
 */
class DurationConstraint extends TimeConstraint {
    //May change implementation in future if VenueTime implements an isShort bool
    /**
     * Checks the time to see if it counts as short or long.
     * Dependencies: VenueTime Class, Range Interface, VenueTime.getTime method
     * @return bool true for if VenueTime is short or false for if it's long.
     */
    @Override
    private boolean checkTime(VenueTime venueTime) { //This is old, and the variable name for short VenueTime doesn't exist yet
        Range time = venueTime.getTime();
        if (time.END-time.START < 60) {
            return true;
        }
        else {
            return false;
        }
    }

}
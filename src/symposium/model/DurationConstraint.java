package symposium.model;

/**
 * Constraint class; its presence in a panel means that the slot it takes up should be short.
 */
public class DurationConstraint extends TimeConstraint {

    public DurationConstraint(short priority, Panel p) {
        super(priority, p);
    }
    //May change implementation in future if VenueTime implements an isShort bool
    /**
     * Checks the time to see if it counts as short or long.
     * Dependencies: VenueTime Class, Range Interface, VenueTime.getTime method
     * @return bool true for if VenueTime is short or false for if it's long.
     */
    @Override
    boolean checkTime(VenueTime venueTime) { //This is old, and the variable name for short VenueTime doesn't exist yet
        Range time = venueTime.TIME;
        if (time.getEnd()-time.getStart() < 60) {
            return true;
        }
        else {
            return false;
        }
    }

}
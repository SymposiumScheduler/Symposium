package symposium.model;

public abstract class Constraint {
    public short int final PRIORITY;
    Panel panel;
    /**
     * Constructs constraint.
     * Dependencies: Panel class, Parser class
     * @param priority enum determining whether constraint is required, important, or desirable.
     * @param p the Panel that the constraint is part of.
     */
    public Constraint(short int priority, Panel p){  //Refering to ScheduleData globally?
        PRIORITY = priority;
        panel = p;
    }

    /**
     * As is currently written, is just isConstraintViolated for currently assigned VenueTime.
     * Will be changed later to run isConstraintViolated if no cache for the time exists yet, and just check the cache for true/false if it does.
     * Dependencies: isConstraintViolated method, VenueTime class, panel.getAssignedVenueTime method
     * @return True if constraint is violated at current venueTime, false otherwise.
     */
    public boolean checkViolationCache();

    /**
     * Dependencies: VenueTime class
     * @param venueTime
     * @return True if costraint is violated at prospective venueTime, false otherwise.
     */
    public boolean isConstraintViolated(VenueTime venueTime);
}

class VenueConstraint extends Constraint {
    Venue venue;

    /**
     * Dependencies: (Additionally) Venue class
     * @param v The venue the panel must appear in.
     */
    @Override
    public VenueConstraint(short int priority, Panel p, Venue v) {
        PRIORITY = priority;
        panel = p;
        venue = v;
    }

    @Override
    public boolean checkViolationCache() {
        return isConstraintViolated(panel.getAssignedVenueTime());
    }

    /**
     * Dependencies: (Additionally) venueTime.getAssignedPanel method
     * @param venueTime
     * @return If the panel is assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if (venueTime.getAssignedPanel() != venue) {
            return true;
        }
        else {
            return false;
        }
    }
}
class SizeConstraint extends Constraint {

    short int minSize;

    /**
     *
     * @param mSize The minimum size necessary for the panel to fit.
     */
    @Override
    public SizeConstraint(short int priority, Panel p, short int mSize) {
        PRIORITY = priority;
        panel = p;
        minSize = mSize;
    }

    @Override
    public boolean checkViolationCache(){
        return isConstraintViolated(panel.getAssignedVenueTime());
    }

    /**
     * Dependencies: (Additionally) venueTime.VENUE.SIZE variable
     * @param venueTime
     * @return true if the size is smaller than the minimum size required, false otherwise.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if venueTime.VENUE.SIZE >= minSize {
            return false;
        }
        else {
            return true;
        }
    }
}

//Only once together on a single day, does this apply to specific Panelists or to all Panelists generally?
//CAN'T WRITE UNTIL WE KNOW
class PairedPanelistConstraint extends Constraint {}

abstract class TimeConstraint extends Constraint {

    @Override
    public boolean checkViolationCache() {
        return isConstraintViolated(panel.getAssignedVenueTime());
    }

    /**
     * Dependencies: checkTime method
     * @param venueTime
     * @return true if checkTime is false, returns false if checkTime is true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if checkTime(venueTime) {
            return false;
        }
        else {
            return true;
        }
    }
    
    private boolean checkTime();
}
class NewPanelistConstraint extends TimeConstraint { //Ask team members

    /**
     * Dependencies: Range interface, TimeRange class, Panel class, Panel.PANELIST variable
     * @return true if panel doesn't fall on monday OR there is one new panelist or for fewer, otherwise returns false
     */
    @Override
    private boolean checkTime() {
        Range time = venueTime.getTime(); // Implement in VenueTime later
        Range monday = new TimeRange(0, 1440); // Assuming Joey's current parser, change if implementation changes
        short int newMoreThanTwo = 0;
        for (for i = 0; i < panel.PANELIST.size(); i++) {
            if panel.PANELIST[i].contains('new') {  // built on assumption that the PANELIST array in panel contains the substring
                newMoreThanTwo++;
            }
        }
        if (!time.Intersect(monday) || newMoreThanTwo < 2) {
            return true;
        }
        else {
            return false;
        }
    }
}
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

abstract class NoOverlapConstraint extends Constraint {

    @Override
    public boolean checkViolationCache() {
        return isConstraintViolated(panel.getAssigneVenueTime());
    }

    /**
     * Dependencies: (Additionally) doesOvelap method
     * @param venueTime The time being checked by doesOverlap
     * @return If there is a category or panelist scheduled at the same time, returns true, otherwise returns false.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if doesOverlap(venueTime) {
            return true
        }
        else {
            return false
        }
    }
    
    private boolean doesOverlap(VenueTime venueTime);
}
class PanelistConstraint extends NoOverlapConstraint {

    /**
     * There's a (potential) logic error in the way this CURRENTLY works with checkViolationCache,
     * in that panelists will be assigned to the current venueTime
     * so it may return true instead of false if ScheduleData.isAssigned isn't written to take this into account.
     * Dependencies: ScheduleData class, ScheduleData.isAssigned method, Panel.PANELIST variable
     * @param venueTime The venueTime to check Panelists against
     * @return If at least one panelist is scheduled at the same time as venueTime, return true.  Otherwise, return false.
     */
    @Override
    private boolean doesOverlap(VenueTime venueTime) {  // As written currently, assumes ScheduleData is a global singleton
        if ScheduleData.isAssigned(venueTime, panel.PANELIST) { //Unwritten function in ScheduleData, checks if any of the panelists are assigned at any overlapping time slot
            return true
        }
        else {
            return false
        }
    }
}
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
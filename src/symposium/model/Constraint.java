package symposium.model;

public abstract class Constraint {
    short int PRIORITY;
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
     * 
     */
    public boolean checkViolationCache();
    public boolean isConstraintViolated(VenueTime venueTime);  //Prospective
}

class VenueConstraint extends Constraint {
    Venue venue;
    @Override
    public Constraint(short int priority, Panel p, Venue v) {
        PRIORITY = priority;
        panel = p;
        venue = v;
    }
    
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if (venueTime.getAssignedPanel != venue) {
            return true;
        }
        else {
            return false;
        }
    }
}
class SizeConstraint extends Constraint {}
class PairedPanalelistConstraint extends Constraint {}

abstract class TimeConstraint extends Constraint {
    @Override
    public boolean checkViolationCache(){
        
    }

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
    
    @Override
    private boolean checkTime(){
        Range time = venueTime.getTime(); // Implement in VenueTime later
        Range monday = new TimeRange(0, 2400); // Assuming Joey's current parser, change if implementation changes
        short int newMoreThanTwo = 0;
        for (for i = 0; i < panel.PANELIST.length(); i++){
            if panel.PANELIST[i].contains('new'){  // built on assumption that the PANELIST array in panel contains the substring
                newMoreThanTwo++;
            }
        }
        if (!time.Intersect(monday) || newMoreThanTwo < 2){
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
     * Dependencies: VenueTime, Range
     * @return bool true for short or false for long.
     */
    @Override
    private boolean checkTime(VenueTime venueTime) {
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
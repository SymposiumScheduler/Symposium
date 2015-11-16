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
    public Constraint(short int priority, Panel p){
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

abstract class NoOverlapConstraint extends Constraint {}
class PanelistConstraint extends NoOverlapConstraint {}
class CategoryConstraint extends NoOverlapConstraint {}
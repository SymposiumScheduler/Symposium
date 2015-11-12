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
    public boolean isConstraintViolated(VenueTime venueTime){
    }
    
    private boolean checkTime();
}
class NewPanelistConstraint extends TimeConstraint { //Ask team members
    @Override
    private boolean checkTime(){
        RangeLine time = venueTime.getTime(); // Implement in VenueTime later
        RangeLine monday = new RangeLine(0, 2400); // Assuming Joey's current parser, change if implementation changes
        short int newMoreThanTwo = 0;
        for (for i = 0; i < panel.PANELIST.length(); i++){
            if panel.PANELIST[i].contains('new'){  // built on assumption that the PANELIST array in panel contains the substring
                newMoreThanTwo++;
            }
        }
        if (!time.doesIntersect(monday) || newMoreThanTwo < 2){
            return true;
        }
        else {
            return false;
        }
    }
}
class DurationConstraint extends TimeConstraint {}

abstract class NoOverlapConstraint extends Constraint {}
class PanelistConstraint extends NoOverlapConstraint {}
class CategoryConstraint extends NoOverlapConstraint {}
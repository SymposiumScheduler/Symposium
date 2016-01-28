package symposium.model;

import java.util.*;

public class Panel implements Comparable<Panel>{
    public final String NAME;
    public final Range AVAILABILITY;
    public final List<String> PANELISTS;
    public final List<Constraint> CONSTRAINTS;
    public final List<String> CATEGORIES;
    public final boolean LOCKED;

    private VenueTime assignedVenueTime;
    private int difficulty;

    public Panel(String name, List<String> panelists, Range availability, List<String> categories, List<String> constraintsStrs){
        this.NAME = name;
        this.CATEGORIES = categories;
        this.AVAILABILITY = availability;
        this.PANELISTS = panelists;
        this.CONSTRAINTS = ConstraintFactory.buildConstraints(this, constraintsStrs);
        //The below should probably be handled in parser
        //but this is a constraint that isn't on the list of constraints
        //so this is how it's handled now.
        //Assigns Very Important for now, we should talk with Marc about this.
        this.CONSTRAINTS.add(new SizeConstraint(ConstraintPriority.VERY_IMPORTANT, this, this.PANELISTS.size()));
        //TODO set locked if minimum size is larger than some number
        this.LOCKED = false;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        //concurrency - ( (end - start) + (end - start) ....)
    }

    public int getDifficulty(){
        return difficulty;
    }

    public VenueTime getVenueTime(){
        return assignedVenueTime;
    }

    public void setVenueTime(VenueTime venueTime){
        this.assignedVenueTime = venueTime;
    }

    public String getName(){
        return NAME;
    }

    public Range getAvailability(){
        return AVAILABILITY;
    }

    @Override
    public int compareTo(Panel that) {
        return this.difficulty - that.difficulty;
    }

}

package symposium.model;

import java.util.*;

public class Panel implements Comparable<Panel>{
    public final String NAME;
    public final Range AVAILABILITY;
    public final List<String> PANELISTS;
    public final List<Constraint> CONSTRAINTS;
    public final List<String> CATEGORIES;
    public final boolean LOCKED;


    private final List<String> messages;
    private VenueTime assignedVenueTime;
    private int difficulty;

    public Panel(String name, List<String> panelists, Range availability, List<String> categories, List<String> constraintsStrs){
        this.NAME = name;
        this.CATEGORIES = categories;
        this.AVAILABILITY = availability;
        this.PANELISTS = panelists;
        this.CONSTRAINTS = ConstraintFactory.buildConstraints(this, constraintsStrs);
        this.messages = new ArrayList<>();
        int ctr = 0;
        boolean tmp = false;
        for(Constraint c : CONSTRAINTS){
            if(c instanceof VenueFilter || c instanceof SpecificTimeFilter) {
                ctr++;
            }
            if(ctr == 2){
                tmp = true;
                break;
            }
        }
        this.LOCKED = tmp;
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

    public void addMessage(String message){
        messages.add(message);
    }
    public List<String> getMessages() {
        return this.messages;
    }

    @Override
    public int compareTo(Panel that) {
        return this.difficulty - that.difficulty;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.NAME).append("\n\t");
        if(this.LOCKED) {
            sb.append("Locked Panel").append("\n\t");
        }
        if(this.assignedVenueTime != null) {
            sb.append(this.getVenueTime().toString());
        } else {
            sb.append("Unscheduled");
        }
        sb.append("\n\tMessages:");
        if(this.messages.size()!=0) {
            for (String message : this.messages) {
                sb.append("\n\t\t").append(message);
            }
        } else {
            sb.append(" No Messages");
        }
        return sb.toString();
    }

}

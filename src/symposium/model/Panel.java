package symposium.model;


import java.util.*;

public class Panel implements Comparable<Panel>{
    public final String NAME;
    public final Range AVAILABILITY;
    public final List<String> PANELISTS;
    public final List<String> CONSTRAINTS;
    public final List<String> CATEGORIES;

    public final boolean LOCKED;

    private VenueTime assignedVenueTime;
    private int difficulty;

    public Panel(String name, List<String> panelists, Range availability, List<String> categories, List<String> constraints){
        this.NAME = name;
        this.CATEGORIES = categories;
        this.AVAILABILITY = availability;

        // TODO : better way to pass new panelists
        List<String> people = new ArrayList<String>();
        int new_count = 0;
        for (String panelist : panelists){
            if (panelist.contains("n_")){
                panelist.replace("n_","");
                new_count += 1;
            }
            people.add(panelist);
        }
        if (new_count >= 2){
            constraints.add("new");
        }

        this.PANELISTS = people;
        this.CONSTRAINTS = constraints;

        //TODO set locked if minimum size is smaller than some number
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

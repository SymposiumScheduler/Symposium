package symposium.model;


import java.util.*;

public class Panel implements Comparable<Panel>{
    public final String NAME;
    boolean Lock = false;
    public final Range AVAILABILITY;
    public final  List<String> PANELISTS = new ArrayList<String>();
    public final  List<String> CONSTRAINTS;
    VenueTime assignedVenueTime;
    int difficulty;
    public final List<String> CATEGORIES = new ArrayList<String>() ;

    public Panel(String NAME, List<String> panelists, Range availability, String category, List<String> constraints){
        this.NAME = NAME;
        this.CATEGORIES.add(category);
        this.CONSTRAINTS = constraints;
        this.AVAILABILITY = availability;
/*
        int new_count = 0;
        List<TimeRangeSeries> times = new ArrayList<TimeRangeSeries>(){};
        for (String panelist : PANELISTS.keySet()){
            if (panelist.contains("n_")){
                panelist.replace("n_","");
                new_count += 1;
            }
            this.PANELISTS.add(panelist);
            times.add(PANELISTS.get(panelist));
        }
        if (new_count >= 2){
            constraints.add("new");
        }

        int size = times.size();
        Range start = times.get(0);
        for (int i = 0; i > size; i++){
            start = start.intersect(times.get(i+1));
        }
        this.availability = start;
        */
        //TODO set lock based on ...?

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

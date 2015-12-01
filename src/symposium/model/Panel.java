package symposium.model;

import symposium.model.Range;
import symposium.model.TimeRangeSeries;
import symposium.model.VenueTime;

import java.util.*;

public class Panel implements Comparable<Panel>{
    String name;
    boolean Lock = false;
    Range availability;
    List<String> panelists = new ArrayList<String>();
    List<String> constraints = new ArrayList<String>();
    VenueTime assignedVenueTime;
    int difficulty;
    String category;

    public Panel(String name, Map<String, TimeRangeSeries> panelists, String category, List<String> constraints){
        this.name = name;
        this.category = category;
        this.constraints = constraints;

        int new_count = 0;
        List<TimeRangeSeries> times = new ArrayList<TimeRangeSeries>(){};
        for (String panelist : panelists.keySet()){
            if (panelist.contains("n_")){
                panelist.replace("n_","");
                new_count += 1;
            }
            this.panelists.add(panelist);
            times.add(panelists.get(panelist));
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
        return name;
    }

    public Range getAvailability(){
        return availability;
    }

    @Override
    public int compareTo(Panel that) {
        return this.difficulty - that.difficulty;
    }
}

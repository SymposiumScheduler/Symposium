import java.util.List;

public class Venue {
    String name;
    int size;
    List<VenueTime> assignedTimes;
    List<VenueTime> freeTimes;

    public Venue(String name, int size, List<TimeRange> range){
        this.name = name;
        this.size = size;
        for (TimeRange times : range){
            freeTimes.add(new VenueTime(this, times));
        }
    }

    public List<VenueTime> getAssignedTimes(){
        return assignedTimes;
    }

    public List<VenueTime> getFreeTimes(){
        return freeTimes;
    }

    public void assignVenueTime(VenueTime venueTime){}

    public void freeVenueTime(VenueTime venueTime){}


}

package symposium.model;

import java.util.List;

public class Venue {
    public final String NAME;
    public final int SIZE;

    private final List<VenueTime> assignedVenueTimes;
    private final List<VenueTime> freeVenueTimes;

    public Venue(List<VenueTime> venueTimes) {}
    public void assign(VenueTime venuTime) {}
    public void free(VenueTime venueTime) {}

    public List<VenueTime> getAssignedVenueTimes(){}
    public List<VenueTime> getFreeVenueTimes(){}

    public void changeToAssigned(VenueTime venueTime){}
    public void changeToFree(VenueTime venueTime) {}
}
package symposium.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;

public class Venue implements Comparable<Venue>{

    public final String NAME;
    public final int SIZE;
    public final int PRIORITY;

    private final List<VenueTime> assignedVenueTimes;
    private final List<VenueTime> freeVenueTimes;

    public Venue(String name, int size, int priority, Collection<TimeRange> timeRanges) {
        this.NAME = name;
        this.SIZE = size;
        this.PRIORITY = priority;

        this.freeVenueTimes = new ArrayList<VenueTime>();

        if(timeRanges != null) {
            for(TimeRange tr : timeRanges) {
                this.freeVenueTimes.add(new VenueTime(tr,this));
            }
        }
        this.assignedVenueTimes = new ArrayList<VenueTime>();
    }

    public void changeToAssigned(VenueTime venueTime) {
        if(venueTime.VENUE == this) {
            assignedVenueTimes.add(venueTime);
            freeVenueTimes.remove(venueTime);
        }
    }

    public void changeToFree(VenueTime venueTime) {
        if(venueTime.VENUE == this) {
            assignedVenueTimes.remove(venueTime);
            freeVenueTimes.add(venueTime);
        }
    }

    @Override
    public int compareTo(Venue that) {
        return this.SIZE - that.SIZE;
    }

    /**
     * Returns information
     * Immutable version of the lists because manipulation of state is not allowed outside Venue class.
     * @return immutable List with the assignedVenueTimes in this venue.
     */
    public List<VenueTime> getAssignedVenueTimes(){
        return Collections.unmodifiableList(assignedVenueTimes);
    }

    /**
     * @return immutable List with the assignedVenueTimes in this venue.
     */
    public List<VenueTime> getFreeVenueTimes(){
        return Collections.unmodifiableList(freeVenueTimes);
    }
}

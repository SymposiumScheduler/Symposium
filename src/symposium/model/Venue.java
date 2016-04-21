package symposium.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;

/**
 * Venue stores its own object, its name, size, priority, and VenueTimes
 */
public class Venue implements Comparable<Venue>{

    public final String NAME;
    public final int SIZE;
    public final int PRIORITY;

    private final List<VenueTime> assignedVenueTimes;
    private final List<VenueTime> freeVenueTimes;

    /**
     * Constructs for Venue class
     *
     * Creates Venue objects, setting all of the variables to the given values, and convert the timeRange to VenueTime objects
     * @param name of the Venue
     * @param size of the Venue
     * @param priority from the input file. The higher the number, the more important it is to schedule a Panel in this Venue
     * @param timeRanges the available times in which this Venue is open. These are converted to VenueTime objects
     */
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

    /**
     * Switch a VenueTime to the assigned list, showing that it has a panel in it
     * Then remove it from the free list (because another panel cannot be assigned to it)
     * @param venueTime the venueTime to be changed
     */
    public void changeToAssigned(VenueTime venueTime) {
        if(venueTime.VENUE == this) {
            assignedVenueTimes.add(venueTime);
            freeVenueTimes.remove(venueTime);
        }
    }

    /**
     * Switch a VenueTime to the free list, showing that it no longer has a panel in it
     * Then remove it from the assigned list
     * @param venueTime the venueTime to be changed
     */
    public void changeToFree(VenueTime venueTime) {
        if(venueTime.VENUE == this) {
            assignedVenueTimes.remove(venueTime);
            freeVenueTimes.add(venueTime);
        }
    }

    /**
     * Compares Venues sizes so that they can be sorted in descending order.
     * @param OtherVenue the Venue that "this" Venue is being compared to
     * @return If the difference equals 0 return the difference of the PRIORITY, otherwise, return the diffrance of the size
     */
    @Override
    public int compareTo(Venue OtherVenue) {
        int sCompare = this.SIZE - OtherVenue.SIZE;
        if(sCompare != 0) {
            return sCompare;
        }

        return this.PRIORITY - OtherVenue.PRIORITY;
    }

    /**
     * Immutable version of the lists because manipulation of state is not allowed outside Venue class.
     * @return immutable List with the assignedVenueTimes in this venue.
     */
    public List<VenueTime> getAssignedVenueTimes(){
        return Collections.unmodifiableList(assignedVenueTimes);
    }

    /**
     * @return immutable List with the freeVenueTimes in this venue.
     */
    public List<VenueTime> getFreeVenueTimes(){
        return Collections.unmodifiableList(freeVenueTimes);
    }
}

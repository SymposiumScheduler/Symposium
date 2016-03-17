package symposium.model;

/**
 * This class is used to encapsulate time range information for venues, where each Venue object will have multiple
 * VenueTime objects assigned to it. When a panel is assigned to a timeslot, that is kept track of here
 * These are created when a Venue is
 */
public class VenueTime implements Comparable<VenueTime>{

    public final Range TIME;
    public final Venue VENUE;
    private Panel assignedPanel;

    public VenueTime(Range time, Venue venue) {
        this.TIME = time;
        this.VENUE = venue;
        this.assignedPanel = null;
    }

    /**
     * When a panel is placed into a time slot (VenueTime), we store that information here
     * @param panel the panel being placed
     */
    public void assignPanel(Panel panel){
        this.assignedPanel = panel;
    }

    /**
     * Check to see if there is a panel assigned in this VenueTIme
     * @return true/false
     */
    public boolean isAssigned() {
        return (assignedPanel != null);
    }

    /**
     * Check which panel is assigned to this VenueTime
     * @return assigned panel, or null if there isn't one
     */
    public Panel getAssignedPanel(){
        return this.assignedPanel;
    }

    /**
     * used to encapsulate needed information into a string
     * @return the name of the venue and this particular time range
     */
    public String toString() {
        return "Venue: "+ VENUE.NAME + ", Time: " + TimeFormat.absoluteToNormal(TIME);
    }

    /**
     * @return which day this VenueTime falls on by checking how many hours came before
     */
    public int getDay() {
        return TimeFormat.getNumberOfDay(TIME.getStart());
    }

    /**
     * Compares VenueTime objects of the same Venue in order to sort them by timeslot and size
     * They are sorted with small slots first, then normal. These are then sorted in chronological order
     * So the first VenueTime after the sort is the first small one that takes place from the given Venue
     * @param other we compare "this" VenueTime to the one given when the method is called. Ei, this.compareTo(other)
     * @return an integer that sort uses to determine order
     */
    @Override
    public int compareTo(VenueTime other) {
        // smaller venue comes first
        int vCompare = this.VENUE.compareTo(other.VENUE);
        if( vCompare != 0) {
            return vCompare;
        }
        // earlier time comes first
        return this.TIME.getStart() - other.TIME.getStart();
    }
}


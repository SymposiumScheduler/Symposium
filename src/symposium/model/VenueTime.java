package symposium.model;



public class VenueTime implements Comparable<VenueTime>{
    public final Range TIME;
    public final Venue VENUE;
    private Panel assignedPanel;

    public VenueTime(Range time, Venue venue) {
        this.TIME = time;
        this.VENUE = venue;
        this.assignedPanel = null;
    }

    public void assignPanel(Panel panel){

        this.assignedPanel = panel;
    }

    public boolean isAssigned() {

        return (assignedPanel != null);
    }

    public Panel getAssignedPanel(){

        return this.assignedPanel;
    }

    public String toString() {
        return "Venue: "+ VENUE.NAME + ", Time: " + TimeFormat.absoluteToNormal(TIME);
    }

    public int getDay() {
        return TimeFormat.getNumberOfDay(TIME.getStart());
    }

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


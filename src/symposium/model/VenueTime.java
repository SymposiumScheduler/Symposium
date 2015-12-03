package symposium.model;


public class VenueTime {
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
}

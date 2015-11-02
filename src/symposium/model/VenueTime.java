package symposium.model;


public class VenueTime {
    public final RangeLine TIME;
    public final Venue VENUE;
    private Panel assignedPanel;

    public VenueTime(RangeLine time, Venue venue) {
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

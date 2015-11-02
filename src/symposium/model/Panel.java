package symposium.model;


public class Panel extends Comparable<Panel>{
    public final String NAME;
    public final Constraint[] CONSTANTS;
    public final RangeLine AVAILABILITY;
    public final String[] PANELIST;

    private VenueTime assignedVenueTime = null;
    private int difficulity;

    public void setDifficulity(VenueTime vt){}
    public int getDifficulity() {}

    public int compare(Panel panel) {}

    //public boolean isAssigned() {}
    public VenueTime getAssignedVenueTime(){}
    public void assignVenueTime(VenueTime vt){}
}
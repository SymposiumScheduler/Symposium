package symposium.model;

public class NewPanelistConstraint extends TimeConstraint { //Ask team members

    public NewPanelistConstraint(ConstraintPriority priority, Panel p) {
        super(priority, p);
    }

    /**
     * Dependencies: Range interface, TimeRange class, Panel class, Panel.PANELIST variable
     * @return true if panel doesn't fall on monday OR there is one new panelist or for fewer, otherwise returns false
     */
    @Override
    boolean checkTime(VenueTime venueTime) {
        Range time = venueTime.TIME;
        Range firstDay = TimeFormat.getDayRange(0);
        if (!time.doesIntersect(firstDay)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "NewPanelistConstraint (Not all New Panelists on Monday)";
    }
}
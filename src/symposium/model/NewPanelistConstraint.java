package symposium.model;

import java.util.List;

public class NewPanelistConstraint extends TimeConstraint { //Ask team members
    private static List<String> panelist;

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
            panelist = this.PANEL.PANELISTS;
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("New Panelist Constraint Violated: (Not all New Panelists on Monday)").append("\n");
        sb.append("\t\t\t").append("Panelists are: ").append(panelist);

        return sb.toString();
    }
}
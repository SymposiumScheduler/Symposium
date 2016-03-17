package symposium.model;

import java.util.List;

/**
 * The class New Panelist Constraint inherits from TimeConstraint, @see TimeConstraint for documentation.
 * The class tests whether the constraint New Panelist is violated or not.
 * New Panelist constraint is violated when on Monday (or first day) all of the panelists of the panel are new
 *
 */

public class NewPanelistConstraint extends TimeConstraint { //Ask team members
    public static List<String> panelistsViolating;

    /**
     * Constructs for the NewPanelistConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     */

    public NewPanelistConstraint(ConstraintPriority priority, Panel panel) {
        super(priority, panel);
    }

    /**
     * <b>Dependencies:</b> Range interface, TimeRange class, Panel class, Panel.PANELIST variable
     *
     * The method tests if a panel has all new panelist on the first day.
     * When the method finds the panelists violating it records the panelists and assign it to panelistsViolating.
     *
     * @param venueTime The time being checked
     * @return boolean; False if panel falls on Monday and all panelists are new, otherwise returns true
     */
    @Override
    boolean checkTime(VenueTime venueTime) {
        Range time = venueTime.TIME;
        Range firstDay = TimeFormat.getDayRange(0);
        if (!time.doesIntersect(firstDay)) {
            return true;
        }
        else {
            panelistsViolating = this.PANEL.PANELISTS;
            return false;
        }
    }

    /**
     * ToString method for the New Panelist Constraint to be returned when violated.
     * The method specifies what is told when a constraint is violated.
     *
     * @return String of the violation message
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("New Panelist Constraint Violated: (Not all New Panelists on Monday)").append("\n");
        sb.append("\t\t\t").append("Panelists are: ").append(panelistsViolating);

        return sb.toString();
    }
}
package symposium.model;

import java.util.*;

/**
 * The class MaxPanelistConstraint inherits from Constraints, @see Constraint for documentation.
 * Max number of Panelists is violated when a panelist appears more than the set maximum number of times per day.
 *
 */
public class MaxPanelistConstraint extends Constraint {
    final int MAX;
    public static String panelistsViolating;

    /**
     * Constructs for the MaxPanelistConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     * @param max      The maximum number allowed for the panelist to appear per day
     */
    public MaxPanelistConstraint(ConstraintPriority priority, Panel panel, int max) {
        super(priority,panel);
        this.MAX = max;
    }

    /**
     * <b>Dependencies:</b> Panel class, ScheduleData class, VenueTime class
     *
     * The method loops through each panel's panelists and counts how many times they appear per day.
     * When the method finds the panelist violating it, record the panelist and assign it to panelistsViolating.
     *
     * @param venueTime The time being checked
     * @return boolean; If the panelists appears more times the MAX, return true, otherwise, return false.
     */
    public boolean isConstraintViolated(VenueTime venueTime) {
        int counter;
        for (String pist: PANEL.PANELISTS ) {
            counter = 1; // Reset and assume self is always assigned
            List<Panel> assigned = ScheduleData.instance().getPanelistAssignedPanels(pist);
            if (assigned != null) { // Guard condition against null pointers.
                for (Panel pl : assigned) {
                    if(pl == this.PANEL) {
                        continue; // Don't count self because it's assumed and already counted.
                    }
                    int day = pl.getVenueTime().getDay();
                    if (day == venueTime.getDay()) {
                        counter++;
                        if (counter > MAX) {
                            panelistsViolating = pist;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return String of the violation message
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Max Panelist Constraint Violated: (Panelist should only appear a maximum number of times per day)").append("\n");
        sb.append("\t\t\t").append("The Panelist is: ").append(panelistsViolating);


        return sb.toString();
    }
}

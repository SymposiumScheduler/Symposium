package symposium.model;

import java.util.*;

public class MaxPanelistConstraint extends Constraint {
    final int MAX;

    public MaxPanelistConstraint(ConstraintPriority priority, Panel p, int max) {
        super(priority,p);
        this.MAX = max;
    }

    /**
     * Dependencies: Panel class, ScheduleData class, VenueTime class
     * @param venueTime
     * @return bool true if the panel is scheduled more times per day than the maximum, false otherwise
     */
    public boolean isConstraintViolated(VenueTime venueTime) {
        int counter;
        for (String pist: PANEL.PANELISTS ) {
            counter = 1; // reset and assume self is always assigned
            List<Panel> assigned = ScheduleData.instance().getPanelistAssignedPanels(pist);
            if (assigned != null) { //Guard condition against null pointers.
                for (Panel pl : assigned) {
                    if(pl == this.PANEL) {
                        continue; // don't count self because it's assumed and already counted.
                    }

                    int day = pl.getVenueTime().getDay();
                    if (day == venueTime.getDay()) {
                        counter++;
                        if (counter > MAX) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "MaxPanelistConstraint (Panelist should only appear a maximum number of times per day)";
    }
}

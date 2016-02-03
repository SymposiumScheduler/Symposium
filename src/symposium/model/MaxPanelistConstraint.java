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
        boolean violated = false;
        for (String pist: PANEL.PANELISTS ) {
            int counter = 0;
            List<Panel> assigned = ScheduleData.instance().getPanelistAssignedPanels(pist);
            if (assigned != null) { //Guard condition against null pointers.
                for (Panel pl : assigned) {
                    int day = pl.getVenueTime().getDay();
                    if (day == venueTime.getDay()) {
                        counter++;
                        if (counter > MAX) {
                            violated = true;
                        }
                    }
                }
            }
        }
        return violated;
    }

    @Override
    public String toString() {
        return "MaxPanelistConstraint (Panelist should only appear a maximum number of times per day)";
    }
}

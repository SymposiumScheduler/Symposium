package symposium.model;

import java.util.*;

/**
 * Created by Roberto on 1/14/2016.
 */
public class MaxPanelistConstraint extends Constraint {
    final int MAX;

    public MaxPanelistConstraint(short priority, Panel p, int max) {
        super(priority,p);
        this.MAX = max;
    }

    public boolean isConstraintViolated(VenueTime venueTime) {
        int counter = 0;
        for (String pist: PANEL.PANELISTS ) {
            List<Panel> assigned = ScheduleData.instance().getPanelistAssignedPanels(pist);
            for (Panel pl: assigned) {
                int day = pl.getVenueTime().getDay();
                if (day == venueTime.getDay()) {
                    counter++;
                }
            }
        }
        if (counter > MAX) {
            return true;
        } else {
            return false;
        }
    }
}

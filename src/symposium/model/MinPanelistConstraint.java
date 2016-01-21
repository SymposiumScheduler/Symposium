package symposium.model;

import java.util.*;

/**
 * Created by Roberto on 1/14/2016.
 */
public class MinPanelistConstraint extends Constraint {
    int minimum;

    public MinPanelistConstraint(ConstraintPriority priority, Panel p) {
        super(priority,p);
        minimum = 1;
    }

    public boolean isConstraintViolated(VenueTime venueTime) {
        for (String panelist : PANEL.PANELISTS) {
            if (ScheduleData.instance().getPanelistAssignedPanels(panelist).size() < minimum) {
                for (Panel pan: ScheduleData.instance().getPanelistAssignedPanels(panelist)) {
                    if (venueTime.getDay() == pan.getVenueTime().getDay()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

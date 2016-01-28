package symposium.model;

import java.util.*;

public class MinPanelistConstraint extends Constraint {
    int minimum;

    public MinPanelistConstraint(ConstraintPriority priority, Panel p) {
        super(priority,p);
        minimum = 1;
    }

    /**
     * Kind of a poor man's estimate function; checks to make sure when scheduling panels that other panelist doesn't
     * appear on another panel on the same day.
     * AGAIN WORTH NOTING: This minPanelistConstraint is different from the true minPanelistConstraint.
     * @param venueTime
     * @return bool True if panel would be scheduled with panelist appears in another panel on same day, false otherwise.
     */
    public boolean isConstraintViolated(VenueTime venueTime) {
        for (String panelist : PANEL.PANELISTS) {
            if(ScheduleData.instance().getPanelistAssignedPanels(panelist) != null) {//Guard condition against null pointers.
                if (ScheduleData.instance().getPanelistAssignedPanels(panelist).size() < minimum) {
                    for (Panel pan : ScheduleData.instance().getPanelistAssignedPanels(panelist)) {
                        if (venueTime.getDay() == pan.getVenueTime().getDay()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}

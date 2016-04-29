package symposium.model;

import java.util.*;

/**
 * The class ConsecutivePanelsConstraint inherits from {@link symposium.model.Constraint}.
 * Consecutive Panels is violated when two panelists on some panel have 3 or more panels Consecutively
 */

public class ConsecutivePanelsConstraint extends Constraint {
    private static String panelistsViolating;

    /**
     * Constructs for the ConsecutivePanelsConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     */


    public ConsecutivePanelsConstraint(ConstraintPriority priority, Panel panel) {
        super(priority,panel);
    }

    /**
     *
     * The method loops through each panel panelists and for each it takes the unique days the panelists appears in and then
     * it counts the number of panels within the specified time difference between one panel and the next one in each of these days.
     * Doing that will count if the panelists is showing up for consecutive panels in a day.
     *
     * @param venueTime The time being checked by doesOverlap
     * @return boolean; If there is more than 2 consecutive panels that a panelist is in (including the given venueTime) return true.
     */

    public boolean isConstraintViolated(VenueTime venueTime) {
        int difference = 80; //time difference between two panels to be considered consecutive
        for(String pist: PANEL.PANELISTS){
            List<Range> panelsSameDay = new ArrayList<>();
            panelsSameDay.add(venueTime.TIME);
            if(ScheduleData.instance().getPanelistAssignedPanels(pist) == null){
                continue;
            }
            for(Panel panel: ScheduleData.instance().getPanelistAssignedPanels(pist)){
                if(!panel.equals(this.PANEL)) {
                    if (panel.getVenueTime().getDay() == venueTime.getDay()) {
                        panelsSameDay.add(panel.getVenueTime().TIME);
                    }
                }
            }

            if(panelsSameDay.size()<3){
                continue;
            }

            panelsSameDay.sort(Comparator.<Range>naturalOrder());
            int ctr = 0;
            for(int i = 1; i < panelsSameDay.size(); i++){
                int diffrenceStartEnd = panelsSameDay.get(i).getStart() - panelsSameDay.get(i-1).getEnd();
                if(diffrenceStartEnd < difference){
                    ctr++;
                }
                if(diffrenceStartEnd >= difference) {
                    ctr = 0;
                }
                if(ctr > 3 ) {
                    panelistsViolating = pist;
                    return true;
                }
            }

            // if there is more than 1 consecutive panels return true
            if(ctr > 1){
                panelistsViolating = pist;
                return true;
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
        sb.append("Consecutive Panels Constraint is violated: A panelist is in 3 consecutive panels ").append("\n");
        sb.append("\t\t\t").append("The Panelists is: ").append(panelistsViolating);


        return sb.toString();
    }

}

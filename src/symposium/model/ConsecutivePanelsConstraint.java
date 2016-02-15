package symposium.model;

import java.util.*;

public class ConsecutivePanelsConstraint extends Constraint {

    public ConsecutivePanelsConstraint(ConstraintPriority priority, Panel p) {
        super(priority,p);
    }
    /**
     * @param venueTime The time being checked by doesOverlap
     * @return If there is more than 2 consecutive panels that a panelist is in (including the given venueTime) return true.
     */
    public boolean isConstraintViolated(VenueTime venueTime) {
        //time difference between two panels to be considered consecutive
        int difference = 80;
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
            for(int i = 1; i<panelsSameDay.size(); i++){
                int diffrenceStartEnd = panelsSameDay.get(i).getStart() - panelsSameDay.get(i-1).getEnd();
                if(diffrenceStartEnd < difference){
                    ctr++;
                }
            }

            // if there is more than 1 consecutive panels return true
            if(ctr > 1){
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return "ConsecutivePanelsConstraint: priority = "+ PRIORITY;
    }

}

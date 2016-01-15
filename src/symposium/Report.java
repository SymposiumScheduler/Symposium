package symposium;

import symposium.model.Constraint;
import symposium.model.Panel;
import symposium.model.ScheduleData;

import java.util.ArrayList;
import java.util.List;
// TODO : better implementation for this.
public class Report {
    public static final Report INSTANCE = new Report();

    private final List<Panel> unschedulablePanels;
    private final List<String> unschedulablePanelMessages;
    private String lastSchedule;

    private Report(){
        unschedulablePanels = new ArrayList<Panel>();
        unschedulablePanelMessages = new ArrayList<String>();
        lastSchedule = "";
    }

    public void cannotSchedule(Panel panel) {
        this.cannotSchedule(panel, "No Message");
    }
    public void finishReport() {
        // print final schedule
        // count and print violations
        for (Panel p : ScheduleData.instance().getAssignedPanels()) {
            lastSchedule += p.NAME + "\n\t" + p.getVenueTime() + "\n";
        }
    }
    public String toString() {
        String result = lastSchedule + "\n";
        for(int i = 0;  i < unschedulablePanels.size() ; i++) {
            result += unschedulablePanels.get(i).NAME + " cannot be scheduled : " + unschedulablePanelMessages.get(i) + "\n";
        }
        return result;
    }

    public void cannotSchedule(Panel p, String msg) {
        unschedulablePanels.add(p);
        unschedulablePanelMessages.add(msg);
    }

    public void reset() {
        this.unschedulablePanelMessages.clear();
        this.unschedulablePanels.clear();

    }
}
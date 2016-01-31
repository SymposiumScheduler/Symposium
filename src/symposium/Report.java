package symposium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import symposium.model.*;
import java.util.*;

public class Report {
    public static final Report INSTANCE = new Report();
    public static Map<String, Integer> errorRecord = new HashMap<String, Integer>();

    public String toString() {
        StringBuilder result = new StringBuilder();
        for(Panel panel : ScheduleData.instance().getAssignedPanels()) {
            result.append("\n\n").append(panel.NAME).append("\n\t").append(panel.getVenueTime().toString());
        }
        result.append("\n");
        for(Panel panel : ScheduleData.instance().getUnschedulablePanels().keySet()) {
            result.append("\n").append(panel.NAME).append(" cannot be scheduled : ")
                    .append(ScheduleData.instance().getUnschedulablePanels().get(panel));
        }
        return result.toString();
    }
    public JSONObject toJson(){
        JSONObject root = new JSONObject();
        // scheduled Panels
        JSONArray scheduled = new JSONArray();
        for(Panel panel : ScheduleData.instance().getAssignedPanels()) {
            JSONObject panelObj = new JSONObject();
            panelObj.put("panel", panel.NAME);
            panelObj.put("Venue", panel.getVenueTime().VENUE.NAME);
            panelObj.put("Time", TimeFormat.absoluteToNormal(panel.getVenueTime().TIME));
            scheduled.add(panelObj);
        }
        root.put("scheduled", scheduled);

        // unscheduled Panels
        JSONArray unscheduled = new JSONArray();
        for(Panel panel : ScheduleData.instance().getUnschedulablePanels().keySet()) {
            JSONObject panelObj = new JSONObject();
            panelObj.put("panel", panel.NAME);
            panelObj.put("message", ScheduleData.instance().getUnschedulablePanels().get(panel));
            unscheduled.add(panelObj);
        }
        root.put("unscheduled", unscheduled);

        return root;
    }
}
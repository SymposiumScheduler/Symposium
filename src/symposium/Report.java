package symposium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import symposium.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO : better implementation for this. Make sure to include the percentage of panels scheduled at the end of the report
public class Report {
    public static final Report INSTANCE = new Report();

    private final Map<Panel, String> unscheduledPanels;


    private Report(){
        unscheduledPanels = new HashMap<Panel, String>();
    }

    public void cannotSchedule(Panel panel) {
        this.cannotSchedule(panel, "No Message");
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for(Panel panel : ScheduleData.instance().getAssignedPanels()) {
            result.append("\n").append(panel.NAME).append("\n\t").append(panel.getVenueTime().toString());
        }
        result.append("\n");
        for(Panel panel : unscheduledPanels.keySet()) {
            result.append("\n").append(panel.NAME).append(" cannot be scheduled : ").append(unscheduledPanels.get(panel));
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
        for(Panel panel : this.unscheduledPanels.keySet()) {
            JSONObject panelObj = new JSONObject();
            panelObj.put("panel", panel.NAME);
            panelObj.put("message", unscheduledPanels.get(panel));
            unscheduled.add(panelObj);
        }
        root.put("unscheduled", unscheduled);

        return root;
    }


    public void cannotSchedule(Panel p, String msg) {
        unscheduledPanels.put(p,msg);
    }

    public void reset() {
        this.unscheduledPanels.clear();
    }
}
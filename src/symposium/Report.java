package symposium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import symposium.model.*;
import java.util.*;

public class Report {
    public static final Report INSTANCE = new Report();

    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("\n\n------------------------------ Scheduled ------------------------------");
        for(Panel panel : ScheduleData.instance().getAssignedPanels()) {
            result.append("\n\n").append(panel.toString());
        }

        result.append("\n\n------------------------------ Unscheduled ----------------------------");
        for(Panel panel : ScheduleData.instance().getUnschedulablePanels()) {
            result.append("\n\n").append(panel.toString());
        }

        result.append("\n\n---------------------------- Underutilized Panelists ---------------------------");
        Map<String, Set<Integer>> freePanelists = ScheduleData.instance().getPanelistsUnscheduledDays();
        int counter = 0;
        int total = 0;
        for(String pnst : freePanelists.keySet()) {
            counter++;
            total += freePanelists.get(pnst).size();
            result.append("\n").append(counter).append(") ").append(pnst).append(" in days ").append(freePanelists.get(pnst));
        }
        result.append("\n\nTotal Panelist×Day violations: ").append(total);


        result.append("\n\n---------------------------- Messages ---------------------------");
        List<String> messages =  ScheduleData.instance().getWarningMessages();
        if(messages.isEmpty()) {
            result.append("\nNo Messages");
        }
        else {
            for(String mes : messages) {
                result.append("\n").append(mes);
            }

        }

        return result.toString();
    }

    public JSONObject toJson(){
        JSONObject root = new JSONObject();
        // scheduled Panels
        JSONArray scheduled = new JSONArray();
        for(Panel panel : ScheduleData.instance().getAssignedPanels()) {
            JSONObject panelObj = new JSONObject();
            panelObj.put("Panel", panel.NAME);
            panelObj.put("Venue", panel.getVenueTime().VENUE.NAME);
            panelObj.put("Time", TimeFormat.absoluteToNormal(panel.getVenueTime().TIME));
            panelObj.put("Messages", panel.getMessages());
            scheduled.add(panelObj);
        }
        root.put("Scheduled", scheduled);

        // unscheduled Panels
        JSONArray unscheduled = new JSONArray();
        for(Panel panel : ScheduleData.instance().getUnschedulablePanels()) {
            JSONObject panelObj = new JSONObject();
            panelObj.put("Panel", panel.NAME);
            panelObj.put("Messages", panel.getMessages());
            unscheduled.add(panelObj);
        }
        root.put("Unscheduled", unscheduled);

        // Free Panelists
        JSONObject minViolations = new JSONObject();
        Map<String, Set<Integer>> freePanelists = ScheduleData.instance().getPanelistsUnscheduledDays();
        for(String pnst : freePanelists.keySet()) {
            JSONArray pnstDays = new JSONArray();
            pnstDays.addAll(freePanelists.get(pnst));
            minViolations.put(pnst, pnstDays);
        }
        root.put("Underutilized_Panelists", minViolations);

        // Warning Messages
        JSONArray warningMessages = new JSONArray();
        warningMessages.addAll(ScheduleData.instance().getWarningMessages());
        root.put("Messages", warningMessages);

        return root;
    }
}
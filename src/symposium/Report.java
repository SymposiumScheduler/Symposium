package symposium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import symposium.model.*;
import java.util.*;

/**
 * The class Report is the class responsible for printing a report about the schedule.
 * The class includes two methods toString() and toJson() each printing the report in a specific format.
 */


public class Report {
    public static final Report INSTANCE = new Report();

    /**
     * This method is used to print a full report of the schedule. The report includes the Scheduled panels, the Unscheduled panels,
     * a full list of Underutilized Panelists, which are panelists not scheduled in certain days. As well as, the messages of the schedule.
     * The messages could be understood as errors, which include, but are not limited to, anything that went wrong or needs to be reported to the user,
     * for example, a panel that had no Panelists will be skipped in the scheduling process and the user will be told of that panel.
     *
     * The method uses a String Builder in order to set the objects in heaps, each heap will contain the corresponding segment of the report.
     *
     * The method takes no parameters. However it uses the panels and panelists.
     *
     * @return String report of the schedule.
     */

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
            result.append("\n\n").append(counter).append(") ").append(pnst).append(" in days ").append(freePanelists.get(pnst));
        }
        result.append("\n\nTotal Panelist×Day violations: ").append(total);


        result.append("\n\n---------------------------- Messages ---------------------------");
        for(String mes : ScheduleData.instance().getWarningMessages()) {
            result.append("\n").append(mes);
        }

        return result.toString();
    }

    /**
     * Similar to the previous method. This method is used to print a full report of the schedule. The report includes the Scheduled panels, the Unscheduled panels,
     * a full list of Underutilized Panelists, which are panelists not scheduled in certain days. As well as, the messages of the schedule.
     * The messages could be understood as errors, which include, but are not limited to, anything that went wrong or needs to be reported to the user,
     * for example, a panel that had no Panelists will be skipped in the scheduling process and the user will be told of that panel.
     *
     * The difference is this method uses JSONObject to report instead of string, it creates arrays each holding the corresponding segment.
     *
     * The method takes no parameters. However it uses the panels and panelists.
     *
     * @return JSONObject report of the schedule.
     */

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
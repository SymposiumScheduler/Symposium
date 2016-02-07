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

        result.append("\n\n").append(percentages()).append("\n");

        return result.toString();
    }

    public StringBuilder percentages() {
        StringBuilder constraintCount = new StringBuilder();
        Map<String, Integer> violationMap = new HashMap();
        violationMap.put("New-Panelist Constraint", 0);
        violationMap.put("Paired-Panelist Constraint", 0);
        violationMap.put("Single-Category Constraint", 0);
        violationMap.put("Venue Constraint", 0);
        violationMap.put("Max-Panels Constraint", 0);
        violationMap.put("Min-Panels Constraint", 0);
        violationMap.put("Minimum-Capacity Constraint", 0);
        violationMap.put("Specific Time Constraint", 0);

        constraintCount.append("\n------------------------------ Report For Scheduled ------------------------------");

        for (Panel panel : ScheduleData.instance().getAssignedPanels()) {
            for (Constraint constraint : panel.CONSTRAINTS) {
                if (constraint.isConstraintViolated(panel.getVenueTime())) {
                    if (constraint instanceof NewPanelistConstraint) {
                        violationMap.put("New-Panelist Constraint", violationMap.get("New-Panelist Constraint")+1);
                    }
                    if (constraint instanceof PairedPanelistConstraint) {
                        violationMap.put("Paired-Panelist Constraint", violationMap.get("Paired-Panelist Constraint")+1);

                    }
                    if (constraint instanceof CategoryConstraint) {
                        violationMap.put("Single-Category Constraint", violationMap.get("Single-Category Constraint")+1);
                    }
                    if (constraint instanceof VenueConstraint) {
                        violationMap.put("Venue Constraint", violationMap.get("Venue Constraint")+1);
                    }
                    if (constraint instanceof MaxPanelistConstraint) {
                        violationMap.put("Max-Panels Constraint", violationMap.get("Max-Panels Constraint"+1));
                    }
                    if (constraint instanceof MinPanelistConstraint) {
                        violationMap.put("Min-Panels Constraint", violationMap.get("Min-Panels Constraint")+1);
                    }
                    if (constraint instanceof SizeConstraint) {
                        violationMap.put("Minimum-Capacity Constraint", violationMap.get("Minimum-Capacity Constraint")+1);
                    }
                    if (constraint instanceof SpecificTimeConstraint) {
                        violationMap.put("Specific Time Constraint", violationMap.get("Specific Time Constraint")+1);
                    }

                }
            }

        }

        int scheduled = ScheduleData.instance().getAssignedPanels().size();
        int unscheduled = ScheduleData.instance().getUnschedulablePanels().size();
        int total = scheduled + unscheduled;
        float percent = (float) scheduled/(float) total;
        constraintCount.append("\n\n").append(percent + "% of panels scheduled (" + scheduled + " out of " + total + ")");

        for (String key : violationMap.keySet()) {
            constraintCount.append("\n\n").append(key + " violated " + violationMap.get(key) + " times");
        }

        return constraintCount;
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
            panelObj.put("Messages", panel.getMessages());
            scheduled.add(panelObj);
        }
        root.put("scheduled", scheduled);

        // unscheduled Panels
        JSONArray unscheduled = new JSONArray();
        for(Panel panel : ScheduleData.instance().getUnschedulablePanels()) {
            JSONObject panelObj = new JSONObject();
            panelObj.put("panel", panel.NAME);
            panelObj.put("Messages", panel.getMessages());
            unscheduled.add(panelObj);
        }
        root.put("unscheduled", unscheduled);

        return root;
    }
}
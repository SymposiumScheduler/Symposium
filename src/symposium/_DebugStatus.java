package symposium;

import symposium.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is meant for our use to look into the bugs
 */
public class _DebugStatus {

    public static String __debugStats() {
        StringBuilder statusReport = new StringBuilder();
        statusReport.append(ScheduledStatus().toString());

        return statusReport.toString();
    }

    private static String ScheduledStatus() {
        StringBuilder statusReport = new StringBuilder();
        Map<String, Integer> violationMap = new HashMap();
        violationMap.put("New-Panelist Constraint", 0);
        violationMap.put("Paired-Panelist Constraint", 0);
        violationMap.put("Single-Category Constraint", 0);
        violationMap.put("Venue Constraint", 0);
        violationMap.put("Max-Panels Constraint", 0);
        violationMap.put("Minimum-Capacity Constraint", 0);
        violationMap.put("Specific Time Constraint", 0);
        violationMap.put("Consecutive Panels Constraint", 0);

        int countDurationViolations_50 = 0;
        int countDurationViolations_80 = 0;

        statusReport.append("\n------------------------------ Debug Stats ------------------------------ ");

        statusReport.append("\n\n------------------------------ Violations for The Constraints ------------------------------ \n");

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
                    if (constraint instanceof VenueFilter) {
                        violationMap.put("Venue Constraint", violationMap.get("Venue Constraint")+1);
                    }
                    if (constraint instanceof MaxPanelistConstraint) {
                        violationMap.put("Max-Panels Constraint", violationMap.get("Max-Panels Constraint"+1));
                    }
                    if (constraint instanceof SizeConstraint) {
                        violationMap.put("Minimum-Capacity Constraint", violationMap.get("Minimum-Capacity Constraint")+1);
                    }
                    if (constraint instanceof SpecificTimeFilter) {
                        violationMap.put("Specific Time Constraint", violationMap.get("Specific Time Constraint")+1);
                    }
                    if (constraint instanceof ConsecutivePanelsConstraint) {
                        violationMap.put("Consecutive Panels Constraint", violationMap.get("Consecutive Panels Constraint")+1);
                    }

                }
                // Calculate Violations for Duration Filter
                if((panel.PANELISTS.size() <= 2) && TimeFormat.withinError(panel.getVenueTime().TIME.length(), 80, 1)) {
                    countDurationViolations_80++;
                }
                else if((panel.PANELISTS.size() > 2) && TimeFormat.withinError(panel.getVenueTime().TIME.length(), 50, 1)) {
                    countDurationViolations_50++;
                }

            }
        }

        // Calculate % of scheduled
        int scheduled = ScheduleData.instance().getAssignedPanels().size();
        int unscheduled = ScheduleData.instance().getUnschedulablePanels().size();
        int total = scheduled + unscheduled;
        float percent = 100*((float) scheduled/(float) total);
        statusReport.append("\n\n").append(percent + "% of Panels Scheduled (" + scheduled + " out of " + total + ")");

        // Calculate Total Violations for constraints
        int totalViolations = 0;
        for (String key : violationMap.keySet()) {
            if(violationMap.get(key) == null) {
                continue;
            }
            statusReport.append("\n\n").append(key + " Violated " + violationMap.get(key) + " Times");
            totalViolations += violationMap.get(key);
        }

        statusReport.append("\n\n").append("Total of Violations: " + totalViolations);


        // Calculate Total Violations for min-panelists
        int countPanelists = 0;
        Map<String, Set<Integer>> freePanelists = ScheduleData.instance().getPanelistsUnscheduledDays();
        for(String pnst : freePanelists.keySet()) {
            countPanelists += freePanelists.get(pnst).size();
        }

        // Calculate Violations for preferred Filter
        int countPrefVenueViolations = 0;
        for(Venue v : ScheduleData.instance().VENUES) {
            if(v.PRIORITY < 10) {
                for (VenueTime vt : v.getFreeVenueTimes()) {
                    countPrefVenueViolations++;

                }
            }

        }

        statusReport.append("\n\n------------------------------ Violations for The Filters ------------------------------ \n");

        statusReport.append("\n").append("Violations for Min-Panelists: ").append(countPanelists);
        statusReport.append("\n").append("Violations for Duration_80: ").append(countDurationViolations_80);
        statusReport.append("\n").append("Violations for Duration_50: ").append(countDurationViolations_50);
        statusReport.append("\n").append("Violations for Preferred Venues: ").append(countPrefVenueViolations);


        return statusReport.toString();
    }

    // ToDo: Make this an analysis of why panels are unscheduled
    private static String UnscheduledStatus() {
        StringBuilder statusReport = new StringBuilder();
        Map<String, Integer> violationMap = new HashMap();
        violationMap.put("Venue Constraint", 0);
        violationMap.put("Max-Panels Constraint", 0);
        violationMap.put("Specific Time Constraint", 0);

        statusReport.append("\n------------------------------ Debug Stats for Unscheduled  ------------------------------ ");

        statusReport.append("\n\n------------------------------ Violations for The Constraints ------------------------------ \n");

        for (Panel panel : ScheduleData.instance().getUnschedulablePanels()) {
            if(panel == null) {
                continue;
            }
            for (Constraint constraint : panel.CONSTRAINTS) {
                if(constraint == null) {
                    continue;
                }
                if (constraint instanceof VenueFilter) {
                    violationMap.put("Venue Constraint", violationMap.get("Venue Constraint")+1);
                }
                if (constraint instanceof MaxPanelistConstraint) {
                    violationMap.put("Max-Panels Constraint", violationMap.get("Max-Panels Constraint"+1));
                }
                if (constraint instanceof SpecificTimeFilter) {
                    violationMap.put("Specific Time Constraint", violationMap.get("Specific Time Constraint")+1);
                }
            }
        }


        return statusReport.toString();
    }

}


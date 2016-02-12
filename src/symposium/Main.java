package symposium;

import symposium.model.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {
	    // Reading parsing json files
        //long initTime = System.nanoTime();
        final String INPUT_FILE = "datas/data.txt";
        Parser.parse(INPUT_FILE);
        // Schedule data is initiated

        DummyScheduler bs = new DummyScheduler();
        bs.makeSchedule();
        //long elapsedTime = System.nanoTime() - initTime;

        // Print report
        System.out.println(Report.INSTANCE);
        System.out.println(__debugStats());
        //System.out.println("\n\n\nTIME= " + (elapsedTime/(double)1000000000) + " ± 0.05 s");
    }

    private static String __debugStats() {
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
        violationMap.put("Consecutive Panels Constraint", 0);

        constraintCount.append("\n------------------------------ Debug Stats ------------------------------");

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
                    /*if (constraint instanceof MinPanelistConstraint) {
                        violationMap.put("Min-Panels Constraint", violationMap.get("Min-Panels Constraint")+1);
                    }*/
                    if (constraint instanceof SizeConstraint) {
                        violationMap.put("Minimum-Capacity Constraint", violationMap.get("Minimum-Capacity Constraint")+1);
                    }
                    if (constraint instanceof SpecificTimeConstraint) {
                        violationMap.put("Specific Time Constraint", violationMap.get("Specific Time Constraint")+1);
                    }
                    if (constraint instanceof ConsecutivePanelsConstraint) {
                        violationMap.put("Consecutive Panels Constraint", violationMap.get("Consecutive Panels Constraint")+1);
                    }

                }
            }
        }

        int scheduled = ScheduleData.instance().getAssignedPanels().size();
        int unscheduled = ScheduleData.instance().getUnschedulablePanels().size();
        int total = scheduled + unscheduled;
        float percent = 100*((float) scheduled/(float) total);
        constraintCount.append("\n\n").append(percent + "% of panels scheduled (" + scheduled + " out of " + total + ")");

        int totalViolations = 0;

        for (String key : violationMap.keySet()) {
            constraintCount.append("\n\n").append(key + " violated " + violationMap.get(key) + " times");
            totalViolations += violationMap.get(key);
        }

        constraintCount.append("\n\n").append(totalViolations + " Total Number of Violations ");


        return constraintCount.toString();
    }
}

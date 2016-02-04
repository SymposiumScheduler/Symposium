package symposium.model;

import java.util.ArrayList;
import java.util.List;

public class ConstraintFactory{
    public static List<Constraint> buildConstraints(Panel panel, List<String> constraint_strings) {
        List<Constraint> constraints = new ArrayList<Constraint>();
        // assumed constraints
        constraints.add(new PanelistConstraint(ConstraintPriority.REQUIRED, panel));
        // input constraints
        for (String constraint_string : constraint_strings) {
            if (constraint_string.contains("New-Panelist")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                Constraint constraint = new NewPanelistConstraint(intToPriority(priority), panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Paired-Panelist")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                Constraint constraint = new PairedPanelistConstraint(intToPriority(priority), panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Single-Category")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                Constraint constraint = new CategoryConstraint(intToPriority(priority), panel);
                constraints.add(constraint);
            }

            else if (constraint_string.contains("Venue")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                String venueS = constraint_string.split("\\(")[1].split("\\)")[0];

                List<Venue> venueList = ScheduleData.instance().VENUES;
                Venue venue = null;
                for (Venue v: venueList) {
                    if (v.NAME.equals(venueS)) {
                        venue = v;
                        break;
                    }
                }
                if (venue == null) {
                    throw new RuntimeException("VenueConstraint for " + panel.NAME + " cannot be created: No such Venue " + venueS + " exists in input.");
                }
                Constraint constraint = new VenueConstraint(intToPriority(priority), panel, venue);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Max-Panels")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                int maxPanels = Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                Constraint constraint = new MaxPanelistConstraint(intToPriority(priority), panel, maxPanels);
                constraints.add(constraint);
            }
            //FIXME: Min-Panels needs to be implemented.
            else if (constraint_string.contains("Min-Panels")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                int minimum = Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                //Always assigns Desired priority, because we're using our fake-up estimation.
                Constraint constraint = new MinPanelistConstraint(ConstraintPriority.VERY_IMPORTANT, panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Minimum-Capacity")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                int minimum = Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                Constraint constraint = new SizeConstraint(intToPriority(priority), panel, minimum);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Availability")) {
                // TODO : Availability does not have a constraint. It's always assumed. REMOVE FROM DATA FILE
            }
            else if (constraint_string.contains("Time(")) {
                String timeOfDay = constraint_string.split("\\(")[1];
                int day = Integer.valueOf(timeOfDay.split(";")[0]);
                int hour = Integer.valueOf((timeOfDay.split(";")[1]).split(":")[0]);
                int minute = Integer.valueOf((timeOfDay.split(";")[1]).split(":")[1].split("\\)")[0]);
                int timePoint = TimeFormat.timeComponentsToAbsolute(day,hour,minute);
                int priority = Integer.valueOf(constraint_string.split("\\):")[1]);
                Constraint constraint = new SpecificTimeConstraint(intToPriority(priority), panel, timePoint);
                constraints.add(constraint);
            }
            else {
                System.err.println(constraint_string + " Constraint is not implemented ");
            }
        }

        return constraints;
    }
    private static ConstraintPriority intToPriority(int level) {
        switch (level) {
            case 1 :
                return ConstraintPriority.REQUIRED;
            case 2 :
                return ConstraintPriority.VERY_IMPORTANT;
            case 3 :
                return ConstraintPriority.DESIRED;
            default :
                return ConstraintPriority.DESIRED; // assume lowest priority for other values
        }
    }
}

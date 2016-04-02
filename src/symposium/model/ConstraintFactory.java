package symposium.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ConstraintFactory creates constraints objects using their predetermined priority number
 */

public class ConstraintFactory{
    /**
     * This is our main factory method, which is called from Panel. Each time this is called, a list of constraint names (string) is
     * sent in. For each string, we create an actual constraint object using the predetermined priority number
     * Once the method has looped over all constraints, it returns a list of the constraint objects for the Panel to store
     * @param panel is the panel that is calling this method, given because most constraints require their assigned panel to instantiate
     * @param constraint_strings is a list of names corresponding to constraint objects
     * @return a list of constraint objects for the panel that called this method
     */
    public static List<Constraint> buildConstraints(Panel panel, List<String> constraint_strings) {
        List<Constraint> constraints = new ArrayList<Constraint>();
        // assumed constraints, these are given for all panels
        constraints.add(new PanelistConstraint(ConstraintPriority.REQUIRED, panel));
        constraints.add(new ConsecutivePanelsConstraint(ConstraintPriority.DESIRED, panel));
        constraints.add(new PreferredVenuesFilter(panel));
        constraints.add(new VenueTimeDurationFilter(panel));

        // input constraints, from the input file
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
                    ScheduleData.instance().addWarningMessage("VenueFilter for " + panel.NAME + " cannot be created: No such Venue " + venueS + " exists in input.");
                    continue;
                }
                Constraint constraint = new VenueFilter(intToPriority(priority), panel, venue);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Max-Panels")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                int maxPanels = Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                Constraint constraint = new MaxPanelistConstraint(intToPriority(priority), panel, maxPanels);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Min-Panels")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                constraints.add(new MinPanelsFilter(intToPriority(priority), panel));
            }
            else if (constraint_string.contains("Minimum-Capacity")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                int minimum = Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                Constraint constraint = new SizeConstraint(intToPriority(priority), panel, minimum);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Availability")) {
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                constraints.add(new AvailabilityFilter(intToPriority(priority), panel));
            }
            else if (constraint_string.contains("Time(")) {
                String timeOfDay = constraint_string.split("\\(")[1];
                int day = Integer.valueOf(timeOfDay.split(";")[0]);
                int hour = Integer.valueOf((timeOfDay.split(";")[1]).split(":")[0]);
                int minute = Integer.valueOf((timeOfDay.split(";")[1]).split(":")[1].split("\\)")[0]);
                int timePoint = TimeFormat.timeComponentsToAbsolute(day,hour,minute);
                int priority = Integer.valueOf(constraint_string.split("\\):")[1]);
                Constraint constraint = new SpecificTimeFilter(intToPriority(priority), panel, timePoint);
                constraints.add(constraint);
            }
            else { // Constraint not implemented in the factory.
                ScheduleData.instance().addWarningMessage("panel : " + panel.NAME + " has a constraint " + constraint_string + " which is not Known");
            }
        }

        Collections.sort(constraints, new Comparator<Constraint>() {
            @Override
            public int compare(Constraint c, Constraint other) {
                // TODO : FIXME : better implementation
                return -1*c.PRIORITY.compareTo(other.PRIORITY);
            }
        });
        return constraints;
    }

    /**
     * Helper method to simply converts an integer value to a priority type (REQUIRED, VERY_IMPORTANT, or DESIRED.)
     * @param level an integer between 1-3
     * @return the corresponding priority type (or DESIRED if the given number doesn't map to a priority type)
     */
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

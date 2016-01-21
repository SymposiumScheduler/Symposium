package symposium.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;

//TODO we need venue-constaint (need to build venue somehow), duration-constraint (need to see how this is represented in input)
public class ConstraintFactory{
    public static List<Constraint> buildConstraints(Panel panel, List<String> constraint_strings){
        List<Constraint> constraints = new ArrayList<Constraint>();
        for (String constraint_string : constraint_strings){
            if (constraint_string.contains("New-Panelist")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                Constraint constraint = new NewPanelistConstraint(intToPriority(priority), panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Paired-Panelist")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                Constraint constraint = new PairedPanelistConstraint(intToPriority(priority), panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Single-Category")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                Constraint constraint = new CategoryConstraint(intToPriority(priority), panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Minimum-Capacity")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                int size =  Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                short s = (short) size;
                Constraint constraint = new SizeConstraint(intToPriority(priority), panel, s);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Panelist-Constraint")){
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
                    if (v.NAME == venueS) {
                        venue = v;
                        break;
                    }
                }
                Constraint constraint = new VenueConstraint(intToPriority(priority), panel, venue);
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

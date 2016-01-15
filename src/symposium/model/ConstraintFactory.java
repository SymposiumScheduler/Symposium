package symposium.model;

import java.util.ArrayList;
import java.util.List;

public class ConstraintFactory{
    public static List<Constraint> buildConstraints(Panel panel, List<String> constraint_strings){
        List<Constraint> constraints = new ArrayList<Constraint>();
        for (String constraint_string : constraint_strings){
            if (constraint_string.contains("New-Panelist")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                short ps = (short) priority;
                Constraint constraint = new NewPanelistConstraint(ps, panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Paired-Panelist")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                short ps = (short) priority;
                Constraint constraint = new PairedPanelistConstraint(ps, panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Single-Category")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                short ps = (short) priority;
                Constraint constraint = new CategoryConstraint(ps, panel);
                constraints.add(constraint);
            }
            else if (constraint_string.contains("Minimum-Capacity")){
                int priority = Integer.valueOf(constraint_string.split(":")[1]);
                short ps = (short) priority;
                int size =  Integer.valueOf(constraint_string.split("\\(")[1].split("\\)")[0]);
                short s = (short) size;
                Constraint constraint = new SizeConstraint(ps, panel, s);
                constraints.add(constraint);
            }
        }
        return constraints;
    }
}

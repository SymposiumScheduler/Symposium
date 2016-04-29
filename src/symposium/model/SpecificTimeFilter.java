package symposium.model;
import java.util.*;

/**
 * SpecificTimeFilter inherits from {@link symposium.model.Filter Filter}.
 * SpecificTimeFilter is a Filter removing all venueTime's not matching the time given in the constructor.
 *
 */
public class SpecificTimeFilter extends Filter {
    public final int TIME;

    /**
    * Constructs for the SpecificTimeFilter class.
    *
    * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
    * @param panel    The Panel that the constraint is part of.
    * @param time     The wanted time
    */
    public SpecificTimeFilter(ConstraintPriority priority, Panel panel, int time) {
        super(priority, panel);
        TIME = time;
    }

    /**
     * Any time of venueTime not matching TIME will be removed.
     * @param vtScoreMap A map from venueTime to score
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        Set<VenueTime> keys = new HashSet<>(vtScoreMap.keySet());
        for (VenueTime vt : keys) {
            if(isConstraintViolated(vt)){
                if(!PANEL.LOCKED){
                    if(!requiredViolationMap.containsKey(this)){
                        requiredViolationMap.put(this, 1);
                    } else{
                        requiredViolationMap.put(this, requiredViolationMap.get(this)+1);
                    }
                }
                vtScoreMap.remove(vt);
            }
        }
    }

    /**
     * @param venueTime
     * @return If the panel is not assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return (!TimeFormat.withinError(venueTime.TIME.getStart(), TIME, 1));
    }

    /**
     * @return String of the violation message and the PRIORITY
     */
    @Override
    public String toString() {
        return "Specific Time Constraint Violated (Panel must be scheduled at specific time)";
    }

}

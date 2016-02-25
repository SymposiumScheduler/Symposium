package symposium.model;
import java.util.*;

public class SpecificTimeFilter extends Filter {
    public final int TIME;

    public SpecificTimeFilter(ConstraintPriority priority, Panel p, int t) {
        super(priority, p);
        TIME = t;
    }

    /**
     *
     *
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
     * @return If the panel is assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return (!TimeFormat.withinError(venueTime.TIME.getStart(), TIME, 1));
    }
    @Override
    public String toString() {
        return "SpecificTimeFilter (Panel must be scheduled at specific time)";
    }
}


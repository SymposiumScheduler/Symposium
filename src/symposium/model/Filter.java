package symposium.model;

import java.util.Map;

public abstract class Filter extends Constraint {


    public Filter(ConstraintPriority priority, Panel p) {  //Refering to ScheduleData globally?
        super(priority, p);
    }

    /**
     * evaluate the given VenueTimes and assign a score to it depending on the filters specification
     * If the filter is REQUIRED, the VenueTimes that do not meet the specifications will be removed from the map.
     * @param vtScoreMap Ax map from possible venueTime to score to be evaluated
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    public abstract void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap);
}

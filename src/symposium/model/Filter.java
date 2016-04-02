package symposium.model;

import java.util.Map;

/**
 * Filter Evaluate the given VenueTimes and assign a score to it depending on the filters specification
 * If the filter is REQUIRED, the VenueTimes that do not meet the specifications will be removed from the map.
 */
public abstract class Filter extends Constraint {

    /**
     * Constructs for the VenueFilter class.
     *
     * @param priority enum which determines if a filter is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the filter is part of.
     */
    public Filter(ConstraintPriority priority, Panel panel) {  //Refering to ScheduleData globally?
        super(priority, panel);
    }

    /**
     * Depending on the specification filter will have a different implementation.
     * @param vtScoreMap Ax map from possible venueTime to score to be evaluated
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    public abstract void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap);
}

package symposium.model;

import java.util.*;

/**
 * PreferredVenuesFilter inherits from {@link symposium.model.Filter Filter}.
 * PreferredVenuesFilter orders the venues based on the priority value given in the input file
 */
public class PreferredVenuesFilter extends Filter {
    public static final int HIGHEST_PRIORITY_VENUE_POINTS = 300; // Points picked to utilize the priority, different values yield different results

    /**
     * Constructs for the PreferredVenuesFilter class.
     *
     * @param panel The Panel that the filter is part of.
     */
    public PreferredVenuesFilter(Panel panel) {
        super(ConstraintPriority.DESIRED, panel);
    }

    /**
     * For each VenueTime calculate the score based on preference and gives more score to those with higher priority.
     * The priority value for each venue is given with the input file.
     * @param vtScoreMap A map from venueTime to score
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        for (VenueTime vt : vtScoreMap.keySet()) {
           vtScoreMap.put(vt, vtScoreMap.get(vt) + (int)(HIGHEST_PRIORITY_VENUE_POINTS*1.00/vt.VENUE.PRIORITY));
        }
    }

    /**
     * Never actually called in scheduling stage, only for report purposes
     * @param venueTime
     * @return false
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false;
    }

    /**
     * Never actually used.
     * @return "String with the name of the class and what it does."
     */
    @Override
    public String toString() {
        return "Preferred Venues Filter (Sort the venue based on preference";
    }
}

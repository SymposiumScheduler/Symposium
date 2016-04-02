package symposium.model;

import java.util.*;

/**
 * VenueFilter checks the availability of the panel and keep only the times accepted by the panel
 */
public class PreferedVenuesFilter extends Filter {
    public static final int HIGHEST_PRIORITY_VENUE_POINTS = 300;
    public PreferedVenuesFilter(Panel p) {
        super(ConstraintPriority.DESIRED, p);
    }

    /**
     * ch
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
     * @return "PreferedVenuesFIlter"
     */
    @Override
    public String toString() {
        return "PreferedVenuesFilter";
    }
}

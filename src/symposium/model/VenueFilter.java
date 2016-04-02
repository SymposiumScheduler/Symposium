package symposium.model;

import java.util.*;

/**
 * Filters out venues that are not the one(s) required by the particular panel
 * Dependencies: (Additionally) Venue class
 * @param v The venue the panel must appear in.
 */
public class VenueFilter extends Filter {
    public final Venue VENUE;
    public VenueFilter(ConstraintPriority priority, Panel p, Venue v) {
        super(priority, p);
        VENUE = v;
    }

    /**
     * Chec
     * @param vtScoreMap Ax map from possible venueTime to score to be evaluated
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
     * Dependencies: (Additionally) venueTime.getAssignedPanel method
     * @param venueTime
     * @return If the panel is assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (venueTime.VENUE != VENUE) {
            violated = true;
        }
        else {
            violated = false;
        }
        cache.put(venueTime, violated);
        return violated;
    }
    @Override
    public String toString() {
        return "Venue Constraint Violated: Venue name: " + this.VENUE.NAME;
    }
}


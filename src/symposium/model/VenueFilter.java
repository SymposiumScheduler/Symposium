package symposium.model;

import java.util.*;

public class VenueFilter extends Filter {
    public final Venue VENUE;

    /**
     * Dependencies: (Additionally) Venue class
     * @param v The venue the panel must appear in.
     */
    public VenueFilter(ConstraintPriority priority, Panel p, Venue v) {
        super(priority, p);
        VENUE = v;
    }

    /**
     * VenueTimeDurationFilter checks the venue times and increase score based on the length of the venue time. If the
     * number of panelists on a panel is 1 or 2, it prefer a short venue time. If it more than that it prefer a long
     * venue time.
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


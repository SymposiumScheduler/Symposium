package symposium.model;

import java.util.*;

/**
 * VenueFilter inherits from Filter, @see Filter for documentation.
 * VenueFilter fillers out any venues that are not the required Venues for the particular panel
 */
public class VenueFilter extends Filter {
    public final Venue VENUE;

    /**
     * Constructs for the VenueFilter class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     * @param venue    The venue checked
     */

    public VenueFilter(ConstraintPriority priority, Panel panel, Venue venue) {
        super(priority, panel);
        VENUE = venue;
    }

    /**
     * Filler out any venues that are not in what the panel wants
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
     *
     * @param venueTime The time being checked
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

    /**
     * @return String of the violation message and the Venue name
     */

    @Override
    public String toString() {
        return "Venue Constraint Violated (Specfiic panel needed to be in this Venue but was not able to) : Venue name: " + this.VENUE.NAME;
    }
}


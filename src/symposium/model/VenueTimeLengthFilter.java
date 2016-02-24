package symposium.model;

import java.util.*;

public class VenueTimeLengthFilter extends Filter {
    public static final int NUMBER_PANELIST_NEED_SHORT = 2;
    public static final int VENUE_POINTS = 100;

    public VenueTimeLengthFilter(Panel p) {
        super(ConstraintPriority.DESIRED, p);
    }

    /**
     * VenueTimeLengthFilter checks the venue times and increase score based on the length of the venue time. If the
     * number of panelists on a panel is 1 or 2, it prefer a short venue time. If it more than that it prefer a long
     * venue time.
     *
     * @param vtScoreMap A map from venueTime to score
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        for (VenueTime vt : vtScoreMap.keySet()) {
            if(PANEL.PANELISTS.size() <= NUMBER_PANELIST_NEED_SHORT && vt.SHORT){
                vtScoreMap.put(vt, vtScoreMap.get(vt)+VENUE_POINTS);
            }
            else if(PANEL.PANELISTS.size() > NUMBER_PANELIST_NEED_SHORT && !vt.SHORT){
                vtScoreMap.put(vt, vtScoreMap.get(vt)+VENUE_POINTS);
            }
        }
    }

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false;
    }
    @Override
    public String toString() {
        return "VenueTimeLengthFilter";
    }
}


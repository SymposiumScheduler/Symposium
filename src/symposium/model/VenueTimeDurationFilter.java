package symposium.model;

import java.util.*;

public class VenueTimeDurationFilter extends Filter {
    // ToDo: change the values to be read from the input file
    public static final int NUMBER_PANELIST_NEED_SHORT = 2;
    public static final int SHORT_VENUE_TIME_LENGTH = 50;
    public static final int LONG_VENUE_TIME_LENGTH = 80;
    public static final int VENUE_POINTS = 400;

    public VenueTimeDurationFilter(Panel p) {
        super(ConstraintPriority.VERY_IMPORTANT, p);
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
        if(!PANEL.LOCKED) {
            for (VenueTime vt : keys) {
                if ((PANEL.PANELISTS.size() <= NUMBER_PANELIST_NEED_SHORT) && TimeFormat.withinError(vt.TIME.length(),
                        SHORT_VENUE_TIME_LENGTH, 1)) {
                    vtScoreMap.put(vt, vtScoreMap.get(vt) + VENUE_POINTS);
                } else if ((PANEL.PANELISTS.size() > NUMBER_PANELIST_NEED_SHORT) && vt.TIME.length() < LONG_VENUE_TIME_LENGTH) {
                    vtScoreMap.remove(vt);
                    if (!requiredViolationMap.containsKey(this)) {
                        requiredViolationMap.put(this, 1);
                    } else {
                        requiredViolationMap.put(this, requiredViolationMap.get(this) + 1);
                    }
                }
            }
        }
    }

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false;
    }
    @Override
    public String toString() {
        return "VenueTimeDurationFilter";
    }
}


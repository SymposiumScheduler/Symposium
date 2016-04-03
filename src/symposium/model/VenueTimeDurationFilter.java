package symposium.model;

import java.util.*;

/**
 * VenueTimeDurationFilter inherits from Filter, @see Filter for documentation.
 * VenueTimeDurationFilter checks the venue times and increase score based on the length of the venue time.
 * If the number of panelists on a panel is 1 or 2, it prefer a short venue time.
 * If it has more than that it prefer a long venue time.
 */
public class VenueTimeDurationFilter extends Filter {
    // ToDo: change the values to be read from the input file
    public static final int NUMBER_PANELIST_NEED_SHORT = 2;
    public static final int SHORT_VENUE_TIME_LENGTH = 50;
    public static final int LONG_VENUE_TIME_LENGTH = 80;
    public static final int VENUE_POINTS = 400; // Points picked to utilize the priority, different values yield different results

    /**
     * Constructs for the VenueTimeDurationFilter class.
     *
     * @param panel The Panel that the filter is part of.
     */

    public VenueTimeDurationFilter(Panel panel) {
        super(ConstraintPriority.VERY_IMPORTANT, panel);
    }

    /**
     * Orders and filters venue times based on their size; used to prioritize short venueTImes for short Panels
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

    /**
     * Never actually called in scheduling stage, only for report purposes
     * @param venueTime The time being checked
     * @return false
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false;
    }

    /**
     * @return String of the violation message
     */

    @Override
    public String toString() {
        return "Venue Time Duration Filter (Panels with =< 2 panelists go in short venue time slots, otherwise, go in long time slots)";
    }
}


package symposium.model;

import java.util.Map;

public abstract class Filter extends Constraint {
    public static class RecommendedVenueTime implements Comparable<RecommendedVenueTime> {
        public final VenueTime VENUETIME;
        public final int POINTS;
        public RecommendedVenueTime(VenueTime vt, int points) {
            VENUETIME = vt;
            POINTS = points;
        }

        @Override
        public int compareTo(RecommendedVenueTime otherVt) {
                //smaller points is first => negative
            return otherVt.POINTS - this.POINTS ;
        }
        public String toString() {
            return POINTS + " => " + VENUETIME.toString();
        }
    }

    public Filter(ConstraintPriority priority, Panel p) {  //Refering to ScheduleData globally?
        super(priority, p);
    }

    /**
     * evaluate the venueTimes and encourage or discourage.
     * If the filter is required, bad venueTimes should be removed.
     * @param vtMap
     */
    public abstract void filter(Map<VenueTime, Integer> vtMap);
}

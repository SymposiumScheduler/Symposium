package symposium;

import symposium.model.*;

import java.util.*;

/**
 * Can be extended for other Constraints.
 * And not return a venueTime if it's violated like venueConstraint
 *
 *
 * Estimate for gain in day D is the number of panelists that are not scheduled in D and the panel is available in D.
 */
public class VenueTimeFilter implements Iterable<VenueTimeFilter.RecommendedVenueTime> {
    public static class RecommendedVenueTime implements Comparable<RecommendedVenueTime> {
        public final VenueTime VENUETIME;
        public final int GAIN;
        public RecommendedVenueTime(VenueTime vt, int gain) {
            VENUETIME = vt;
            GAIN = gain;
        }

        @Override
        public int compareTo(RecommendedVenueTime otherVt) {
            //smaller gain is first => negative
            return otherVt.GAIN - this.GAIN ;
        }
        public String toString() {
            return GAIN + " => " + VENUETIME.toString();
        }
    }

    public static int COST_OF_MIN_PANELIST_VIOLATION = 2; // by testing various values

    private final Panel panel;
    private final List<RecommendedVenueTime> suggestions;
    public VenueTimeFilter(Panel panel) {
        this.panel = panel;
        this.suggestions = createSuggestions(panel);
    }

    public static Map<Integer, Integer> getMinPanelistDayGain(Panel panel) {
        Set<Integer> daysInAvailability = new HashSet<>();
        Iterator<TimeRange> pItr = panel.AVAILABILITY.iterator();
        while (pItr.hasNext()) {
            daysInAvailability.add(TimeFormat.getNumberOfDay(pItr.next().START));
        }
        //

        //
        Map<Integer, Integer> dayGainMap = new HashMap<>();
        for (String pnst : panel.PANELISTS) {
            for (int avDay : daysInAvailability) {
                if(ScheduleData.instance().getPanelistAppearanceNo(avDay, pnst)>0){
                    continue;
                }
                // If this line is reached, pnst is not scheduled in avDay
                if (dayGainMap.get(avDay) == null) {
                    dayGainMap.put(avDay, 1);
                } else {
                    dayGainMap.put(avDay, dayGainMap.get(avDay) + 1);
                }
            }
        }
        return dayGainMap;
    }

    public static List<RecommendedVenueTime> createSuggestions(Panel panel) {
        Map<Integer, Integer> minDayToGainMap = getMinPanelistDayGain(panel);
        //
        List<RecommendedVenueTime> suggs = new ArrayList<>();
        for (Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                // Availability elimination
                if(! panel.AVAILABILITY.doesEnclose(vt.TIME)) {
                    continue;
                }
                int gain = 0;
                // Min panelist constraint
                gain += COST_OF_MIN_PANELIST_VIOLATION*(minDayToGainMap.containsKey(vt.getDay()) ? minDayToGainMap.get(vt.getDay()) : 0 );
                //
                suggs.add(new RecommendedVenueTime(vt, gain) );
            }
        }
        //
        java.util.Collections.sort(suggs);
        /*System.out.println("---------");
        for(RecommendedVenueTime r : suggs) {
            System.out.println(r);
        }*/
        return suggs;
    }

    @Override
    public Iterator<RecommendedVenueTime> iterator() {
        return suggestions.iterator();
    }
}



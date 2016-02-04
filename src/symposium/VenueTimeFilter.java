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
    }


    public static int COST_OF_MIN_PANELIST_VIOLATION = 4; // by testing various values

    private final Panel panel;
    private final List<RecommendedVenueTime> suggestions;
    public VenueTimeFilter(Panel panel) {
        this.panel = panel;
        this.suggestions = createSuggestionsBasedOnMinPanelists(panel);
    }

    public static List<RecommendedVenueTime> createSuggestionsBasedOnMinPanelists(Panel panel) {
        Collection<Integer> daysInAvailability = new HashSet<>();
        Iterator<TimeRange> pItr = panel.AVAILABILITY.iterator();
        while (pItr.hasNext()) {
            daysInAvailability.add(TimeFormat.getNumberOfDay(pItr.next().START));
        }
        //

        //
        Map<Integer, Integer> dayGainMap = new HashMap<>();
        for (String pnst : panel.PANELISTS) {
            for (int avDay : daysInAvailability) {
                boolean scheduledInAvDay = false;
                Collection<Panel> ps = ScheduleData.instance().getPanelistAssignedPanels(pnst);
                if (ps != null) {
                    for (Panel p : ps) {
                        if (p.getVenueTime().getDay() == avDay) {
                            scheduledInAvDay = true;
                            break;
                        }
                    }
                }

                if (scheduledInAvDay) {
                    continue; // it's scheduled in this day
                }

                if (dayGainMap.get(avDay) == null) {
                    dayGainMap.put(avDay, 1);
                } else {
                    dayGainMap.put(avDay, dayGainMap.get(avDay) + 1);
                }
            }
        }
        //
        // Sort Vt's by gain
        //
        List<RecommendedVenueTime> suggs = new ArrayList<>();
        for (Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                    int gain = COST_OF_MIN_PANELIST_VIOLATION*(dayGainMap.containsKey(vt.getDay()) ? dayGainMap.get(vt.getDay()) : 0 );
                    suggs.add(new RecommendedVenueTime(vt, gain) );
            }
        }
        //
        java.util.Collections.sort(suggs);
        return suggs;
    }

    @Override
    public Iterator<RecommendedVenueTime> iterator() {
        return suggestions.iterator();
    }
}



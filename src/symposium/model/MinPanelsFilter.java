package symposium.model;

import java.util.*;

public class MinPanelsFilter extends Filter {
    public static int COST_OF_MIN_PANELIST_VIOLATION = 2; // by testing various values

    public MinPanelsFilter(ConstraintPriority priority,Panel panel) {
        super(priority, panel);
    }

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false; // not violated because the panel is scheduled. and its panels are scheduled in this day.
    }

    @Override
    public void filter(Map<VenueTime, Integer> vtMap) {
        Map<Integer, Integer> minDayToGainMap = getMinPanelistDayGain();
        //
        List<RecommendedVenueTime> suggs = new ArrayList<>();
        for (VenueTime vt : vtMap.keySet()) {
            vtMap.put(vt, vtMap.get(vt) + COST_OF_MIN_PANELIST_VIOLATION * (minDayToGainMap.containsKey(vt.getDay()) ? minDayToGainMap.get(vt.getDay()) : 0));
        }
    }

    private Map<Integer, Integer> getMinPanelistDayGain() {
        Set<Integer> daysInAvailability = new HashSet<>();
        Iterator<TimeRange> pItr = this.PANEL.AVAILABILITY.iterator();
        while (pItr.hasNext()) {
            daysInAvailability.add(TimeFormat.getNumberOfDay(pItr.next().START));
        }
        //

        //
        Map<Integer, Integer> dayGainMap = new HashMap<>();
        for (String pnst : this.PANEL.PANELISTS) {
            for (int avDay : daysInAvailability) {
                if (ScheduleData.instance().getPanelistAppearanceNo(avDay, pnst) > 0) {
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

}

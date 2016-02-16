package symposium.model;

import java.util.*;
//TODO: MinPanelsFilter is always assumed that the priority is VeryImportant
public class MinPanelsFilter extends Filter {
    public static int COST_OF_MIN_PANELIST_VIOLATION = 4; // by testing various values

    public MinPanelsFilter(ConstraintPriority priority,Panel panel) {
        super(priority, panel);
    }

    /**
     * Never actually called in scheduling stage, only for report purposes
     * @param venueTime
     * @return false
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return false; // not violated because the panel is scheduled. and its panels are scheduled in this day.
    }

    /**
     *  for each VenueTime vt in the map vtScoreMap:
     *      increase vt's score by the number of panelist not scheduled yet in vt's day
     *
     * @param vtScoreMap A map from possible venueTime to score to be evaluated
     * @param requiredViolationMap A map from only required Constraints to the number of violations
     */
    @Override
    public void filter(Map<VenueTime, Integer> vtScoreMap, Map<Constraint, Integer> requiredViolationMap) {
        Map<Integer, Integer> minDayToGainMap = getMinPanelistDayGain();
        //
        for (VenueTime vt : vtScoreMap.keySet()) {
            vtScoreMap.put(vt, vtScoreMap.get(vt) + COST_OF_MIN_PANELIST_VIOLATION * (minDayToGainMap.containsKey(vt.getDay()) ? minDayToGainMap.get(vt.getDay()) : 0));
        }
    }

    /**
     *
     * @return Map<available day, number of panelists not scheduled on a certain day
     */
    private Map<Integer, Integer> getMinPanelistDayGain() {
        //TODO: better implementation is  possible in scheduleData
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

    @Override
    public String toString() {
        return "Min Panels Filter: priority = " + PRIORITY;
    }

}

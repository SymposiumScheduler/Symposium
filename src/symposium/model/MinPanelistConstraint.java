package symposium.model;

import java.util.*;

public class MinPanelistConstraint extends Constraint {

    private final Set<Integer> daysInAvailability = new HashSet<>();
    public MinPanelistConstraint(ConstraintPriority priority, Panel p) {
        super(priority,p);
        Iterator<TimeRange> avlbItr = p.AVAILABILITY.iterator();
        while(avlbItr.hasNext()) {
            daysInAvailability.add(TimeFormat.getNumberOfDay(avlbItr.next().START));
        }
    }

    /**
     * Kind of a poor man's estimate function; checks to make sure when scheduling panels that other panelist doesn't
     * appear on another panel on the same day.
     * AGAIN WORTH NOTING: This minPanelistConstraint is different from the true minPanelistConstraint.
     *
     *
     * This constraint :
     *      VIOLATED : if for at lest one panelist ((day of venueTime has panel) && (there is a day in availibilty which has no panel))
     *      NOT VIOLATED : otherwise
     *
     *
     * @param venueTime
     * @return bool True if panel would be scheduled with panelist appears in another panel on same day, false otherwise.
     */
    public boolean isConstraintViolated(VenueTime venueTime) {

        int candidateDay = TimeFormat.getNumberOfDay(venueTime.getDay());

        for (String panelist : this.PANEL.PANELISTS) {

            // if no panel in candidateDay, no violation
            if(ScheduleData.instance().getPanelistAppearanceNo(candidateDay, panelist) == 0) {
                return false;
                // this is a heuristic, so, it's better to not violate if the vt helps.
            }


            // if this panelist has a panel in every day in availability excluding candidateDay, no violation
            boolean scheduledInEveryDay = true;
            for(int day : this.daysInAvailability) {
                if(day == candidateDay) {
                    continue; // next day
                }
                if(ScheduleData.instance().getPanelistAppearanceNo(day, panelist) == 0 ) {
                    scheduledInEveryDay = false; // found a day with no appearances
                }
            }
            if(scheduledInEveryDay) {
                continue;
            }


            return true; // VIOLATION
        }
        return false; // NO VIOLATION was found
    }

    @Override
    public String toString() {
        return "MinPanelistConstraint (Panelist should appear a minimum number of times per day)";
    }

}

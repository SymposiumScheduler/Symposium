package symposium;

import symposium.model.*;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;

public class DummyScheduler {
    public DummyScheduler() {}

    private void schedule(Panel panel) {
        VenueTime vt = returnFirstLegalVenueTime(panel);
        if (vt != null) {
            ScheduleData.instance().assignPanelToVenueTime(panel,vt);
        } else {
            ScheduleData.instance().cannotSchedule(panel, "no available venue times");
        }
    }

    public void makeSchedule() {
        // init difficulty
        DetermineDifficulty.setDifficulties();
        // go through panels and schedule
        List<Panel> pnlCollection = ScheduleData.instance().getFreePanels();
        while(pnlCollection.size() > 0) {
            this.schedule(pnlCollection.get(0));
        }
    }

    private VenueTime returnFirstLegalVenueTime(Panel panel) {
    /*
        TODO :
        Can be optimized by making one iteration and keeping three venueTimes in the same time as iterating.
        DesiredSatisfied, VeryImportantSatisfied, RequiredSatisfied.

        In the iteration, if a DesiredSatisfied venueTime is found, just return it. If we finished the whole venueTime space and cannot find
        DesiredSatisfied then we return VeryImportantSatisfied if it's found. if not, return RequiredSatisfied.
        If no RequiredSatisfied found, then venueTime cannot be found.
*/

       VenueTime vt = searchForLegalVenueTime(panel, ConstraintPriority.DESIRED); // min requirement is desired
        if (vt == null) {
            vt = searchForLegalVenueTime(panel, ConstraintPriority.VERY_IMPORTANT); // min requirement is important
            if ( vt == null) {
                vt = searchForLegalVenueTime(panel, ConstraintPriority.REQUIRED); // min requirement is required
            }
        }
        return vt;
    }

    private VenueTime searchForLegalVenueTime(Panel panel, ConstraintPriority minRequirement) {

        boolean noViolations;
        for ( Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                noViolations = true;
                for (Constraint constraint : panel.CONSTRAINTS) {
                    if(constraint.PRIORITY.compareTo(minRequirement) >= 0) {
                        if(constraint.isConstraintViolated(vt)) {
                            noViolations = false;
                            break;
                        }
                    }
                }
                if(noViolations){
                    return vt; // vt has passed all priority conditions
                } else {
                    continue;
                }
            }
        }
        return null; // no venueTime found
    }

    private static abstract class DetermineDifficulty {
        /**
         * Difficulty is determined by :
         *
         * 1 :   : overlap and length of availability
         * 2 :   : panelists overlap
         * 3 :   : min size of venue
         * 4 :   : locked (no longer needed)
         * 5 :   : number and priority of constraint
         * 6:   : category overlap
         *
         *
         * 1: Required * 100
         * 2: Very important * 10
         * 3: Desirable * 1
         *
         * X = order of magnitude
         */

        public static void setDifficulties() {
            List<Panel> freePanels = ScheduleData.instance().getFreePanels();
            HashMap panelistDifficulty = DetermineDifficulty.panelistDifficulty(freePanels);
            HashMap categoryDifficulty = DetermineDifficulty.categoryDifficulty(freePanels);
            int y;
            for(Panel p : freePanels) {
                y = 0; // reset y for every panel

                for (String x : p.PANELISTS){
                    y += (int) panelistDifficulty.get(x);
                }
                for (String x : p.CATEGORIES){
                    y += (int) categoryDifficulty.get(x);
                }
                p.setDifficulty(DetermineDifficulty.evalDifficulty(p)+y);
            }
            Collections.sort(freePanels);
            Collections.reverse(freePanels);
        }


        private static int evalDifficulty(Panel panel) {
            //int i = ScheduleData.instance().VENUES.size(); // do somehting from ScheduleData, but why? What does this do?
            int difficulty = availabilityDifficulty(panel) + venueConstraintDifficulty(panel) + sizeConstraintDifficulty(panel);
            return difficulty;
        }

        private static int availabilityDifficulty(Panel panel){
            Range range = panel.getAvailability();
            return  (int) 10000/range.length();
        }

        private static HashMap panelistDifficulty(List<Panel> panels) {
            HashMap<String, Integer> m = new HashMap();
            for (Panel panel: panels) {
                for (String panelist : panel.PANELISTS) {
                    if (m.containsKey(panelist)) {
                        m.put(panelist, m.get(panelist) + 1);
                    } else {
                        m.put(panelist, 1);
                    }
                }
            }
            return m;
        }

        private static int venueConstraintDifficulty(Panel panel) {
            boolean hasvc = false;
            for (Constraint c: panel.CONSTRAINTS) {
                if (c instanceof VenueConstraint) {
                    hasvc = true;
                    break;
                }
            }
            //
            if (hasvc) {
                return 200;
            }
            else {
                return 0;
            }
        }

        private static int sizeConstraintDifficulty(Panel panel) {
            int hassc = 0;
            for (Constraint c: panel.CONSTRAINTS) {
                if (c instanceof SizeConstraint) {
                    hassc = ((SizeConstraint) c).getMinSize();
                    break;
                }
            }
            //
            return 10*hassc;
        }

        private static HashMap categoryDifficulty(List<Panel> panels) {
            HashMap<String, Integer> m = new HashMap();
            for (Panel panel: panels) {
                for (String category : panel.CATEGORIES) {
                    if (m.containsKey(category)) {
                        m.put(category, m.get(category) + 1);
                    } else {
                        m.put(category, 1);
                    }
                }
            }
            return m;
        }
    }
}
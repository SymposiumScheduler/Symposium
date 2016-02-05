package symposium;

import symposium.model.*;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DummyScheduler {
    public int[] adjustment;
    public static int SIZE_CONSTRAINT_VALUE = 1;
    public static int VENUE_CONSTRAINT_VALUE = 1;
    public static int TIME_CONSTRAINT_VALUE = 1;
    public static int AVAILABILITY_CONSTRAINT_VALUE = 1;

    public DummyScheduler(int[] adjust) {
        this.adjustment = adjust;
        SIZE_CONSTRAINT_VALUE = SIZE_CONSTRAINT_VALUE * adjust[0];
        VENUE_CONSTRAINT_VALUE = VENUE_CONSTRAINT_VALUE * adjust[1];
        TIME_CONSTRAINT_VALUE = TIME_CONSTRAINT_VALUE * adjust[2];
        AVAILABILITY_CONSTRAINT_VALUE = AVAILABILITY_CONSTRAINT_VALUE * adjust[3];
    }

    public DummyScheduler(){

    }

    private void schedule(Panel panel) {
        VenueTime vt = returnFirstLegalVenueTime(panel);
        if (vt != null) {
            ScheduleData.instance().assignPanelToVenueTime(panel,vt);
        }
    }

    public void makeSchedule() {
        // 0) init difficulty
        DetermineDifficulty.setDifficulties();
        // 1) go through panels and schedule
        List<Panel> pnlCollection = ScheduleData.instance().getFreePanels();
        while(pnlCollection.size() > 0) {
            this.schedule(pnlCollection.get(0));
        }
        setAssignedPanelsMessages();
    }
    public void setAssignedPanelsMessages(){
        for(Panel panel: ScheduleData.instance().getAssignedPanels()) {
            for (Constraint constraint : panel.CONSTRAINTS) {
                if (constraint.isConstraintViolated(panel.getVenueTime())) {
                    panel.addMessage(constraint.toString());
                }
            }
        }
    }
    public void setUnschedulablePanelMessages(Panel p, Map<Constraint, Integer> m) {
        for (Constraint key : m.keySet()) {
            p.addMessage(key + " violated " + m.get(key) + " times");
        }
    }
    private VenueTime returnFirstLegalVenueTime(Panel panel) {
        /*
         TODO :
         Can be optimized by making one iteration and keeping three venueTimes : DesiredSatisfied, VeryImportantSatisfied, RequiredSatisfied.

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
        Map<Constraint, Integer> violationMap = new HashMap();
        boolean noViolations;
        for ( Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                if(! panel.AVAILABILITY.doesEnclose(vt.TIME) ) {
                    continue;
                }
                noViolations = true;
                for (Constraint constraint : panel.CONSTRAINTS) {
                    if(constraint.PRIORITY.compareTo(minRequirement) >= 0) {
                        if(constraint.isConstraintViolated(vt)) {
                            if (minRequirement == ConstraintPriority.REQUIRED) {
                                if (violationMap.containsKey(constraint)) {
                                    violationMap.put(constraint,  violationMap.get(constraint) + 1);
                                } else {
                                    violationMap.put(constraint, 1);
                                }
                            }
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
        if (minRequirement == ConstraintPriority.REQUIRED) {
            setUnschedulablePanelMessages(panel, violationMap);
            ScheduleData.instance().cannotSchedule(panel);
        }
        return null; // no venueTime found
    }

    private static abstract class DetermineDifficulty {
        //private static final int SIZE_CONSTRAINT_VALUE = 10;
        //private static final int VENUE_CONSTRAINT_VALUE = 10000;
        //private static final int TIME_CONSTRAINT_VALUE = 10000;
        //private static final int AVAILABILITY_CONSTRAINT_VALUE = 10000;

        /**
         * Difficulty is determined by :
         *
         * 1 :   : overlap and length of availability
         * 2 :   : panelists overlap
         * 3 :   : category overlap
         * 4 :   : min size of venue
         * 5 :   : venue constraint
         * 6 :   : time constraint
         * 7 :   : number and priority of constraint
         *
         *
         * 1: Required * 100
         * 2: Very important * 10
         * 3: Desirable * 1
         */

        public static void setDifficulties() {
            List<Panel> freePanels = ScheduleData.instance().getFreePanels();
            HashMap<String, Integer> panelistDifficulty = panelistDifficultyMap(freePanels);
            HashMap<String, Integer> categoryDifficulty = categoryDifficultyMap(freePanels);
            int panelDifficulty;
            for(Panel p : freePanels) {
                panelDifficulty = 0; // reset  for every panel

                for (String x : p.PANELISTS){
                    panelDifficulty += panelistDifficulty.get(x);
                }
                for (String x : p.CATEGORIES){
                    panelDifficulty += categoryDifficulty.get(x);
                }

                panelDifficulty += availabilityDifficulty(p) + venueConstraintDifficulty(p) + sizeConstraintDifficulty(p) + TimeConstraintDifficulty(p);
                p.setDifficulty(panelDifficulty);
            }
            Collections.sort(freePanels);
            Collections.reverse(freePanels);
        }

        private static int availabilityDifficulty(Panel panel){
            Range range = panel.getAvailability();
            return  AVAILABILITY_CONSTRAINT_VALUE /range.length();
        }

        private static int venueConstraintDifficulty(Panel panel) {
            for (Constraint c: panel.CONSTRAINTS) {
                if (c instanceof VenueConstraint) {
                    return VENUE_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

        private static int TimeConstraintDifficulty(Panel panel) {
            for (Constraint c: panel.CONSTRAINTS) {
                if (c instanceof SpecificTimeConstraint) {
                    return TIME_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

        private static int sizeConstraintDifficulty(Panel panel) {
            for (Constraint c: panel.CONSTRAINTS) {
                if (c instanceof SizeConstraint) {
                    return SIZE_CONSTRAINT_VALUE * ((SizeConstraint) c).getMinSize();
                }
            }

            return 0;
        }

        private static HashMap<String, Integer> categoryDifficultyMap(List<Panel> panels) {
            HashMap<String, Integer> m = new HashMap<>();
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

        private static HashMap<String, Integer> panelistDifficultyMap(List<Panel> panels) {
            HashMap<String, Integer> m = new HashMap<>();
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
    }
}
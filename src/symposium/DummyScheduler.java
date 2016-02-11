package symposium;

import symposium.model.*;
import static symposium.model.ConstraintPriority.*;
import java.util.*;
import static symposium.model.Filter.RecommendedVenueTime;
public class DummyScheduler {

    private static class SearchResult {
        public final VenueTime VENUETIME;
        public final Map<Constraint, Integer> CAUSE_OF_FAIL_MAP;

        public SearchResult(VenueTime vt) {
            VENUETIME = vt;
            CAUSE_OF_FAIL_MAP = null;
        }
        public SearchResult(Map<Constraint, Integer> violaitons) {
            VENUETIME = null;
            CAUSE_OF_FAIL_MAP = violaitons;
        }
        public boolean isSuccess() {
            return (VENUETIME != null);
        }
    }

    public static final int VIOLATION_COST_DESIRED = 2;
    public static final int VIOLATION_COST_VERYIMPORTANT = 4;

    public DummyScheduler() {
    }


    private void schedule(Panel panel) {
        SearchResult sr = searchForVenueTime(panel);
        if(sr.isSuccess()) {
            ScheduleData.instance().assignPanelToVenueTime(panel, sr.VENUETIME);
        } else {
            setUnschedulablePanelMessages(panel, sr.CAUSE_OF_FAIL_MAP);
            ScheduleData.instance().cannotSchedule(panel);
        }
    }

    public void makeSchedule() {
        // 0) init difficulty
        DetermineDifficulty.setDifficulties();
        // 1) go through panels and schedule
        List<Panel> pnlCollection = ScheduleData.instance().getFreePanels();
        while (pnlCollection.size() > 0) {
            this.schedule(pnlCollection.get(0));
        }
        setAssignedPanelsMessages();
    }


    public void setAssignedPanelsMessages() {
        for (Panel panel : ScheduleData.instance().getAssignedPanels()) {
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


    private SearchResult searchForVenueTime(Panel panel) {
        // prepare venueTime map, should be done in schedule data. TODO this is just for now.
        Map<VenueTime, Integer> vtPointMap = new HashMap<>();
        for(Venue v : ScheduleData.instance().VENUES) {
            for(VenueTime vt : v.getFreeVenueTimes()) {
                vtPointMap.put(vt,0);
            }
        }
        // go thorugh the filters
        for(Constraint c : panel.CONSTRAINTS) {
            if(c instanceof Filter) {
                ((Filter) c).filter(vtPointMap);
            }
        }
        // create Iterable recommendations!
        List<RecommendedVenueTime> rVts = new ArrayList<>();
        for(VenueTime vt : vtPointMap.keySet()) {
            rVts.add(new RecommendedVenueTime(vt, vtPointMap.get(vt)));
        }
        Collections.sort(rVts);
        // begin the search
        Map<Constraint, Integer> violationMap = new HashMap<>();
        RecommendedVenueTime bestVt = null;
        vtLoop : for (RecommendedVenueTime rVt : rVts) {
            int vtPoints = rVt.POINTS;
            for (Constraint c : panel.CONSTRAINTS) {
                if(!(c instanceof Filter) && c.isConstraintViolated(rVt.VENUETIME)) {
                    if(c.PRIORITY == REQUIRED) {
                        // add to violationMap
                        if(violationMap.containsKey(c)) {
                            violationMap.put(c, violationMap.get(c) + 1);
                        } else {
                            violationMap.put(c,1);
                        }
                        //System.out.println("V!"+ c);
                        //
                        continue vtLoop; // next venueTime
                    }
                    //
                    vtPoints -= (c.PRIORITY == DESIRED ? VIOLATION_COST_DESIRED : VIOLATION_COST_VERYIMPORTANT);
                }
            }
            //System.out.println(/*rVt.toString()+*/"   OrigGain:"+rVt.GAIN +" New GAIN: " + vtGain);
            if(bestVt == null || bestVt.POINTS < vtPoints) {
                //System.out.print((bestVt == null ? "" : bestVt.GAIN));
                //System.out.println("-->" + vtGain);
                bestVt = new RecommendedVenueTime(rVt.VENUETIME, vtPoints);

                // TODO : Currently it doesn't stop if there is no violations. It checks everything regardless. The results are much better if we checked everything
                // and the cost in terms of performance is really small
            }
        }
        //System.out.println("-------");
        // return
        if(bestVt != null) {
            return new SearchResult(bestVt.VENUETIME);
        } else {
            return new SearchResult(violationMap);
        }
    }


    private static abstract class DetermineDifficulty {
        private static final int SIZE_CONSTRAINT_VALUE = 10;
        private static final int VENUE_CONSTRAINT_VALUE = 20000;
        private static final int TIME_CONSTRAINT_VALUE = 20000;
        private static final int AVAILABILITY_CONSTRAINT_VALUE = 10000;

        /**
         * Difficulty is determined by :
         * <p>
         * 1 :   : overlap and length of availability
         * 2 :   : panelists overlap
         * 3 :   : category overlap
         * 4 :   : min size of venue
         * 5 :   : venue constraint
         * 6 :   : time constraint
         * 7 :   : number and priority of constraint
         * <p>
         * <p>
         * 1: Required * 100
         * 2: Very important * 10
         * 3: Desirable * 1
         */

        public static void setDifficulties() {
            List<Panel> freePanels = ScheduleData.instance().getFreePanels();
            HashMap<String, Integer> panelistDifficulty = panelistDifficultyMap(freePanels);
            HashMap<String, Integer> categoryDifficulty = categoryDifficultyMap(freePanels);
            int panelDifficulty;
            for (Panel p : freePanels) {
                panelDifficulty = 0; // reset  for every panel

                for (String x : p.PANELISTS) {
                    panelDifficulty += panelistDifficulty.get(x);
                }
                for (String x : p.CATEGORIES) {
                    panelDifficulty += categoryDifficulty.get(x);
                }

                panelDifficulty += availabilityDifficulty(p) + venueConstraintDifficulty(p) + sizeConstraintDifficulty(p) + TimeConstraintDifficulty(p);
                p.setDifficulty(panelDifficulty);
            }
            Collections.sort(freePanels);
            Collections.reverse(freePanels);
        }

        private static int availabilityDifficulty(Panel panel) {
            Range range = panel.getAvailability();
            return AVAILABILITY_CONSTRAINT_VALUE / range.length();
        }

        private static int venueConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof VenueConstraint) {
                    return VENUE_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

        private static int TimeConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof SpecificTimeConstraint) {
                    return TIME_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

        private static int sizeConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof SizeConstraint) {
                    return SIZE_CONSTRAINT_VALUE * ((SizeConstraint) c).getMinSize();
                }
            }

            return 0;
        }

        private static HashMap<String, Integer> categoryDifficultyMap(List<Panel> panels) {
            HashMap<String, Integer> m = new HashMap<>();
            for (Panel panel : panels) {
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
            for (Panel panel : panels) {
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
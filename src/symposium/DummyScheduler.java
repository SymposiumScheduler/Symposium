package symposium;

import symposium.model.*;
import static symposium.model.ConstraintPriority.*;
import java.util.*;

public class DummyScheduler {
    private final int SIZE_CONSTRAINT_VALUE;
    private final  int VENUE_CONSTRAINT_VALUE;
    private final  int TIME_CONSTRAINT_VALUE;
    private final  int AVAILABILITY_CONSTRAINT_VALUE;
    private final  int PANELISTS_CONSTRAINT_VALUE;

    public DummyScheduler(int[] diffValues) {
        SIZE_CONSTRAINT_VALUE = diffValues[0];
        VENUE_CONSTRAINT_VALUE =  diffValues[1];
        TIME_CONSTRAINT_VALUE =  diffValues[2];
        AVAILABILITY_CONSTRAINT_VALUE =  diffValues[3];
        PANELISTS_CONSTRAINT_VALUE = diffValues[4];
    }

    private class SearchResult {
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

    public final int VIOLATION_COST_DESIRED = 100;
    public final int VIOLATION_COST_VERYIMPORTANT = 400;


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
        setDifficulties();
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

    /**
     * A data structure that contains a VenueTime and its score. It is used in searchForVenueTime to order the venueTimes by their scores
     */
    private class VenueTimeWithScore implements Comparable<VenueTimeWithScore> {
        public final VenueTime VENUETIME;
        public final int SCORE;
        public VenueTimeWithScore(VenueTime vt, int score) {
            VENUETIME = vt;
            SCORE = score;
        }

        /**
         * If this is larger than other return negative number.
         * @param otherVt
         * @return negative if this is first, 0 if equal, and positive if other is first.
         */
        @Override
        public int compareTo(VenueTimeWithScore otherVt) {
            if(this.SCORE != otherVt.SCORE) {
                return (otherVt.SCORE < this.SCORE ? -1 : 1);
            }
            return this.VENUETIME.compareTo(otherVt.VENUETIME);
        }
        public String toString() {
            return "Score: " + SCORE + ", VenueTime : " + VENUETIME.toString();
        }
    }
    private SearchResult searchForVenueTime(Panel panel) {
        Map<Constraint, Integer> requiredViolationMap = new HashMap<>();
        // prepare venueTime map, should be done in schedule data. TODO this is just for now.
        Map<VenueTime, Integer> vtScoreMap = new HashMap<>();
        for (Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                vtScoreMap.put(vt, 0);
            }
        }
        // go thorugh the filters
        for (Constraint c : panel.CONSTRAINTS) {
            if (c instanceof Filter) {
                ((Filter) c).filter(vtScoreMap, requiredViolationMap);
            }
        }
        // create Iterable score
        List<VenueTimeWithScore> vtScores = new ArrayList<>();
        for (VenueTime vt : vtScoreMap.keySet()) {
            vtScores.add(new VenueTimeWithScore(vt, vtScoreMap.get(vt)));
        }
        Collections.sort(vtScores);
        // begin the search
        VenueTimeWithScore bestVt = null;
        vtLoop:
        for (VenueTimeWithScore recommendedVt : vtScores) {
            int vtScore = recommendedVt.SCORE;
            for (Constraint c : panel.CONSTRAINTS) {
                if (c.isConstraintViolated(recommendedVt.VENUETIME)) {

                    // if c is required, continue to next vt and update violation map
                    if (c.PRIORITY == REQUIRED) {
                        // add to violationMap
                        if (requiredViolationMap.containsKey(c)) {
                            requiredViolationMap.put(c, requiredViolationMap.get(c) + 1);
                        } else {
                            requiredViolationMap.put(c, 1);
                        }
                        continue vtLoop; // next venueTime
                    }
                    //
                    vtScore -= (c.PRIORITY == DESIRED ? VIOLATION_COST_DESIRED : VIOLATION_COST_VERYIMPORTANT);
                }
            }

            if (bestVt == null || bestVt.SCORE < vtScore) {
                bestVt = new VenueTimeWithScore(recommendedVt.VENUETIME, vtScore);

                // if No violations, break. No way a better venue time is coming.
                if (bestVt.SCORE == recommendedVt.SCORE) {
                    break;
                }
            }
        }
        // return
        if (bestVt != null) {
            return new SearchResult(bestVt.VENUETIME);
        } else {
            return new SearchResult(requiredViolationMap);
        }
    }

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

        public void setDifficulties() {
            List<Panel> freePanels = ScheduleData.instance().getFreePanels();
            List<Panel> lockedPanels = new ArrayList<>();
            HashMap<String, Integer> panelistDifficulty = panelistDifficultyMap(freePanels);
            HashMap<String, Integer> categoryDifficulty = categoryDifficultyMap(freePanels);
            int panelDifficulty;


            // remove locked from freePanels
            boolean venueConst = false;
            boolean timeConst = false;
            for (Panel p : freePanels) {
                for (Constraint c : p.CONSTRAINTS) {
                    if (c instanceof VenueConstraint) {
                        venueConst = true;
                    }
                    if (c instanceof SpecificTimeConstraint) {
                        timeConst = true;
                    }
                }
                if (venueConst && timeConst) {
                    lockedPanels.add(p);
                }
            }
            // remove from freePanels
            freePanels.removeAll(lockedPanels);
            //////////////////////////////

            for (Panel p : freePanels) {
                panelDifficulty = 0; // reset  for every panel

                for (String x : p.PANELISTS) {
                    panelDifficulty += panelistDifficulty.get(x) * PANELISTS_CONSTRAINT_VALUE;
                }
                for (String x : p.CATEGORIES) {
                    panelDifficulty += categoryDifficulty.get(x);
                }

                panelDifficulty += availabilityDifficulty(p) + venueConstraintDifficulty(p) + sizeConstraintDifficulty(p) + TimeConstraintDifficulty(p);
                p.setDifficulty(panelDifficulty);
            }
            Collections.sort(freePanels);
            Collections.reverse(freePanels);
            // add locked to the begainning
            freePanels.addAll(0,lockedPanels);
        }

        private int availabilityDifficulty(Panel panel) {
            Range range = panel.getAvailability();
            return AVAILABILITY_CONSTRAINT_VALUE / range.length();
        }

        private int venueConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof VenueConstraint) {
                    return VENUE_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

        private int TimeConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof SpecificTimeConstraint) {
                    return TIME_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

        private int sizeConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof SizeConstraint) {
                    return SIZE_CONSTRAINT_VALUE * ((SizeConstraint) c).getMinSize();
                }
            }

            return 0;
        }

        private HashMap<String, Integer> categoryDifficultyMap(List<Panel> panels) {
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

        private HashMap<String, Integer> panelistDifficultyMap(List<Panel> panels) {
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
package symposium;

import symposium.model.*;
import static symposium.model.ConstraintPriority.*;
import java.util.*;

/**
 * DummyScheduler is the core of the algorithm. Here is where all of the actual scheduling takes place once the parser reads the information
 * and the data structure is built.
 *
 * Our Algorithm: best fit, greedy approach.
 * First we take each panel and calculate it's difficulty, the difficulty values can be decided on by the user.
 * The choose difficulty values are explained @see setDifficulties(). after each panel has a difficulty value, order the panels by the most difficult.
 * Once the panels are sorted by difficulty, we go into the venues and venueTimes. Each Venue and VenueTimes will be filtered out using scores.
 * Scores are based on each filter for each panel please @see Filter to understand how each venue and venueTime is scored.
 * After each Venue and VenueTimes is scored, order them based on best fit greedy approach for each panel.
 * Now that we have a list of Venue and VenueTimes for each panel take the top one (greedy part).
 * Now the panel is scheduled and the messages for it will be assigned.
 *
 * * If a panel can not be scheduled it will be removed from the que and assign why it was not scheduled with its messages.
 * * As well as added to the unschedulablePanels list.
 *
 * @see ScheduleData for lists.
 *
 */
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

    /**
     * A container, a struct, for the venueTime proposed by the algorithm to place a panel in.
     *
     */
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


    /**
     * Calls @see searchForVenueTime. If an open venueTime is found using the algorithm, assign this panel to that venueTime.
     * Otherwise, add the panel to a list of unscheduled panels
     *
     * @param panel Panel to be scheduled
     *
     */
    private void schedule(Panel panel) {
        SearchResult sr = searchForVenueTime(panel);
        if(sr.isSuccess()) {
            ScheduleData.instance().assignPanelToVenueTime(panel, sr.VENUETIME);
        } else {
            setUnschedulablePanelMessages(panel, sr.CAUSE_OF_FAIL_MAP);
            ScheduleData.instance().cannotSchedule(panel);
        }
    }

    /**
     * Calls @see setDifficulty() to assign a difficulty rating to each panel
     * Then loops through the panels and attempts to schedule them (calls schedule)
     *
     */
    public void makeSchedule() {
        setDifficulties();
        List<Panel> pnlCollection = ScheduleData.instance().getFreePanels();
        while (pnlCollection.size() > 0) {
            this.schedule(pnlCollection.get(0));
        }
        setAssignedPanelsMessages();
    }

    /**
     * For each panel that has been scheduled, generate output messages, which is:
     *  a string given during the scheduling process that will be printed in Report
     *  Each message hold what happened in the process of scheduling a panel, including what violations a panel might have
     *
     */
    public void setAssignedPanelsMessages() {
        for (Panel panel : ScheduleData.instance().getAssignedPanels()) {
            for (Constraint constraint : panel.CONSTRAINTS) {
                if (constraint.isConstraintViolated(panel.getVenueTime())) {
                    panel.addMessage(constraint.toString());
                }
            }
        }
    }

    /**
     * For each unscheduled panel, determine why it wasn't scheduled and generate output pertaining to those reasons,
     * the amount of times each constraint or filter was violated. For the locked panels the method gives what, venue and time was violated.
     * As well as, create a message for the panel.
     *
     * @param panel is the panel in question
     * @param map a map of constraints to the number of times the panel violated that constraint
     */
    public void setUnschedulablePanelMessages(Panel panel, Map<Constraint, Integer> map) {
        for (Constraint key : map.keySet()) {
            panel.addMessage(key + " violated " + map.get(key) + " times");
        }
        if(panel.LOCKED && panel.getVenueTime() == null){
            Venue venue = null;
            int time = -1;
            for(Constraint c : panel.CONSTRAINTS){
                if(c instanceof VenueFilter){
                    venue = ((VenueFilter)c).VENUE;
                } else if(c instanceof SpecificTimeFilter){
                    time = ((SpecificTimeFilter)c).TIME;
                }
            }
            for(VenueTime vt : venue.getAssignedVenueTimes()){
                if(TimeFormat.withinError(vt.TIME.getStart(), time, 1)){
                    panel.addMessage("Cannot schedule, because panel \"" + vt.getAssignedPanel().NAME +
                            "\" is scheduled at the requested venue and time");
                    break;
                }
            }
        }
    }

    /**
     * A data structure that contains a VenueTime and its score.
     * It is used in @see searchForVenueTime() to order the venueTimes by their scores using the filters @see Filter
     *
     */
    private class VenueTimeWithScore implements Comparable<VenueTimeWithScore> {
        public final VenueTime VENUETIME;
        public final int SCORE;
        public VenueTimeWithScore(VenueTime vt, int score) {
            VENUETIME = vt;
            SCORE = score;
        }

        /**
         * <I>Helper Method:</I>
         * If this is larger than other return negative number.
         *
         * @param otherVt the other venueTime to be compared.
         * @return negative if this is first, 0 if equal, and positive if other is first.
         *
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

    /**
     * Given a panel, find the best (through greed) venueTime and venue for it.
     * This entails running filters (@see Filter) on the venueTimes given a panel to produce only ones that are applicable to the panel.
     * Then sort those based on best fit.
     *
     * @param panel panel to be scheduled
     * @return a searchResult object of the proposed venueTime
     */
    private SearchResult searchForVenueTime(Panel panel) {
        Map<Constraint, Integer> requiredViolationMap = new HashMap<>();
        // prepare venueTime map, should be done in schedule data. TODO this is fine for now.
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
         * Each panel is assigned a difficulty value, these difficulty values can be changed by the user.
         * The default values are choosen based on which holds more prioirty.
         *
         * Difficulty is determined by:
         *
         * 1 AVAILABILITY_CONSTRAINT_VALUE: overlap and length of availability
         * 2 PANELISTS_CONSTRAINT_VALUE: count panelists overlap and multiple by the
         * 3 <I>No value</I>: count category overlap using the map
         * 4 SIZE_CONSTRAINT_VALUE: min size of venue
         * 5 VENUE_CONSTRAINT_VALUE: venue constraint, for locked panels
         * 6 TIME_CONSTRAINT_VALUE: time constraint
         *
         *
         * To make sure the locked panels are always at the top the method take them out of the que then add them again on top
         */
        public void setDifficulties() {

            List<Panel> freePanels = ScheduleData.instance().getFreePanels();
            List<Panel> lockedPanels = new ArrayList<>();
            /**
             * Runs the methods @see panelistDifficultyMap and @see categoryDifficultyMap
             * Then assign them to HashMap's, each HashMap will be iterated through to calculate the difficulty for each panel
             */
            HashMap<String, Integer> panelistDifficulty = panelistDifficultyMap(freePanels);
            HashMap<String, Integer> categoryDifficulty = categoryDifficultyMap(freePanels);
            int panelDifficulty;

            // remove locked from freePanels
            boolean venueConst = false;
            boolean timeConst = false;
            for (Panel p : freePanels) {
                for (Constraint c : p.CONSTRAINTS) {
                    if (c instanceof VenueFilter) {
                        venueConst = true;
                    }
                    if (c instanceof SpecificTimeFilter) {
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

            // iterated through to calculate the difficulty for each panel
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
            freePanels.addAll(0, lockedPanels);
        }

    /**
     * Bases difficulty on the amount of times the panel can be scheduled on
     *
     * @param panel calculating difficulty for
     * @return float; the larger the number indicates less times the panel can be placed in
     */
        protected int availabilityDifficulty(Panel panel) {
            Range range = panel.getAvailability();
            return AVAILABILITY_CONSTRAINT_VALUE / range.length();
        }

    /**
     * Adds a the difficulty value VENUE_CONSTRAINT_VALUE if the panel is assigned to a specific Venue.
     *
     * @param panel calculating difficulty for
     * @return int; VENUE_CONSTRAINT_VALUE or 0 if the panel has no Venue constraint.
     */
        protected int venueConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof VenueFilter) {
                    return VENUE_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

    /**
     * AAdds a the difficulty value TIME_CONSTRAINT_VALUE if the panel is assigned to a specific Time.
     *
     * @param panel calculating difficulty for
     * @return int; TIME_CONSTRAINT_VALUE or 0 if the panel has no Time constraint.
     */
        protected int TimeConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof SpecificTimeFilter) {
                    return TIME_CONSTRAINT_VALUE;
                }
            }
            return 0;
        }

    /**
     * Determines difficulty based on how large of a venue is needed; the larger, the more difficult
     *
     * @param panel calculating difficulty for
     * @return int; SIZE_CONSTRAINT_VALUE or 0 if the panel has no Size constraint.
     */
        protected int sizeConstraintDifficulty(Panel panel) {
            for (Constraint c : panel.CONSTRAINTS) {
                if (c instanceof SizeConstraint) {
                    return SIZE_CONSTRAINT_VALUE * ((SizeConstraint) c).getMinSize();
                }
            }

            return 0;
        }

    /**
     * Adds difficulty based on how many other panels share a category with this one
     *
     * @param panels calculating difficulty for
     * @return HashMap<String, Integer>; overlapping categories to be counted in setDifficulties()
     */
        private HashMap<String, Integer> categoryDifficultyMap(List<Panel> panels) {
            HashMap<String, Integer> map = new HashMap<>();
            for (Panel panel : panels) {
                for (String category : panel.CATEGORIES) {
                    if (map.containsKey(category)) {
                        map.put(category, map.get(category) + 1);
                    } else {
                        map.put(category, 1);
                    }
                }
            }
            return map;
        }

    /**
     * Adds difficulty based on how many times the panel's panelists appear in other panels.
     *
     * @param panels calculating difficulty for
     * @return HashMap<String, Integer>; overlapping panelists to be counted in setDifficulties()
     */
        private HashMap<String, Integer> panelistDifficultyMap(List<Panel> panels) {
            HashMap<String, Integer> map = new HashMap<>();
            for (Panel panel : panels) {
                for (String panelist : panel.PANELISTS) {
                    if (map.containsKey(panelist)) {
                        map.put(panelist, map.get(panelist) + 1);
                    } else {
                        map.put(panelist, 1);
                    }
                }
            }
            return map;
        }
}
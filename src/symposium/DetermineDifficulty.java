package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;
import symposium.model.Range;
import symposium.model.TimeRange;

import java.util.*;

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

//to use, call panelistDifficulty and categoryDifficulty
    //Then loop through each panel and call evalDifficulty which then calls all other difficulty methods.
    //This will return an integer. Then, for the panel in question, query the panelistdifficulty and categorydifficulty maps and add that information to the number (after applying modifiers)
    //set this to that panel's difficulty, and repeat
    //sort panels by difficulty

abstract class DetermineDifficulty {
    public static int evalDifficulty(Panel panel) {
        //int i = ScheduleData.instance().VENUES.size(); // do somehting from ScheduleData, but why? What does this do?
        int difficulty = availabilityDifficulty(panel) + venueConstraintDifficulty(panel) + sizeConstraintDifficulty(panel);
        return difficulty;
    }

    public static int availabilityDifficulty(Panel panel){
        Range range = panel.getAvailability();
        Iterator<TimeRange> rangeIterator = range.iterator();
        TimeRange timeRange = rangeIterator.next();
        int total = 0;
        while (rangeIterator.hasNext()){
            int start = timeRange.getStart();
            int end = timeRange.getEnd();
            total += (end - start);
            timeRange = rangeIterator.next();
        }
        int avail = (int) 10000/total;
        return avail;
    }

    public static HashMap panelistDifficulty(List<Panel> panels) {
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

    public static int venueConstraintDifficulty(Panel panel) {
        if (panel.hasVenueConstraint()) {
            return 200;
        }
        else {
            return 0;
        }
    }

    public static int sizeConstraintDifficulty(Panel panel) {
        return 10*panel.getSizeConstraint();
    }

    public static HashMap constraintDifficulty(List<Panel> panels) {
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

























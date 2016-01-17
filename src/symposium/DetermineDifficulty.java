package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;
import symposium.model.Range;
import symposium.model.TimeRange;

import java.util.Iterator;

/**
 * Difficulty is determined by :
 *
 * 1 :   : overlap and length of availability
 * 2 :   : panelists overlap
 * 3 :   : min size of venue
 * 4 :   : locked
 * 5 :   : number and priority of constraint
 * 6?:   : category overlap
 *
 *
 * 1: Required * 100
 * 2: Very important * 10
 * 3: Desirable * 1
 *
 * X = order of magnitude
 */


/**
 * TO DO: Method for Availability length: X/Availability (might want a realluy high x here and then round so we don't need to deal with decimals and it is on the same scale as other difficulties)
 * TO DO: Method for Panelists Overlap length: X * (Sum of Panilists: Panels)
 * TO DO: Method for Min-Size + VenueConstraints 
 *
 */



abstract class DetermineDifficulty {
    public static int evalDifficulty(Panel panel) {
        int i = ScheduleData.instance().VENUES.size(); // do somehting from ScheduleData
        return 0;
    }

    public int availabilityDifficulty(Panel panel){
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
}

























package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;

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
 * TO DO: Method for Availability length: X * (Days * 24 - Availability )
 * TO DO: Method for Panelists Overlap length: X * (Sum of Panilists: Panels)
 * TO DO: Method for Min-Size + VenueConstraints 
 *
 */



abstract class DetermineDifficulty {
    public static int evalDifficulty(Panel panel) {
        int i = ScheduleData.instance().VENUES.size(); // do somehting from ScheduleData
        return 0;
    }
}
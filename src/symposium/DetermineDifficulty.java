package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;

/**
 * Created by Christopher on 1/14/2016.
 */
abstract class DetermineDifficulty {
    public static int evalDifficulty(Panel panel) {
        int i = ScheduleData.instance().VENUES.size(); // do somehting from ScheduleData
        return 0;
    }
}
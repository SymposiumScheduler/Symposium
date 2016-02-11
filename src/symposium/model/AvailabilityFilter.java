package symposium.model;

import java.util.Iterator;
import java.util.Map;

public class AvailabilityFilter extends Filter {
    public AvailabilityFilter(ConstraintPriority priority, Panel p) {
        super(priority, p);
    }

    @Override
    public void filter(Map<VenueTime, Integer> vtMap) {
        Iterator<Map.Entry<VenueTime,Integer>> iter = vtMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<VenueTime,Integer> entry = iter.next();
            if(this.isConstraintViolated(entry.getKey()) && this.PRIORITY == ConstraintPriority.REQUIRED) {
                // TODO : doesn't do anything if it's not required. Low priority since it's impossible to be not required
                iter.remove(); // remove vt from map
            }
        }
    }

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return ! this.PANEL.AVAILABILITY.doesEnclose(venueTime.TIME);
    }
}

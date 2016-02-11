package symposium.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AvailabilityFilter extends Filter {
    public AvailabilityFilter(ConstraintPriority priority, Panel p) {
        super(priority, p);
    }

    @Override
    public void filter(Map<VenueTime, Integer> vtMap) {
        Set<VenueTime> keys = new HashSet<>(vtMap.keySet());
        for ( VenueTime vt : keys) {
            if(this.isConstraintViolated(vt) && this.PRIORITY == ConstraintPriority.REQUIRED) {
                // TODO : doesn't do anything if it's not required. Low priority since it's impossible to be not required
                vtMap.remove(vt);
            }
        }
    }

    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        return ! this.PANEL.AVAILABILITY.doesEnclose(venueTime.TIME);
    }
}

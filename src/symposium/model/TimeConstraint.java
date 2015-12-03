package symposium.model;

import java.sql.Time;

abstract class TimeConstraint extends Constraint {

    public TimeConstraint(short priority, Panel p) {
        super(priority,p);
    }
    /**
     * Dependencies: checkTime method
     * @param venueTime
     * @return true if checkTime is false, returns false if checkTime is true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if (checkTime(venueTime)) {
            violated = false;
        }
        else {
            violated = true;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    private boolean checkTime(VenueTime vt) {
        throw new UnsupportedOperationException();
    }
}
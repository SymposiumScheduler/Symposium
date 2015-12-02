package symposium.model;

abstract class TimeConstraint extends Constraint {

    /**
     * Dependencies: checkTime method
     * @param venueTime
     * @return true if checkTime is false, returns false if checkTime is true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated;
        if checkTime(venueTime) {
            violated = false;
        }
        else {
            violated = true;
        }
        cache.put(venueTime, violated);
        return violated;
    }

    private boolean checkTime();
}
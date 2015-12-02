package symposium.model;

abstract class TimeConstraint extends Constraint {

    @Override
    public boolean checkViolationCache() {
        return isConstraintViolated(panel.getVenueTime());
    }

    /**
     * Dependencies: checkTime method
     * @param venueTime
     * @return true if checkTime is false, returns false if checkTime is true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if checkTime(venueTime) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean checkTime();
}
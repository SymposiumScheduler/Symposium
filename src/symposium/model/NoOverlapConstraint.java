package symposium.model;

abstract class NoOverlapConstraint extends Constraint {

    @Override
    public boolean checkViolationCache() {
        return isConstraintViolated(panel.getAssigneVenueTime());
    }

    /**
     * Dependencies: (Additionally) doesOvelap method
     * @param venueTime The time being checked by doesOverlap
     * @return If there is a category or panelist scheduled at the same time, returns true, otherwise returns false.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if doesOverlap(venueTime) {
            return true
        }
        else {
            return false
        }
    }

    private boolean doesOverlap(VenueTime venueTime);
}
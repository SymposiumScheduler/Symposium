package symposium.model;

class VenueConstraint extends Constraint {
    Venue venue;

    /**
     * Dependencies: (Additionally) Venue class
     * @param v The venue the panel must appear in.
     */
    @Override
    public VenueConstraint(short priority, Panel p, Venue v) {
        PRIORITY = priority;
        panel = p;
        venue = v;
    }

    @Override
    public boolean checkViolationCache() {
        return isConstraintViolated(panel.getAssignedVenueTime());
    }

    /**
     * Dependencies: (Additionally) venueTime.getAssignedPanel method
     * @param venueTime
     * @return If the panel is assigned to the correct venue, returns false, otherwise returns true.
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        if (venueTime.getAssignedPanel() != venue) {
            return true;
        }
        else {
            return false;
        }
    }
}
package symposium.model;

public abstract class Constraint {
    public short final PRIORITY;
    Panel panel;
    /**
     * Constructs constraint.
     * Dependencies: Panel class, Parser class
     * @param priority enum determining whether constraint is required, important, or desirable.
     * @param p the Panel that the constraint is part of.
     */
    public Constraint(short priority, Panel p){  //Refering to ScheduleData globally?
        PRIORITY = priority;
        panel = p;
    }

    /**
     * As is currently written, is just isConstraintViolated for currently assigned VenueTime.
     * Will be changed later to run isConstraintViolated if no cache for the time exists yet, and just check the cache for true/false if it does.
     * Dependencies: isConstraintViolated method, VenueTime class, panel.getAssignedVenueTime method
     * @return True if constraint is violated at current venueTime, false otherwise.
     */
    public boolean checkViolationCache();

    /**
     * Dependencies: VenueTime class
     * @param venueTime
     * @return True if costraint is violated at prospective venueTime, false otherwise.
     */
    public boolean isConstraintViolated(VenueTime venueTime);
}
package symposium.model;

public class SpecificTimeConstraint extends TimeConstraint {

    int time;

    public SpecificTimeConstraint(ConstraintPriority priority, Panel p, int t) {
        super(priority, p);
        time = t;
    }

    @Override
    public boolean checkTime(VenueTime vt){
        return (vt.TIME.getStart() == time);
    }

    @Override
    public String toString() {
        return "SpecificTimeConstraint: Panel must be scheduled at specific time.";
    }
}

package symposium.model;

/**
 * The class PairedPanelistConstraint inherits from Constraints, @see Constraint for documentation.
 * Paired Panelist Constraint is violated when two panelists appear together twice or more in a single day
 */
public class PairedPanelistConstraint extends Constraint {
    public static String firstPanelist;
    public static String secondPanelist;

    /**
     * Constructs for the PairedPanelistConstraint class.
     *
     * @param priority enum which determines if a constraint is REQUIRED, VERY_IMPORTANT, or DESIRED.
     * @param panel    The Panel that the constraint is part of.
     */
    public PairedPanelistConstraint(ConstraintPriority priority, Panel panel) {
        super(priority,panel);
    }

    /**
     * <b>Dependencies:</b> ScheduleData class, ScheduleData.timesAssignedTogetherDay method, VenueTime class
     *
     * This method loops through the panel's panelists and checks if two panelists appears more than once per day.
     * The method uses timesAssignedTogetherDay which takes in a venueTime and two panelists
     * timesAssignedTogetherDay: returns the number of times the panelists appear together on a given day.
     * If this number is larger than or equal to 2, the constraint is violated.
     *
     * When the method finds the panelists violating, it records the two panelists and assign it to firstPanelist and secondPanelist.
     * @param venueTime The time being checked
     * @return boolean; true if two panelists appear together twice or more in a day, otherwise, false
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated = false;
        for (int i = 0; i < PANEL.PANELISTS.size(); i++) {
            for (int j = 0; j < PANEL.PANELISTS.size(); j++) {
                if ((i != j)) {
                    if ((ScheduleData.instance().timesAssignedTogetherDay(venueTime, PANEL.PANELISTS.get(i)
                            , PANEL.PANELISTS.get(j)) >= 2)) {
                        //Another function that will be written later in scheduleData
                        violated = true;
                        firstPanelist = PANEL.PANELISTS.get(i);
                        secondPanelist = PANEL.PANELISTS.get(j);

                    }
                }
            }
        }
        cache.put(venueTime, violated);
        return violated;
    }

    /**
     * @return String of the violation message
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Paired Panelist Constraint Violated: (Two panelists shouldn't appear together twice or more per day)").append("\n");
        sb.append("\t\t\t").append("Panelists are: ").append(firstPanelist).append(" , ").append(secondPanelist);
        return sb.toString();
    }
}
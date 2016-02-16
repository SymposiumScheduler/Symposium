package symposium.model;

public class PairedPanelistConstraint extends Constraint {
    public static String firstPanelist;
    public static String secondPanelist;
    public PairedPanelistConstraint(ConstraintPriority priority, Panel p) {
        super(priority,p);
    }
    /**
     * Dependencies: ScheduleData class, ScheduleData.timesAssignedTogetherDay method, VenueTime class
     * @param venueTime
     * @return boolean; returns true if two panelists appear together twice or more in a day, false if otherwise
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Paired Panelist Constraint Violated: (Two panelists shouldn't appear together more than once per day)").append("\n");
        sb.append("\t\t\t").append("Panelists are: ").append(firstPanelist).append(" , ").append(secondPanelist);
        return sb.toString();
    }
}
package symposium.model;

class PairedPanelistConstraint extends Constraint {
    public PairedPanelistConstraint(short priority, Panel p) {
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
                if ((ScheduleData.INSTANCE.timesAssignedTogetherDay(venueTime, PANEL.PANELISTS.get(i)
                        , PANEL.PANELISTS.get(j)) >= 2)  && (i != j)) {  //Another function that will be written later in scheduleData
                    violated = true;
                }
            }
        }
        cache.put(venueTime, violated);
        return violated;
    }
}
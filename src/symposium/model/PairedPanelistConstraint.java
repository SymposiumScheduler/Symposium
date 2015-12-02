package symposium.model;

class PairedPanelistConstraint extends Constraint {

    /**
     * Dependencies: ScheduleData class, ScheduleData.timesAssignedTogetherDay method, VenueTime class
     * @param venueTime
     * @return boolean; returns true if two panelists appear together twice or more in a day, false if otherwise
     */
    @Override
    public boolean isConstraintViolated(VenueTime venueTime) {
        boolean violated = false;
        for (i = 0; i < panel.PANELIST.size(); i++) {
            for (j = 0; j < panel.PANELIST.size(); j++) {
                if (ScheduleData.timesAssignedTogetherDay(venueTime, panel.PANELIST[i], panel.PANELIST) >= 2)  && (i != j) {  //Another function that will be written later in scheduleData
                    violated = true;
                }
            }
        }
        return violated;
    }
}
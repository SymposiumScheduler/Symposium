package symposium.model;

class PairedPanelistConstraint extends Constraint {

    @Override
    public boolean checkViolationCache() {

    }

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
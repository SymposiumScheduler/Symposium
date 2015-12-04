package symposium.model;

class NewPanelistConstraint extends TimeConstraint { //Ask team members

    public NewPanelistConstraint(short priority, Panel p) {
        super(priority, p);
    }

    /**
     * Dependencies: Range interface, TimeRange class, Panel class, Panel.PANELIST variable
     * @return true if panel doesn't fall on monday OR there is one new panelist or for fewer, otherwise returns false
     */
    @Override
    boolean checkTime(VenueTime venueTime) {
        Range time = venueTime.TIME; // Implement in VenueTime later
        Range monday = new TimeRange(0, 1440); // Assuming Joey's current parser, change if implementation changes
        short newMoreThanTwo = 0;
        for (int i = 0; i < PANEL.PANELISTS.size(); i++) {
            if(PANEL.PANELISTS.get(i).contains("new")) {  // built on assumption that the PANELIST array in panel contains the substring
                newMoreThanTwo++;
            }
        }
        if (!time.doesIntersect(monday) || newMoreThanTwo < 2) {
            return true;
        }
        else {
            return false;
        }
    }
}
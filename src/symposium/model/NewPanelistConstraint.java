package symposium.model;

class NewPanelistConstraint extends TimeConstraint { //Ask team members

    /**
     * Dependencies: Range interface, TimeRange class, Panel class, Panel.PANELIST variable
     * @return true if panel doesn't fall on monday OR there is one new panelist or for fewer, otherwise returns false
     */
    @Override
    private boolean checkTime() {
        Range time = venueTime.getTime(); // Implement in VenueTime later
        Range monday = new TimeRange(0, 1440); // Assuming Joey's current parser, change if implementation changes
        short newMoreThanTwo = 0;
        for (for i = 0; i < panel.PANELIST.size(); i++) {
            if panel.PANELIST[i].contains('new') {  // built on assumption that the PANELIST array in panel contains the substring
                newMoreThanTwo++;
            }
        }
        if (!time.Intersect(monday) || newMoreThanTwo < 2) {
            return true;
        }
        else {
            return false;
        }
    }
}
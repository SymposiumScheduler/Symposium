package symposium.model;


import java.util.*;


public class ScheduleData {
    // singleton : see init() and instance()
    private static ScheduleData instance;

    public final List<Venue> VENUES;
    public final int NUMBER_OF_DAYS;
    private final Comparator<Panel> panelTimeComparator = new Comparator<Panel>() {
        @Override
        public int compare(Panel firstPanel, Panel secondPanel) {
            return firstPanel.getVenueTime().TIME.compareTo(secondPanel.getVenueTime().TIME);
        }
    };


    private final Map<String, List<Panel>> categoryAssigned;
    private final Map<String, List<Panel>> panelistAssigned;

    /**
     * sortedSets because duplication is not allowed in these collections.
     */
    private final List<Panel> assignedPanels;
    private final List<Panel> freePanels;

    /**
     * @param venues
     * @param panels
     */
    public ScheduleData(List<Venue> venues, List<Panel> panels, int numberOfDays) {
        this.VENUES = Collections.unmodifiableList(venues);

        this.freePanels = new ArrayList<>(panels);
        this.assignedPanels = new ArrayList<>();

        this.categoryAssigned = new HashMap<>();
        this.panelistAssigned = new HashMap<>();

        this.NUMBER_OF_DAYS = numberOfDays;
    }

    /**
     * change a panel status from free to assigned.
     * @param panel panel to be changed to assigned status
     */
    public void changeToAssigned(Panel panel) {
        if(freePanels.contains(panel)) {
            freePanels.remove(panel);
            assignedPanels.add(panel);

            // update panelists map
            for ( String panelist : panel.PANELISTS) {

                if(!panelistAssigned.containsKey(panelist)) {
                    panelistAssigned.put(panelist, new ArrayList<>());
                }
                panelistAssigned.get(panelist).add(panel);
                Collections.sort( panelistAssigned.get(panelist), panelTimeComparator);
            }

            // update category map
            for( String category : panel.CATEGORIES ) {
                if(!categoryAssigned.containsKey(category)) {
                    categoryAssigned.put(category, new ArrayList<>());
                }
                categoryAssigned.get(category).add(panel);
                //keep sorted
                Collections.sort( categoryAssigned.get(category), panelTimeComparator);
            }



        }
    }

    /**
     * change a panel status from assigned to free.
     * @param panel panel to be changed to free status
     *
     */
    public void changeToFree(Panel panel) {
        if(assignedPanels.contains(panel)) {
            assignedPanels.remove(panel);
            freePanels.add(panel);

            // update panelists map
            for ( String panelist : panel.PANELISTS) {
                panelistAssigned.get(panelist).remove(panel);
            }
            // update category map
            for ( String category : panel.CATEGORIES) {
                panelistAssigned.get(category).remove(panel);
            }
        }
    }


    public boolean isAssignedPanelists(VenueTime vt, List<String> panelists) {
        boolean panelistOverlap = false;
        for (int i = 0; i < panelists.size(); i++) {
            String panelist = panelists.get(i);
            List<Panel> p = this.panelistAssigned.get(panelist);
            if (this.panelistAssigned.get(panelist) != null) {
                for (int j = 0; j < p.size(); j++) {
                    if (p.get(j).getVenueTime() != null) {
                        if (p.get(j).getVenueTime().TIME.doesIntersect(vt.TIME)) {
                            panelistOverlap = true;
                            break;
                        }
                    }
                }
            }
        }
        return panelistOverlap;
    }
    public boolean isAssignedCategories(VenueTime vt, List<String> categories) {
        boolean categoryOverlap = false;
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            List<Panel> p = this.categoryAssigned.get(category);
            if (this.categoryAssigned.get(category) != null) {
                for (int j = 0; j < p.size(); j++) {
                    if(p.get(j).getVenueTime() != null) {
                        if (p.get(j).getVenueTime().TIME.doesIntersect(vt.TIME)) {
                            categoryOverlap = true;
                            break;
                        }
                    }
                }
            }
        }
        return categoryOverlap;
    }

    public int timesAssignedTogetherDay(VenueTime vt, String p1, String p2) {
        int assigned = 0;
        int start = (vt.TIME.getStart() / 1440)*1440; //Division Should be floor
        int end = start + 1440;
        Range day = new TimeRange(start, end);
        if (panelistAssigned.get(p1) == null || panelistAssigned.get(p2) == null) {
            return assigned;
        }
        else {
            List<Panel> appears1 = panelistAssigned.get(p1);
            List<Panel> appears2 = panelistAssigned.get(p2);
            for (int i = 0; i < appears1.size(); i++) {
                for (int j = 0; j < appears2.size(); j++) {
                    if (appears1.get(i) == appears2.get(j)) {
                        Panel appear = appears1.get(i);
                        if (appear.getVenueTime() != null) {
                            if (appear.getVenueTime().TIME.doesIntersect(day)) {
                                assigned++;
                            }
                        }
                    }
                }
            }
            return assigned;
        }
    }

    public void assignPanelToVenueTime(Panel p, VenueTime vt) {
        p.setVenueTime(vt);
        vt.assignPanel(p);

        vt.VENUE.changeToAssigned(vt);
        this.changeToAssigned(p);
    }

    public List<Panel> getFreePanels() {
        return this.freePanels;
    }
    public List<Panel> getAssignedPanels() {
        return this.assignedPanels;
    }
    public List<Panel> getPanelistAssignedPanels(String panelist) { return this.panelistAssigned.get(panelist);}
    public void cannotSchedule(Panel p) {
        getFreePanels().remove(p);
    }

    public static void init(List<Venue> venues, List<Panel> panels, int noOfDays){
        if(instance == null) {
            instance = new ScheduleData(venues, panels, noOfDays);
        }
    }

    public static ScheduleData instance() {
        return ScheduleData.instance;
    }
}

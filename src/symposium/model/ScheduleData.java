package symposium.model;


import java.util.*;


public class ScheduleData {
    public final List<Venue> VENUES;

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
    private final SortedSet<Panel> assignedPanels;
    private final SortedSet<Panel> freePanels;

    /**
     * @param venues
     * @param panels
     */
    public ScheduleData(List<Venue> venues, List<Panel> panels) {
        this.VENUES = Collections.unmodifiableList(venues);

        this.freePanels = new TreeSet<>(panels);
        this.assignedPanels = new TreeSet<>();

        this.categoryAssigned = new HashMap<>();
        this.panelistAssigned = new HashMap<>();
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
                Collections.sort( categoryAssigned.get(panelist), panelTimeComparator);
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
}

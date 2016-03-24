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

    private final List<String> warningMessages;

    /**
     * sortedSets because duplication is not allowed in these collections.
     */
    private final List<Panel> assignedPanels;
    private final List<Panel> freePanels;
    private final List<Panel> unschedulablePanels;

    /**
     * <b>Dependencies:</b> Venue class, Panel class
     *
     * ScheduleData constructor; initializes internal variables, assigns list of venues,
     * assigns number of days Schedule will be in length.
     *
     * @param venues
     * @param numberOfDays
     */
    public ScheduleData(List<Venue> venues, int numberOfDays) {
        this.VENUES = Collections.unmodifiableList(venues);

        this.freePanels = new ArrayList<>();
        this.assignedPanels = new ArrayList<>();
        this.unschedulablePanels = new ArrayList<>();

        this.categoryAssigned = new HashMap<>();
        this.panelistAssigned = new HashMap<>();

        this.NUMBER_OF_DAYS = numberOfDays;

        this.warningMessages = new ArrayList<>();

    }

    /**
     * Change a panel's status from free to assigned, updates relevant actors.
     *
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
     * Unused
     *
     * change a panel status from assigned to free.
     * @param panel panel to be changed to free status
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

    /**
     * <b>Dependencies:</b> VenueTime class, Panel class, Panel.getVenueTime method, ScheduleData.panelistAssigned method, Range interface, Range.doesIntersect method
     *
     * Checks a list of panelists and a venueTime; returns true if any panelists are scheduled at a venueTime that isn't the parameter vt
     * and the the venueTime and vt overlap.
     *
     * @param vt; The venuetime all panelists are assumed to be scheduled at.
     * @param panelists; A list of all panelists whose availability needs to be checked.
     * @return boolean; returns true if there is any panelist that is scheduled for a venueTime that overlaps with vt.
     */
    public boolean isAssignedPanelists(VenueTime vt, List<String> panelists) {
        boolean panelistOverlap = false;
        for (int i = 0; i < panelists.size(); i++) {
            String panelist = panelists.get(i);
            List<Panel> p = this.panelistAssigned.get(panelist);
            if (this.panelistAssigned.get(panelist) != null) {
                for (int j = 0; j < p.size(); j++) {
                    if (p.get(j).getVenueTime() != null && p.get(j).getVenueTime() != vt) {
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

    /**
     * <b>Dependencies:</b> VenueTime class, Panel class, Panel.getVenueTime method,
     * Range interface, Range.doesIntersect method
     *
     * Checks a category and a venueTime; returns true if the category is scheduled for a venueTime that isn't the parameter vt
     * and the the venueTime and vt overlap.
     *
     * @param vt; The venuetime to be checked.
     * @param categories; The category to check for avaliability.
     * @return boolean; returns true if the category is scheduled for a venueTime that overlaps with vt.
     */
    public boolean isAssignedCategories(VenueTime vt, List<String> categories) {
        boolean categoryOverlap = false;
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            List<Panel> p = this.categoryAssigned.get(category);
            if (this.categoryAssigned.get(category) != null) {
                for (int j = 0; j < p.size(); j++) {
                    if(p.get(j).getVenueTime() != null && p.get(j).getVenueTime() != vt) {
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

    /**
     * <b>Dependencies:</b> VenueTime class, Panel class, Range interface,
     * Range.getStart method, Range.doesIntersect method, Panel.getVenueTime method
     *
     * Takes a venueTime, which from which the day the venueTime occurs during is calculated, and two panelists.
     * Totals the number of times that day those two panelists appear together.
     *
     * @param vt; the venueTime from which day is calculated.
     * @param p1; the first panelist to check.
     * @param p2; the second panelist to check.
     * @return int; the number of times the panelists appear together that day.
     */
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
                    if (appears1.get(i).equals(appears2.get(j))) {
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

    /**
     * @param panels; List of Panels to be initialized as freePanels, done as part of ScheduleData initialization.
     */
    public void initPanels(List<Panel> panels) {
        this.freePanels.addAll(panels);
    }

    /**
     * <b>Dependencies:</b> Panel class, VenueTime class, Panel.setVenueTime method, VenueTime.assignPanel method,
     * Venue class, Venue.changeToAssigned method, ScheduleData.changeToAssigned method
     *
     * Schedules a panel, updates all relevant actors.
     *
     * @param p; Panel to be scheduled.
     * @param vt; VenueTime to be scheduled at.
     */
    public void assignPanelToVenueTime(Panel p, VenueTime vt) {
        p.setVenueTime(vt);
        vt.assignPanel(p);

        vt.VENUE.changeToAssigned(vt);
        this.changeToAssigned(p);
    }

    /**
     * <b>Dependencies:</b> Panel class
     *
     * @return int[2]; Returns int[0] that is the total number of messages assignedPanels and unschedulablePanels has, and int[1] that is the number of unschedulable panels.
     */
    public int[] calculateScheduleOptimization() {
        int count = 0;
        for (Panel p: assignedPanels) {
            count += p.getMessages().size();
        }
        for (Panel p: unschedulablePanels) {
            count += p.getMessages().size();
        }
        int[] r = new int[2];
        r[0] = count;
        r[1] = unschedulablePanels.size();
        return r;
    }

    /**
     * <b>Dependencies:</b> Panel class, ScheduleData.getAssignedPanels method, ScheduleData.getPanelistAssignedPanels method,
     * ScheduleData.getFreePanels method, TimeFormat.getNumberOfDay method, Panel.getVenueTime method
     *
     * Calculated from the union of the panelist's panel's availabilities no the availability of the panelist themselves
     *
     * @return map from panelist to set of days which they are not scheduled
     */
    public Map<String, Set<Integer>> getPanelistsUnscheduledDays() {
        // TODO : implementation is bad and slow. Better implementation needed!.


        Map<String, Set<Integer>> resultMap = new HashMap<String,Set<Integer>>();
            Set<String> checked = new HashSet<>();

            for(Panel p : this.getAssignedPanels()) {
                for(String pnst : p.PANELISTS) {
                    if(checked.contains(pnst)) {
                        continue;
                    }
                    //
                    Set<Integer> daysThisPanelistIsAvailable = new HashSet<>();
                    // find days of availability for pnst {{{
                    // assigned
                    for(Panel pnstP: ScheduleData.instance().getPanelistAssignedPanels(pnst)){
                        Iterator<TimeRange> itr = pnstP.AVAILABILITY.iterator();
                        while(itr.hasNext()) {
                            daysThisPanelistIsAvailable.add(TimeFormat.getNumberOfDay(itr.next().START));
                        }
                    }
                    // un assigned
                    for(Panel pnstP: ScheduleData.instance().getFreePanels()){
                        if(!pnstP.PANELISTS.contains(pnstP) ){
                            continue;
                        }
                        Iterator<TimeRange> itr = pnstP.AVAILABILITY.iterator();
                        while(itr.hasNext()) {
                            daysThisPanelistIsAvailable.add(TimeFormat.getNumberOfDay(itr.next().START));
                        }
                    }
                    // }}}


                    pnstDayLoop : for(int day : daysThisPanelistIsAvailable) {
                        for(Panel dayP : this.getPanelistAssignedPanels(pnst) ){
                            if(dayP.getVenueTime().getDay() == day) {
                                continue pnstDayLoop;
                            }
                        }

                        if(resultMap.containsKey(pnst)) {
                            resultMap.get(pnst).add(day);
                        } else {
                            resultMap.put(pnst, new HashSet<Integer>());
                            resultMap.get(pnst).add(day);
                        }
                    }

                    //
                    checked.add(pnst);
                }
            }

        return resultMap;
    }

    /**
     * <b>Dependencies:</b> Panel class, Panel.getVenueTime method, VenueTime class, VenueTime.getDay method
     *
     * Gives the number of times a given panelist is scheduled during a given day.
     *
     * @param day; the day to check
     * @param panelist; the panelist to check
     * @return int; the number of times panelist appears that day.
     */
    public int getPanelistAppearanceNo(int day, String panelist) {
        if(panelistAssigned.get(panelist) == null) {
            return 0;
        }
        int count = 0;
        for (Panel p: panelistAssigned.get(panelist)) {
            if (p.getVenueTime().getDay() == day) {
                count++;
            }
        }
        return count;
    }

    public List<Panel> getFreePanels() {
        return this.freePanels;
    }
    public List<Panel> getAssignedPanels() {
        return this.assignedPanels;
    }
    public List<Panel> getUnschedulablePanels() {
        return this.unschedulablePanels;
    }
    public List<Panel> getPanelistAssignedPanels(String panelist) { return this.panelistAssigned.get(panelist);}

    /**
     * <b>Dependencies:</b> Panel class, ScheduleData.getFreePanels method
     *
     * Lists a panel as Unschedulable for error messaging purposes.
     *
     * @param p; Panel that cannot be scheduled.
     */
    public void cannotSchedule(Panel p) {
        getFreePanels().remove(p);
        unschedulablePanels.add(p);
    }

    /**
     * @param message; Adds this string to list of warningMessages
     */
    public void addWarningMessage(String message) {
        warningMessages.add(message);
    }

    /**
     * @return Returns list of all warningMessages.
     */
    public List<String> getWarningMessages() {
        return this.warningMessages;
    }

    /**
     * <b>Dependencies:</b> Venue class
     *
     * Creates ScheduleData singleton if it doesn't already exist.
     *
     * @param venues; Venues that panels can be scheduled in.
     * @param noOfDays; Number of days Schedule is expected to run.
     */
    public static void init(List<Venue> venues, int noOfDays){
        if(instance == null) {
            instance = new ScheduleData(venues, noOfDays);
        }
    }

    /**
     * Deletes the current ScheduleData singleton.
     */
    public static void deleteScheduleData() {
        System.err.println("ScheduleData instance is being deleted.");
        instance = null;
    }


    /**
     * @return ScheduleData; the Singleton.
     */
    public static ScheduleData instance() {
        return ScheduleData.instance;
    }
}

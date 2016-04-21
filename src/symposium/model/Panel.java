package symposium.model;

import java.util.*;

/**
 * Panel stores information about Panels, such as name, constraints, category, the panel's availability based on
 * the panelists, and messages of the violations a panel might have.
 */
public class Panel implements Comparable<Panel> {
    public final String NAME;
    public final Range AVAILABILITY;
    public final List<String> PANELISTS;
    public final List<Constraint> CONSTRAINTS;
    public final List<String> CATEGORIES;
    public final boolean LOCKED;
    private final List<String> messages;
    private VenueTime assignedVenueTime;
    private int difficulty;

    /**
     * Constructs for Panel class
     *
     * Create the panel object, setting all of the variables to the given values, check for filters and set locked to TRUE if a filter is found.
     * Locked is set to show that this panel must be placed at a certain time or in a certain panel, which significantly
     * increases its difficulty.
     * @param name of the panel
     * @param panelists are the people assigned to the panel
     * @param availability an intersection of available times from the panelists (calculated in Parser)
     * @param categories the categories of the panel
     * @param constraintsStrs a string list of the names of the constraints assigned to the panel; converted to actual constraints below
     */
    public Panel(String name, List<String> panelists, Range availability, List<String> categories, List<String> constraintsStrs){
        this.NAME = name;
        this.CATEGORIES = categories;
        this.AVAILABILITY = availability;
        this.PANELISTS = panelists;
        this.CONSTRAINTS = ConstraintFactory.buildConstraints(this, constraintsStrs);
        this.messages = new ArrayList<>();
        int ctr = 0;
        boolean tmp = false;
        for(Constraint c : CONSTRAINTS){
            if(c instanceof VenueFilter || c instanceof SpecificTimeFilter) {
                ctr++;
            }
            if(ctr == 2){
                tmp = true;
                break;
            }
        }
        this.LOCKED = tmp;
    }

    /**
     * @param difficulty is calculated in DummyScheduler
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return the panel's difficulty value
     */
    public int getDifficulty(){
        return difficulty;
    }

    /**
     * @return the time slot that the panel is scheduled in
     */
    public VenueTime getVenueTime(){
        return assignedVenueTime;
    }

    /**
     * @param venueTime is the proposed venueTime that the panel is being placed in (done in DummyScheduler)
     */
    public void setVenueTime(VenueTime venueTime){
        this.assignedVenueTime = venueTime;
    }

    /**
     * @return the name of the panel
     */
    public String getName(){
        return NAME;
    }

    /**
     * @return the range of time in which this panel is available for scheduling
     */
    public Range getAvailability(){
        return AVAILABILITY;
    }

    /**
     * @param message is a string given during the scheduling process that will be printed in Report
     *                Each message hold what happened in the process of scheduling a panel, including what violations a panel might have
     */
    public void addMessage(String message){
        messages.add(message);
    }

    /**
     * @return returns the list of messages
     */
    public List<String> getMessages() {
        return this.messages;
    }

    /**
     * Compares the difficulties of two panels in order to sort them in descending order
     * @param otherPanel panel that this one is being compared to
     * @return a number that determines which one should come first
     */
    @Override
    public int compareTo(Panel otherPanel) {
        return this.difficulty - otherPanel.difficulty;
    }

    /**
     * @return Returns a string that encapsulates needed information about the Panel, including:
     * Its name, when and where it's been assigned, and any messages it has.
     * Again messages are what happened during the process of scheduling a panel, including what violations a panel might have
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.NAME).append("\n\t");
        if(this.assignedVenueTime != null) {
            sb.append(this.getVenueTime().toString());
        } else {
            sb.append("Unscheduled");
        }
        sb.append("\n\tMessages:");
        if(this.messages.size()!=0) {
            for (String message : this.messages) {
                sb.append("\n\t\t").append(message);
            }
        } else {
            sb.append(" No Messages");
        }
        return sb.toString();
    }

}

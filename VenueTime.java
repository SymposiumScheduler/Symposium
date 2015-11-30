public class VenueTime{
    int start;
    int end;
    TimeRange time;
    Venue venue;
    Panel panel;

    public VenueTime(Venue venue, TimeRange time){
        this.time = time;
        this.venue = venue;
    }

    public void setPanel(Panel panel){
        this.panel = panel;
    }

    public Panel getPanel(){
        return this.panel;
    }

}

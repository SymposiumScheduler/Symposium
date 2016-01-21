package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;
import symposium.model.Venue;
import symposium.model.VenueTime;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;

public class DummyScheduler {
    public DummyScheduler() {}

    private void determineDifficulity() {
        List<Panel> freePanels = ScheduleData.instance().getFreePanels();
        for(Panel p : freePanels) {
            p.setDifficulty(DetermineDifficulty.evalDifficulty(p));
        }
        Collections.sort(freePanels);
        Collections.reverse(freePanels);
    }

    private void schedule(Panel panel) {
        for(Venue v : ScheduleData.instance().VENUES) {
            if(v.getFreeVenueTimes().size() >= 1) {
                ScheduleData.instance().assignPanelToVenueTime(panel,v.getFreeVenueTimes().get(0));
                return;
            }
        }
        Report.INSTANCE.cannotSchedule(panel, "no available venue times");
        ScheduleData.instance().cannotSchedule(panel);
    }

    public void makeSchedule() {
        // init difficulity
        determineDifficulity();
        // go through panels and schedule
        List<Panel> pnlCollection = ScheduleData.instance().getFreePanels();
        while(pnlCollection.size() > 0) {
            this.schedule(pnlCollection.get(0));
        }
        //
        Report.INSTANCE.finishReport();
    }

    public VenueTime returnFirstLegalVenueTime() {
        return null;

    }
}
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
import java.util.HashMap;

public class DummyScheduler {
    private enum ConstraintLevel {
        REQUIRED, VERY_IMPORTANT, DESIRED
    }
    public DummyScheduler() {}

    private void determineDifficulity() {
        List<Panel> freePanels = ScheduleData.instance().getFreePanels();
        HashMap panelistDifficulty = DetermineDifficulty.panelistDifficulty(freePanels);
        HashMap categoryDifficulty = DetermineDifficulty.categoryDifficulty(freePanels);
        int y = 0;
        for(Panel p : freePanels) {
            for (String x : p.PANELISTS){
                y += (int) panelistDifficulty.get(x);
            }
            for (String x : p.CATEGORIES){
                y += (int) categoryDifficulty.get(x);
            }
            p.setDifficulty(DetermineDifficulty.evalDifficulty(p)+y);
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

    private VenueTime returnFirstLegalVenueTime(Panel panel) {
        VenueTime vt = searchForLegalVenueTime(panel, ConstraintLevel.DESIRED); // min requirement is desired
        if (vt == null) {
            vt = searchForLegalVenueTime(panel, ConstraintLevel.VERY_IMPORTANT); // min requirement is important
            if ( vt == null) {
                vt = searchForLegalVenueTime(panel, ConstraintLevel.REQUIRED); // min requirement is required
            }
        }
        return vt;
    }
    private VenueTime searchForLegalVenueTime(Panel panel, ConstraintLevel minRequirement) {
        // minRequirement
        // TODO : just a template
        return null; // no venueTime found
    }
}
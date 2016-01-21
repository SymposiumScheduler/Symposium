package symposium;

import symposium.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

public class DummyScheduler {
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
                for (int i = 0; i < v.getFreeVenueTimes().size(); i++) {
                    boolean noConstraintsViolated = true;
                    for (int j = 0; j < panel.CONSTRAINTS.size(); j++) {
                        if(panel.CONSTRAINTS.get(j).isConstraintViolated(v.getFreeVenueTimes().get(i))){
                            noConstraintsViolated = false;
                        }
                    }
                    if (noConstraintsViolated) {
                        ScheduleData.instance().assignPanelToVenueTime(panel, v.getFreeVenueTimes().get(i));
                        return;
                    }

                }
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
        VenueTime vt = searchForLegalVenueTime(panel, ConstraintPriority.DESIRED); // min requirement is desired
        if (vt == null) {
            vt = searchForLegalVenueTime(panel, ConstraintPriority.VERY_IMPORTANT); // min requirement is important
            if ( vt == null) {
                vt = searchForLegalVenueTime(panel, ConstraintPriority.REQUIRED); // min requirement is required
            }
        }
        return vt;
    }

    private VenueTime searchForLegalVenueTime(Panel panel, ConstraintPriority minRequirement) {
        // minRequirement
        for ( Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                for (Constraint constraint : panel.CONSTRAINTS) {
                    if(constraint.PRIORITY.compareTo(minRequirement) >= 0) {
                        if(constraint.isConstraintViolated(vt)) {
                            break; // ignore the current venueTime and check the next ( continue in venueTime loop)
                        }
                    }
                }
            }
        }
        return null; // no venueTime found
    }
}
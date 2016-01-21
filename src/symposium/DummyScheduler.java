package symposium;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
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
        VenueTime vt = returnFirstLegalVenueTime(panel);
        if (vt != null) {
            ScheduleData.instance().assignPanelToVenueTime(panel,vt);
        } else {
            Report.INSTANCE.cannotSchedule(panel, "no available venue times");
            ScheduleData.instance().cannotSchedule(panel);
        }
    }

    public void makeSchedule() {
        // init difficulity
        determineDifficulity();
        // go through panels and schedule
        List<Panel> pnlCollection = ScheduleData.instance().getFreePanels();
        while(pnlCollection.size() > 0) {
            this.schedule(pnlCollection.get(0));
        }
    }

    private VenueTime returnFirstLegalVenueTime(Panel panel) {
    /*
        TODO :
        Can be optimized by making one iteration and keeping three venueTimes in the same time as iterating.
        DesiredSatisfied, VeryImportantSatisfied, RequiredSatisfied.

        In the iteration, if a DesiredSatisfied venueTime is found, just return it. If we finished the whole venueTime space and cannot find
        DesiredSatisfied then we return VeryImportantSatisfied if it's found. if not, return RequiredSatisfied.
        If no RequiredSatisfied found, then venueTime cannot be found.
*/

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
        boolean noViolations;
        for ( Venue v : ScheduleData.instance().VENUES) {
            for (VenueTime vt : v.getFreeVenueTimes()) {
                noViolations = true;
                for (Constraint constraint : panel.CONSTRAINTS) {
                    if(constraint.PRIORITY.compareTo(minRequirement) >= 0) {
                        if(constraint.isConstraintViolated(vt)) {
                            noViolations = false;
                            break;
                        }
                    }
                }
                if(noViolations){
                    return vt; // vt has passed all priority conditions
                } else {
                    continue;
                }
            }
        }
        return null; // no venueTime found
    }
}
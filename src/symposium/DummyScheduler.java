package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;
import symposium.model.Venue;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;

public class DummyScheduler {
    public DummyScheduler() {
    }

    private void determineDifficulity() {
        /**
         * Difficulty is determined by :
         *     W
         * 1 :   : overlap and length of availability
         * 2 :   : panelists overlap
         * 3 :   :min size of venue
         * 4 : H :locked
         * 5 :   :number and priority of constraint
         * 6?:   :category overlap
         */

        //
        Collection<Panel> freePanels = ScheduleData.instance().getFreePanels();
        for(Panel p : freePanels) {
            p.setDifficulty(1);
        }
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
}

abstract class DetermineDifficulity {
        public static int evalDefficulity(Panel panel) {
            int i = ScheduleData.instance().VENUES.size(); // do somehting from ScheduleData
            return 0;
        }
}
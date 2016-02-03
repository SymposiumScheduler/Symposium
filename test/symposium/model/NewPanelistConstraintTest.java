package symposium.model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class NewPanelistConstraintTest {

    @Test
    public void testConstructor() throws Exception {
        ConstraintPriority priority = ConstraintPriority.REQUIRED;

        String name = "new";
        List<String> panelists = new ArrayList<>(3);
        panelists.add("Trey Lyons");
        panelists.add("Jurek Martin");
        panelists.add("Patrick Boel");
        List<TimeRange> avaliability = new ArrayList<>(5);
        avaliability.add(new TimeRange(360, 1080));
        avaliability.add(new TimeRange(1800, 2520));
        avaliability.add(new TimeRange(3240, 3960));
        avaliability.add(new TimeRange(4680, 5400));
        avaliability.add(new TimeRange(6120, 6840));
        TimeRangeSeries range = new TimeRangeSeries(avaliability);
        List<String> constraints = new ArrayList<>(6);
        constraints.add("New-Panelist:2");
        constraints.add("Paired-Panelists:3");
        constraints.add("Single-Category:2");
        constraints.add("Max-Panels(3):1");
        constraints.add("Min-Panels(1):1");
        constraints.add("Minimum-Capacity(2):2");
        List<String> category = new ArrayList<>(1);
        category.add("IA");

        Venue v = new Venue("North Building", 4, Collections.EMPTY_LIST);

        ScheduleData.init(Arrays.asList(v), 1);

        Panel p = new Panel(name, panelists, range, category, constraints);

        Constraint nPC = new NewPanelistConstraint(priority, p);
        assert(nPC.PRIORITY == ConstraintPriority.REQUIRED);

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void test_isConstraintViolated_true() throws Exception {
        ConstraintPriority priority = ConstraintPriority.REQUIRED;

        String name = "new";
        List<String> panelists = new ArrayList<>(3);
        panelists.add("Trey Lyons");
        panelists.add("Jurek Martin");
        panelists.add("Patrick Boel");
        List<TimeRange> avaliability = new ArrayList<>(5);
        avaliability.add(new TimeRange(360, 1080));
        avaliability.add(new TimeRange(1800, 2520));
        avaliability.add(new TimeRange(3240, 3960));
        avaliability.add(new TimeRange(4680, 5400));
        avaliability.add(new TimeRange(6120, 6840));
        TimeRangeSeries range = new TimeRangeSeries(avaliability);
        List<String> constraints = new ArrayList<>(6);
        constraints.add("New-Panelist:2");
        constraints.add("Paired-Panelists:3");
        constraints.add("Single-Category:2");
        constraints.add("Max-Panels(3):1");
        constraints.add("Min-Panels(1):1");
        constraints.add("Minimum-Capacity(2):2");
        List<String> category = new ArrayList<>(1);
        category.add("IA");

        Venue v2 = new Venue ("South Building", 4, Arrays.asList(new TimeRange(480,540)));

        ScheduleData.init(Arrays.asList(v2), 1);

        Panel p = new Panel(name, panelists, range, category, constraints);

        ScheduleData.instance().initPanels(Arrays.asList(p));

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        NewPanelistConstraint nPC = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof NewPanelistConstraint) {
                nPC = (NewPanelistConstraint) c;
                break;
            }
        }

        if (nPC == null) {
            System.err.println("NewPanelistConstraint never created.");
        }

        assertTrue(nPC.isConstraintViolated());

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void test_isConstraintViolated_false() throws Exception {
        ConstraintPriority priority = ConstraintPriority.DESIRED;

        String name = "new";
        List<String> panelists = new ArrayList<>(3);
        panelists.add("Trey Lyons");
        panelists.add("Jurek Martin");
        panelists.add("Patrick Boel");
        List<TimeRange> avaliability = new ArrayList<>(5);
        avaliability.add(new TimeRange(360, 1080));
        avaliability.add(new TimeRange(1800, 2520));
        avaliability.add(new TimeRange(3240, 3960));
        avaliability.add(new TimeRange(4680, 5400));
        avaliability.add(new TimeRange(6120, 6840));
        TimeRangeSeries range = new TimeRangeSeries(avaliability);
        List<String> constraints = new ArrayList<>(6);
        constraints.add("New-Panelist:2");
        constraints.add("Paired-Panelists:3");
        constraints.add("Single-Category:2");
        constraints.add("Max-Panels(3):1");
        constraints.add("Min-Panels(1):1");
        constraints.add("Minimum-Capacity(2):2");
        List<String> category = new ArrayList<>(1);
        category.add("IA");

        Venue v2 = new Venue ("South Building", 4, Arrays.asList(new TimeRange(1440,1500)));

        ScheduleData.init(Arrays.asList(v2), 1);

        Panel p = new Panel(name, panelists, range, category, constraints);

        ScheduleData.instance().initPanels(Arrays.asList(p));

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        NewPanelistConstraint nPC = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof NewPanelistConstraint) {
                nPC = (NewPanelistConstraint) c;
                break;
            }
        }

        if (nPC == null) {
            System.err.println("NewPanelistConstraint never created.");
        }

        assertFalse(nPC.isConstraintViolated());

        ScheduleData.deleteScheduleData();
    }

}
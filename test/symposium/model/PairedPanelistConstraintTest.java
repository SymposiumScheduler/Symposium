package symposium.model;

import org.junit.After;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PairedPanelistConstraintTest {

    @After
    public void tearDown() {
        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testConstructor() {
        ConstraintPriority priority = ConstraintPriority.VERY_IMPORTANT;

        Venue v1 = new Venue("North Building", 4, Collections.EMPTY_LIST);
        List<Venue> venues = Arrays.asList(v1);

        ScheduleData.init(venues, 4);


        String name1 = "new";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Trey Lyons");
        panelists1.add("Jurek Martin");
        panelists1.add("Patrick Boel");
        List<TimeRange> avaliability1 = new ArrayList<>(5);
        avaliability1.add(new TimeRange(360, 1080));
        avaliability1.add(new TimeRange(1800, 2520));
        avaliability1.add(new TimeRange(3240, 3960));
        avaliability1.add(new TimeRange(4680, 5400));
        avaliability1.add(new TimeRange(6120, 6840));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        List<String> constraints1 = new ArrayList<>(6);
        constraints1.add("New-Panelist:2");
        constraints1.add("Paired-Panelists:3");
        constraints1.add("Single-Category:2");
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        PairedPanelistConstraint pc = new PairedPanelistConstraint(priority, p1);

        assert(pc.PRIORITY == ConstraintPriority.VERY_IMPORTANT);
    }

    @Test
    public void test_isConstraintViolated_true() {

        List<TimeRange> vt1 = new ArrayList<TimeRange>();
        List<TimeRange> vt2 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        vt2.add((new TimeRange(800,900)));

        Venue v1 = new Venue("North Building", 4, vt1);

        Venue v2 = new Venue("South Building", 4, vt2);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new1";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Trey Lyons");
        panelists1.add("Jurek Martin");
        panelists1.add("Patrick Boel");
        List<TimeRange> avaliability1 = new ArrayList<>(5);
        avaliability1.add(new TimeRange(360, 1080));
        avaliability1.add(new TimeRange(1800, 2520));
        avaliability1.add(new TimeRange(3240, 3960));
        avaliability1.add(new TimeRange(4680, 5400));
        avaliability1.add(new TimeRange(6120, 6840));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        List<String> constraints1 = new ArrayList<>(6);
        constraints1.add("New-Panelist:2");
        constraints1.add("Paired-Panelists:3");
        constraints1.add("Single-Category:2");
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Availability:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new2";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Trey Lyons");
        panelists2.add("Jurek Martin");
        panelists2.add("Patrick Boel");
        List<TimeRange> avaliability2 = new ArrayList<>(5);
        avaliability2.add(new TimeRange(360, 1080));
        avaliability2.add(new TimeRange(1800, 2520));
        avaliability2.add(new TimeRange(3240, 3960));
        avaliability2.add(new TimeRange(4680, 5400));
        avaliability2.add(new TimeRange(6120, 6840));
        TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
        List<String> constraints2 = new ArrayList<>(6);
        constraints2.add("New-Panelist:2");
        constraints2.add("Paired-Panelists:3");
        constraints2.add("Single-Category:2");
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Availability:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0));

        PairedPanelistConstraint pc1 = null;
        PairedPanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PairedPanelistConstraint) {
                pc1 = (PairedPanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof PairedPanelistConstraint) {
                pc2 = (PairedPanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PairedPanelistConstraint never initialized");
        }

        assertTrue(pc1.isConstraintViolated());
        assertTrue(pc2.isConstraintViolated());
    }

    @Test
    public void test_isConstraintViolated_false() {

        List<TimeRange> vt1 = Arrays.asList(new TimeRange(600, 700));

        Venue v1 = new Venue("North Building", 4, vt1);

        List<TimeRange> vt2 = Arrays.asList(new TimeRange(600, 700));

        Venue v2 = new Venue("South Building", 4, vt2);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

        String name1 = "new1";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Trey Lyons");
        panelists1.add("Jurek Martin");
        panelists1.add("Patrick Boel");
        List<TimeRange> avaliability1 = new ArrayList<>(5);
        avaliability1.add(new TimeRange(360, 1080));
        avaliability1.add(new TimeRange(1800, 2520));
        avaliability1.add(new TimeRange(3240, 3960));
        avaliability1.add(new TimeRange(4680, 5400));
        avaliability1.add(new TimeRange(6120, 6840));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        List<String> constraints1 = new ArrayList<>(6);
        constraints1.add("New-Panelist:2");
        constraints1.add("Paired-Panelists:3");
        constraints1.add("Single-Category:2");
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Availability:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new2";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Alice");
        panelists2.add("Bob");
        panelists2.add("Clark");
        List<TimeRange> avaliability2 = new ArrayList<>(5);
        avaliability2.add(new TimeRange(360, 1080));
        avaliability2.add(new TimeRange(1800, 2520));
        avaliability2.add(new TimeRange(3240, 3960));
        avaliability2.add(new TimeRange(4680, 5400));
        avaliability2.add(new TimeRange(6120, 6840));
        TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
        List<String> constraints2 = new ArrayList<>(6);
        constraints2.add("New-Panelist:2");
        constraints2.add("Paired-Panelists:3");
        constraints2.add("Single-Category:2");
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Availability:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0));

        PairedPanelistConstraint pc1 = null;
        PairedPanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PairedPanelistConstraint) {
                pc1 = (PairedPanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof PairedPanelistConstraint) {
                pc2 = (PairedPanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PairedPanelistConstraint never initialized");
        }

        assertFalse(pc1.isConstraintViolated());
        assertFalse(pc2.isConstraintViolated());
    }

    @Test
    public void test_isConstraintViolated_false2() {

        List<TimeRange> vt1 = Arrays.asList(new TimeRange(600, 700));

        Venue v1 = new Venue("North Building", 4, vt1);

        List<TimeRange> vt2 = Arrays.asList(new TimeRange(1450, 1500));

        Venue v2 = new Venue("South Building", 4, vt2);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 2);

        String name1 = "new1";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Trey Lyons");
        panelists1.add("Jurek Martin");
        panelists1.add("Patrick Boel");
        List<TimeRange> avaliability1 = new ArrayList<>(5);
        avaliability1.add(new TimeRange(360, 1080));
        avaliability1.add(new TimeRange(1800, 2520));
        avaliability1.add(new TimeRange(3240, 3960));
        avaliability1.add(new TimeRange(4680, 5400));
        avaliability1.add(new TimeRange(6120, 6840));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        List<String> constraints1 = new ArrayList<>(6);
        constraints1.add("New-Panelist:2");
        constraints1.add("Paired-Panelists:3");
        constraints1.add("Single-Category:2");
        constraints1.add("Max-Panels(3):1");
        constraints1.add("Min-Panels(1):1");
        constraints1.add("Minimum-Capacity(2):2");
        constraints1.add("Availability:1");
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);

        String name2 = "new2";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Trey Lyons");
        panelists2.add("Jurek Martin");
        panelists2.add("Patrick Boel");
        List<TimeRange> avaliability2 = new ArrayList<>(5);
        avaliability2.add(new TimeRange(360, 1080));
        avaliability2.add(new TimeRange(1800, 2520));
        avaliability2.add(new TimeRange(3240, 3960));
        avaliability2.add(new TimeRange(4680, 5400));
        avaliability2.add(new TimeRange(6120, 6840));
        TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
        List<String> constraints2 = new ArrayList<>(6);
        constraints2.add("New-Panelist:2");
        constraints2.add("Paired-Panelists:3");
        constraints2.add("Single-Category:2");
        constraints2.add("Max-Panels(3):1");
        constraints2.add("Min-Panels(1):1");
        constraints2.add("Minimum-Capacity(2):2");
        constraints2.add("Availability:1");
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(1).getFreeVenueTimes().get(0));

        PairedPanelistConstraint pc1 = null;
        PairedPanelistConstraint pc2 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof PairedPanelistConstraint) {
                pc1 = (PairedPanelistConstraint) c;
                break;
            }
        }
        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof PairedPanelistConstraint) {
                pc2 = (PairedPanelistConstraint) c;
                break;
            }
        }

        if (pc1 == null || pc2 == null) {
            System.out.println("PairedPanelistConstraint never initialized");
        }

        assertFalse(pc1.isConstraintViolated());
        assertFalse(pc2.isConstraintViolated());
    }

}
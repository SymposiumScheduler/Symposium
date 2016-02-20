package symposium.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConsecutivePanelsConstraintTest {

    @Test
    public void testIsConstraintViolated() throws Exception {

        String name1 = "Panel1";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Panelist1");
        panelists1.add("Panelist2");
        panelists1.add("Panelist3");
        List<TimeRange> avaliability1 = new ArrayList<>(4);
        avaliability1.add(new TimeRange(0, 80));
        avaliability1.add(new TimeRange(100, 180));
        avaliability1.add(new TimeRange(200, 280));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        // ConsecutivePanelsConstraint is assumed for now ToChange if not assumed.
        List<String> constraints1 = new ArrayList<>(0);
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        String name2 = "Panel2";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Panelist1");
        panelists2.add("Panelist2");
        List<TimeRange> avaliability2 = new ArrayList<>(4);
        avaliability2.add(new TimeRange(0, 80));
        avaliability2.add(new TimeRange(100, 180));
        avaliability2.add(new TimeRange(200, 280));
        TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
        List<String> constraints2 = new ArrayList<>(0);
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        String name3 = "Panel3";
        List<String> panelists3 = new ArrayList<>(3);
        panelists3.add("Panelist1");
        List<TimeRange> avaliability3 = new ArrayList<>(4);
        avaliability3.add(new TimeRange(0, 80));
        avaliability3.add(new TimeRange(100, 180));
        avaliability3.add(new TimeRange(200, 280));
        TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
        List<String> constraints3 = new ArrayList<>(0);
        List<String> category3 = new ArrayList<>(1);
        category3.add("IA");

        Venue v = new Venue ("Venue", 4, 2, Arrays.asList(new TimeRange(0, 80), new TimeRange(100, 180), new TimeRange(200, 280)));

        ScheduleData.init(Arrays.asList(v), 1);

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
        Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);

        ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3));

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        ConsecutivePanelsConstraint cPC1 = null;
        ConsecutivePanelsConstraint cPC2 = null;
        ConsecutivePanelsConstraint cPC3 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC1 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC2 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(2).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC3 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        assertTrue(cPC1.isConstraintViolated());
        assertTrue(cPC2.isConstraintViolated());
        assertTrue(cPC3.isConstraintViolated());

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testIsConstraintViolated_IfAtLeastOneSeperatedBy80Minutes() throws Exception{
        String name1 = "Panel1";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Panelist1");
        panelists1.add("Panelist2");
        panelists1.add("Panelist3");
        List<TimeRange> avaliability1 = new ArrayList<>(4);
        avaliability1.add(new TimeRange(0, 80));
        avaliability1.add(new TimeRange(100, 180));
        avaliability1.add(new TimeRange(200, 280));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        // ConsecutivePanelsConstraint is assumed for now ToChange if not assumed.
        List<String> constraints1 = new ArrayList<>(0);
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        String name2 = "Panel2";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Panelist1");
        panelists2.add("Panelist2");
        List<TimeRange> avaliability2 = new ArrayList<>(4);
        avaliability2.add(new TimeRange(0, 80));
        avaliability2.add(new TimeRange(100, 180));
        avaliability2.add(new TimeRange(200, 280));
        TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
        List<String> constraints2 = new ArrayList<>(0);
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        String name3 = "Panel3";
        List<String> panelists3 = new ArrayList<>(3);
        panelists3.add("Panelist1");
        List<TimeRange> avaliability3 = new ArrayList<>(4);
        avaliability3.add(new TimeRange(0, 80));
        avaliability3.add(new TimeRange(100, 180));
        avaliability3.add(new TimeRange(300, 380));
        TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
        List<String> constraints3 = new ArrayList<>(0);
        List<String> category3 = new ArrayList<>(1);
        category3.add("IA");

        Venue v = new Venue ("Venue", 4, 2, Arrays.asList(new TimeRange(0, 80), new TimeRange(100, 180), new TimeRange(300, 380)));

        ScheduleData.init(Arrays.asList(v), 1);

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
        Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);

        ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3));

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        ConsecutivePanelsConstraint cPC1 = null;
        ConsecutivePanelsConstraint cPC2 = null;
        ConsecutivePanelsConstraint cPC3 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC1 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC2 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(2).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC3 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        assertFalse(cPC1.isConstraintViolated());
        assertFalse(cPC2.isConstraintViolated());
        assertFalse(cPC3.isConstraintViolated());

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testIsConstraintViolated_ConsecutiveWithBreak() throws Exception{
        String name1 = "Panel1";
        List<String> panelists1 = new ArrayList<>(3);
        panelists1.add("Panelist1");
        panelists1.add("Panelist2");
        panelists1.add("Panelist3");
        List<TimeRange> avaliability1 = new ArrayList<>(4);
        avaliability1.add(new TimeRange(0, 80));
        avaliability1.add(new TimeRange(100, 180));
        avaliability1.add(new TimeRange(200, 280));
        TimeRangeSeries range1 = new TimeRangeSeries(avaliability1);
        // ConsecutivePanelsConstraint is assumed for now ToChange if not assumed.
        List<String> constraints1 = new ArrayList<>(0);
        List<String> category1 = new ArrayList<>(1);
        category1.add("IA");

        String name2 = "Panel2";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Panelist1");
        panelists2.add("Panelist2");
        List<TimeRange> avaliability2 = new ArrayList<>(4);
        avaliability2.add(new TimeRange(0, 80));
        avaliability2.add(new TimeRange(100, 180));
        avaliability2.add(new TimeRange(200, 280));
        TimeRangeSeries range2 = new TimeRangeSeries(avaliability2);
        List<String> constraints2 = new ArrayList<>(0);
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        String name3 = "Panel3";
        List<String> panelists3 = new ArrayList<>(3);
        panelists3.add("Panelist1");
        List<TimeRange> avaliability3 = new ArrayList<>(4);
        avaliability3.add(new TimeRange(0, 80));
        avaliability3.add(new TimeRange(100, 180));
        avaliability3.add(new TimeRange(300, 380));
        TimeRangeSeries range3 = new TimeRangeSeries(avaliability3);
        List<String> constraints3 = new ArrayList<>(0);
        List<String> category3 = new ArrayList<>(1);
        category3.add("IA");

        String name4 = "Panel4";
        List<String> panelists4 = new ArrayList<>(3);
        panelists4.add("Panelist1");
        List<TimeRange> avaliability4 = new ArrayList<>(4);
        avaliability4.add(new TimeRange(0, 80));
        avaliability4.add(new TimeRange(100, 180));
        avaliability4.add(new TimeRange(300, 380));
        avaliability4.add(new TimeRange(400,480));
        TimeRangeSeries range4 = new TimeRangeSeries(avaliability3);
        List<String> constraints4 = new ArrayList<>(0);
        List<String> category4 = new ArrayList<>(1);
        category4.add("IA");

        Venue v = new Venue ("Venue", 4, 2, Arrays.asList(new TimeRange(0, 80), new TimeRange(100, 180), new TimeRange(300, 380), new TimeRange(400,480)));

        ScheduleData.init(Arrays.asList(v), 1);

        Panel p1 = new Panel(name1, panelists1, range1, category1, constraints1);
        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);
        Panel p3 = new Panel(name3, panelists3, range3, category3, constraints3);
        Panel p4 = new Panel(name4, panelists4, range4, category4, constraints4);

        ScheduleData.instance().initPanels(Arrays.asList(p1, p2, p3, p4));

        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));
        ScheduleData.instance().assignPanelToVenueTime(ScheduleData.instance().getFreePanels().get(0), ScheduleData.instance().VENUES.get(0).getFreeVenueTimes().get(0));

        ConsecutivePanelsConstraint cPC1 = null;
        ConsecutivePanelsConstraint cPC2 = null;
        ConsecutivePanelsConstraint cPC3 = null;
        ConsecutivePanelsConstraint cPC4 = null;

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(0).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC1 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(1).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC2 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(2).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC3 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        for (Constraint c: ScheduleData.instance().getAssignedPanels().get(3).CONSTRAINTS) {
            if (c instanceof ConsecutivePanelsConstraint) {
                cPC4 = (ConsecutivePanelsConstraint) c;
                break;
            }
        }

        assertFalse(cPC1.isConstraintViolated());
        assertFalse(cPC2.isConstraintViolated());
        assertFalse(cPC3.isConstraintViolated());
        assertFalse(cPC4.isConstraintViolated());

        ScheduleData.deleteScheduleData();
    }

}
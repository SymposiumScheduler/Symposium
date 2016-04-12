package symposium.model;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ScheduleDataTest {
    @After
    public void tearDown() {
        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testInitPanels() {
        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2,vt1);

        Venue v2 = new Venue("South Building", 4, 2,vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

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

        String name2 = "new";
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
        List<String> category2 = new ArrayList<>(1);
        category2.add("IA");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);

        assertTrue(ScheduleData.instance().getFreePanels().size() == 2);
    }

    @Test
    public void testChangeToAssigned_ChangeToFree() {
        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2,vt1);

        Venue v2 = new Venue("South Building", 4, 2,vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);
        venues.add(v2);

        ScheduleData.init(venues, 1);

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

        String name2 = "new";
        List<String> panelists2 = new ArrayList<>(3);
        panelists2.add("Johnny Test");
        panelists2.add("Red Raja");
        panelists2.add("Blue Beetle");
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
        List<String> category2 = new ArrayList<>(1);
        category2.add("Blah");

        Panel p2 = new Panel(name2, panelists2, range2, category2, constraints2);

        List<Panel> panels = Arrays.asList(p1, p2);

        ScheduleData.instance().initPanels(panels);
        ScheduleData.instance().changeToAssigned(p1);

        assertTrue(ScheduleData.instance().getFreePanels().size() == 1);
        assertTrue(ScheduleData.instance().getAssignedPanels().size() == 1);

        ScheduleData.instance().changeToFree(p1);

        assertTrue(ScheduleData.instance().getFreePanels().size() == 2);
    }

    @Test
    public void testAssignPanelToVenueTime() {
        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2,vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);

        ScheduleData.init(venues, 1);

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

        List<Panel> panels = Arrays.asList(p1);

        ScheduleData.instance().initPanels(panels);
        ScheduleData.instance().assignPanelToVenueTime(p1, v1.getFreeVenueTimes().get(0));

        assertTrue(ScheduleData.instance().getFreePanels().size() == 0);
        assertTrue(ScheduleData.instance().getAssignedPanels().size() == 1);
    }

    @Test
    public void testPanelistAppearanceNo() {
        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2,vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);

        ScheduleData.init(venues, 1);

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

        List<Panel> panels = Arrays.asList(p1);

        ScheduleData.instance().initPanels(panels);
        ScheduleData.instance().assignPanelToVenueTime(p1, v1.getFreeVenueTimes().get(0));

        assertTrue(ScheduleData.instance().getPanelistAppearanceNo(0,"Trey Lyons") == 1);
        assertTrue(ScheduleData.instance().getPanelistAppearanceNo(1,"Trey Lyons") == 0);
    }

    @Test
    public void testTimesAssignedTogetherDay() {
        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2,vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);

        ScheduleData.init(venues, 1);

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

        List<Panel> panels = Arrays.asList(p1);

        ScheduleData.instance().initPanels(panels);
        ScheduleData.instance().assignPanelToVenueTime(p1, v1.getFreeVenueTimes().get(0));

        assertTrue(ScheduleData.instance().timesAssignedTogetherDay(v1.getAssignedVenueTimes().get(0), "Jurek Martin", "Patrick Boel") == 1);
        assertTrue(ScheduleData.instance().timesAssignedTogetherDay(v1.getAssignedVenueTimes().get(0), "Jurek Martin", "Trey Lyons") == 1);
        assertTrue(ScheduleData.instance().timesAssignedTogetherDay(v1.getAssignedVenueTimes().get(0), "Jurek Martin", "Bob Marley") == 0);
    }

    @Test
    public void testWarnings() {
        List<TimeRange> vt1 = new ArrayList<TimeRange>();

        vt1.add((new TimeRange(600,700)));

        Venue v1 = new Venue("North Building", 4, 2,vt1);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v1);

        ScheduleData.init(venues, 1);
        ScheduleData.instance().addWarningMessage("Test");

        assertTrue(ScheduleData.instance().getWarningMessages().get(0) == "Test");
    }
}

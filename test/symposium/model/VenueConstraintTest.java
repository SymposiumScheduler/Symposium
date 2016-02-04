package symposium.model;

import org.junit.After;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import java.util.Collections;

public class VenueConstraintTest {

    @After
    public void tearDown() {
        ScheduleData.deleteScheduleData();
    }

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

        Constraint vC = new VenueConstraint(priority, p, v);
        assert(vC.PRIORITY == ConstraintPriority.REQUIRED);
    }

    @Test
    public void test_isConstraintViolated_predictive_false() throws Exception {

        Venue v1 = new Venue("V1",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        Venue v2 = new Venue("V2",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        ScheduleData.init(Arrays.asList(v1,v2),1);

        List<String> panelists = Arrays.asList("a","b","c");
        List<String> constraints = Arrays.asList("Venue(V1):1");
        Panel p1 = new Panel("p1", panelists, new TimeRange(0,1000), Arrays.asList("Cat1"), constraints);

        ScheduleData.instance().initPanels(Arrays.asList(p1));

        VenueTime vt = v1.getFreeVenueTimes().get(0);
        Constraint cnst = p1.CONSTRAINTS.get(0);

        assertFalse(cnst.isConstraintViolated(vt));
    }

    @Test
    public void test_isConstraintViolated_predictive_true() throws Exception {

        Venue v1 = new Venue("V1",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        Venue v2 = new Venue("V2",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        ScheduleData.init(Arrays.asList(v1,v2),1);

        List<String> panelists = Arrays.asList("a","b","c");
        List<String> constraints = Arrays.asList("Venue(V1):1");
        Panel p1 = new Panel("p1", panelists, new TimeRange(0,1000), Arrays.asList("Cat1"), constraints);

        ScheduleData.instance().initPanels(Arrays.asList(p1));

        VenueTime vt = v2.getFreeVenueTimes().get(0);
        Constraint cnst = p1.CONSTRAINTS.get(0);

        assertTrue(cnst.isConstraintViolated(vt));
    }


    @Test
    public void test_isConstraintViolated_inPlace_true() throws Exception {

        Venue v1 = new Venue("V1",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        Venue v2 = new Venue("V2",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        ScheduleData.init(Arrays.asList(v1,v2),1);

        List<String> panelists = Arrays.asList("a","b","c");
        List<String> constraints = Arrays.asList("Venue(V1):1");
        Panel p1 = new Panel("p1", panelists, new TimeRange(0,1000), Arrays.asList("Cat1"), constraints);

        ScheduleData.instance().initPanels(Arrays.asList(p1));


        // schedule panel
        ScheduleData.instance().assignPanelToVenueTime(p1,v2.getFreeVenueTimes().get(0));

        // check
        Constraint cnst = p1.CONSTRAINTS.get(0);
        assertTrue(cnst.isConstraintViolated());
    }
    @Test
    public void test_isConstraintViolated_inPlace_false() throws Exception {

        Venue v1 = new Venue("V1",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        Venue v2 = new Venue("V2",1,Arrays.asList(new TimeRange(20,40),new TimeRange(30,40)));
        ScheduleData.init(Arrays.asList(v1,v2),1);

        List<String> panelists = Arrays.asList("a","b","c");
        List<String> constraints = Arrays.asList("Venue(V1):1");
        Panel p1 = new Panel("p1", panelists, new TimeRange(0,1000), Arrays.asList("Cat1"), constraints);

        ScheduleData.instance().initPanels(Arrays.asList(p1));


        // schedule panel
        ScheduleData.instance().assignPanelToVenueTime(p1,v1.getFreeVenueTimes().get(0));

        // check
        Constraint cnst = p1.CONSTRAINTS.get(0);
        assertFalse(cnst.isConstraintViolated());
    }
}
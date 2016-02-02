package symposium.model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VenueConstraintTest {

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

        Venue v = new Venue("North Building", 4, Collections.EMPTY_LIST);

        Venue v2 = new Venue ("South Building", 4, Arrays.asList(new TimeRange(1,3), new TimeRange(4,5)));

        ScheduleData.init(Arrays.asList(v, v2), 1);

        Panel p = new Panel(name, panelists, range, category, constraints);


        Constraint vC = new VenueConstraint(priority, p, v);

        assertTrue(vC.isConstraintViolated(v2.getFreeVenueTimes().get(0)));

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

        Venue v = new Venue("North Building", 4, Arrays.asList(new TimeRange(1,3), new TimeRange(4,5)));

        ScheduleData.init(Arrays.asList(v), 1);

        Panel p = new Panel(name, panelists, range, category, constraints);

        Constraint vC = new VenueConstraint(priority, p, v);

        assertFalse(vC.isConstraintViolated(v.getFreeVenueTimes().get(0)));

        ScheduleData.deleteScheduleData();
    }

}
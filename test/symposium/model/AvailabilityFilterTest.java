package symposium.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AvailabilityFilterTest {

    @Test
    public void testFilter() throws Exception {
        Collection<TimeRange> vt = new HashSet<TimeRange>();

        vt.add(new TimeRange(600, 700));
        vt.add(new TimeRange(1000,1100));
        vt.add(new TimeRange(1500, 2000));
        vt.add(new TimeRange(2600, 3000));
        Venue v = new Venue("Venue1", 4, 2,vt);

        List<Venue> venues = new ArrayList<Venue>();
        venues.add(v);
        Map<VenueTime, Integer> scoreMap = new HashMap<>();
        for(VenueTime i: v.getFreeVenueTimes()){
            scoreMap.put(i, 0);
        }

        Collection<VenueTime> result = new HashSet<>();
        result.add(new VenueTime(new TimeRange(600, 700), v));

        ScheduleData.init(venues, 1);

        String name = "Panel";
        List<String> panelists = new ArrayList<>(3);
        panelists.add("panelists1");
        panelists.add("panelists2");
        panelists.add("panelists3");
        Collection<TimeRange> avaliability = new HashSet<>(5);
        avaliability.add(new TimeRange(360, 1080));
        avaliability.add(new TimeRange(1800, 2520));
        avaliability.add(new TimeRange(3240, 3960));
        avaliability.add(new TimeRange(4680, 5400));
        avaliability.add(new TimeRange(6120, 6840));
        TimeRangeSeries range = new TimeRangeSeries(avaliability);
        List<String> constraints = new ArrayList<>(1);
        constraints.add("Availability:1");

        List<String> category = new ArrayList<>(1);
        category.add("IA");

        Panel p1 = new Panel(name, panelists, range, category, constraints);

        Filter x = new AvailabilityFilter(ConstraintPriority.REQUIRED, p1);

        x.filter(scoreMap, new HashMap<Constraint, Integer>());
        for(VenueTime i: scoreMap.keySet()){
            assertTrue(scoreMap.get(i) == 0);
            assertTrue(i.TIME.equals(new TimeRange(600, 700)));
        }
        ScheduleData.deleteScheduleData();
    }

    public void testIsConstraintViolated() throws Exception {

    }

}
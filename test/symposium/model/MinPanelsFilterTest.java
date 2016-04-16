package symposium.model;

import org.junit.After;
import org.junit.Test;
import java.util.*;

public class MinPanelsFilterTest {

    // Can only test constructor due to no other function having returns.
    @Test
    public void testConstructor() {
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

        Venue v = new Venue("North Building", 4, 1, Collections.EMPTY_LIST);

        ScheduleData.init(Arrays.asList(v), 1);

        Panel p = new Panel(name, panelists, range, category, constraints);

        Constraint vC = new MinPanelsFilter(priority, p);
        assert (vC.PRIORITY == ConstraintPriority.REQUIRED);
    }

    @After
    public void tearDown() {
        ScheduleData.deleteScheduleData();
    }
}

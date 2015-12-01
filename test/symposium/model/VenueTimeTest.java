package symposium.model;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by yousef-alsber on 11/30/15.
 */


public class VenueTimeTest {


    @Test
    public void testConstructor() throws Exception {

        List<TimeRange> ranges = new ArrayList<>(3);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));

        Venue venue = new Venue("NewVenue", 5, ranges);

        assertTrue(venue.NAME.equals("NewVenue"));
        assertTrue(venue.SIZE == 5);
        assertTrue(venue.getAssignedVenueTimes().isEmpty());

        List<VenueTime> venueTimes = venue.getFreeVenueTimes();
        for (int i = 0 ; i < ranges.size() ; i++) {
            assertTrue(venueTimes.get(i).VENUE == venue);
            assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
            assertTrue(venueTimes.get(i).getAssignedPanel() == null);
        }


    }


    @Test
    public void testAssignPanel() throws Exception {

        List<TimeRange> ranges = new ArrayList<>(3);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));

        Venue venue = new Venue("NewVenue", 5, ranges);

        String panelName = "NewPanel";
        String newCategory = "newCategory";
        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        Map<String, TimeRangeSeries> panalists = new HashMap<String, TimeRangeSeries>();

        Panel panel = new Panel(panelName, panalists, newCategory, constraints);

        VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);


        assertFalse(vt.getAssignedPanel().equals(panel));
        assertFalse(vt.isAssigned());
        vt.assignPanel(panel);
        assertTrue(vt.getAssignedPanel().equals(panel));
        assertTrue(vt.isAssigned());


    }

    @Test
    public void testIsAssigned() throws Exception {

        List<TimeRange> ranges = new ArrayList<>(3);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));

        Venue venue = new Venue("NewVenue", 5, ranges);

        String panelName = "NewPanel";
        String newCategory = "newCategory";
        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        Map<String, TimeRangeSeries> panelist = new HashMap<String, TimeRangeSeries>();

        Panel panel = new Panel(panelName, panelist, newCategory, constraints);

        VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);

        assertFalse(vt.isAssigned());
        vt.assignPanel(panel);
        assertTrue(vt.isAssigned());


    }

    @Test
    public void testGetAssignedPanel() throws Exception {

        List<TimeRange> ranges = new ArrayList<>(3);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));

        Venue venue = new Venue("NewVenue", 5, ranges);

        String panelName = "NewPanel";
        String newCategory = "newCategory";
        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        Map<String, TimeRangeSeries> panalists = new HashMap<String, TimeRangeSeries>();

        Panel panel = new Panel(panelName, panalists, newCategory, constraints);

        VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);

        assertFalse(vt.getAssignedPanel().equals(panel));
        vt.assignPanel(panel);
        assertTrue(vt.getAssignedPanel().equals(panel));

    }
}
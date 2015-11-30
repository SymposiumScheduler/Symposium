package symposium.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class VenueTest {

    @Test
    public void testConstructor() throws Exception {
        // test:   calling a constructor with appropriate values
        // return: constructor should set correct values to variables

        String name = "vnuName";
        int size = 3;
        List<TimeRange> ranges = new ArrayList<>(4);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));
        ranges.add(new TimeRange(70,80));

        Venue venue = new Venue(name, size, ranges);

        assertTrue(venue.NAME.equals(name));
        assertTrue(venue.SIZE == size);
        assertTrue(venue.getAssignedVenueTimes().isEmpty());

        List<VenueTime> venueTimes = venue.getFreeVenueTimes();
        for (int i = 0 ; i < ranges.size() ; i++){
            assertTrue(venueTimes.get(i).VENUE == venue);
            assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
        }
    }

    @Test
    public void testChangeToAssigned_Normal() throws Exception {
        // test:   changing one VenueTime to assigned from free
        // return: intended venue time should be removed from free list and added to assigned list

        List<TimeRange> ranges = new ArrayList<>(4);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));
        ranges.add(new TimeRange(70,80));

        Venue venue = new Venue("vnuName", 3, ranges);

        venue.changeToAssigned(venue.getFreeVenueTimes().get(0));

        // should be in assigned venueTimes
        VenueTime vt = venue.getAssignedVenueTimes().get(0);
        assertTrue(vt.VENUE == venue);
        assertTrue(vt.TIME == ranges.get(0));

        // should be removed from free venueTimes
        assertFalse(venue.getFreeVenueTimes().contains(vt));
    }
    @Test
    public void testChangeToAssigned_VenueTimeDifferentVenue() throws Exception {
        // test:   changing a venueTime with a different venue to assigned
        // return: the action should be ignored

        List<TimeRange> ranges_1 = new ArrayList<>(2);
        ranges_1.add(new TimeRange(10,20));
        ranges_1.add(new TimeRange(30,40));
        Venue venue_1 = new Venue("vnuName", 3, ranges_1);

        List<TimeRange> ranges_2 = new ArrayList<>(2);
        ranges_2.add(new TimeRange(50,60));
        ranges_2.add(new TimeRange(70,80));
        Venue venue_2 = new Venue("diffVenue", 2, ranges_2);

        VenueTime fromVenue_1 = venue_1.getFreeVenueTimes().get(0);

        // this command should not change anything
        venue_2.changeToAssigned(fromVenue_1);

        assertFalse(venue_2.getAssignedVenueTimes().contains(fromVenue_1));
    }

    @Test
    public void testChangeToFree_Normal() throws Exception {
        // test:   changing one VenueTime to free from assigned
        // return: intended venue time should be removed from assigned list and added to free list

        List<TimeRange> ranges = new ArrayList<>(4);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));

        Venue venue = new Venue("vnuName", 3, ranges);

        VenueTime testVT = venue.getFreeVenueTimes().get(0);

        venue.changeToAssigned(testVT);

        venue.changeToFree(testVT);

        assertFalse(venue.getAssignedVenueTimes().contains(testVT));
        assertTrue(venue.getFreeVenueTimes().contains(testVT));
    }
    @Test
    public void testChangeToFree_VenueTimeDifferentVenue() throws Exception {
        // test:   changing a venueTime with a different venue to free
        // return: the action should be ignored

        List<TimeRange> ranges_1 = new ArrayList<>(2);
        ranges_1.add(new TimeRange(10,20));
        ranges_1.add(new TimeRange(30,40));
        Venue venue_1 = new Venue("vnuName", 3, ranges_1);

        List<TimeRange> ranges_2 = new ArrayList<>(2);
        ranges_2.add(new TimeRange(50,60));
        ranges_2.add(new TimeRange(70,80));
        Venue venue_2 = new Venue("diffVenue", 2, ranges_2);

        VenueTime fromVenue_1 = venue_1.getFreeVenueTimes().get(0);

        // this command should not change anything
        venue_2.changeToFree(fromVenue_1);

        assertFalse(venue_2.getFreeVenueTimes().contains(fromVenue_1));
    }

    @Test
    public void testGetAssignedVenueTimes() throws Exception {
        // test:   getting the assigned venue time collection
        // return: produce the correct list of VenueTimes

        List<TimeRange> ranges = new ArrayList<>(4);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));
        ranges.add(new TimeRange(70,80));

        Venue venue = new Venue("vnuName", 3, ranges);

        // change first 2 venueTimes to assigned
        venue.changeToAssigned(venue.getFreeVenueTimes().get(0));
        venue.changeToAssigned(venue.getFreeVenueTimes().get(0));

        List<VenueTime> venueTimes = venue.getAssignedVenueTimes();

        // check if these two are returned
        for (int i = 0 ; i < 2 ; i++){
            assertTrue(venueTimes.get(i).VENUE == venue);
            assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
        }
    }

    @Test
    public void testGetFreeVenueTimes() throws Exception {
        // test:    getting the free venue time collection
        // return:  produce the correct list of VenueTimes

        List<TimeRange> ranges = new ArrayList<>(4);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));
        ranges.add(new TimeRange(70,80));

        Venue venue = new Venue("vnuName", 3, ranges);

        List<VenueTime> venueTimes = venue.getFreeVenueTimes();
        for (int i = 0 ; i < ranges.size() ; i++){
            assertTrue(venueTimes.get(i).VENUE == venue);
            assertTrue(venueTimes.get(i).TIME.equals(ranges.get(i)));
        }
    }
}
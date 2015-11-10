package symposium.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TimeFormatTest {

    @Test
    public void testNormalToAbsolute_oneRange() throws Exception {
        // test:    parse single time range in the normal range
        // return:  produce a correct TimeRange object
        String normalRange = "2, 7:31-14:32";
        Range result = TimeFormat.normalToAbsolute(normalRange);

        if(!(result instanceof TimeRange)) {
            fail("Produced TimeRangeSeries object, should be TimeRange");
        }

        assertTrue(new TimeRange(2*24*60 + 7*60 + 31,2*24*60 + 14*60 + 32).equals(result));
    }

    @Test
    public void testNormalToAbsolute_multipleRanges() throws Exception {
        // test:    parse a normal time with multiple lines
        // return:  produce a correct TimeRangeSeries object

        String normalRange = "2, 7:31-14:32\n"+"2, 15:00-17:41\n" +
                "3, 2:5-12:30\n"+"3, 13:00-14:20";
        Range result = TimeFormat.normalToAbsolute(normalRange);

        if(!(result instanceof TimeRangeSeries)) {
            fail("result is not TimeRangeSeries object , should be TimeRangeSeries");
        }

        List<TimeRange> timeRangeList = new ArrayList<>(2);

        timeRangeList.add(new TimeRange(2*24*60 + 7*60 + 31, 2*24*60 + 14*60 + 32));
        timeRangeList.add(new TimeRange(2*24*60 + 15*60 + 0, 2*24*60 + 17*60 + 41));
        timeRangeList.add(new TimeRange(3*24*60 + 2*60 + 5, 3*24*60 + 12*60 + 30));
        timeRangeList.add(new TimeRange(3*24*60 + 13*60 + 0, 3*24*60 + 14*60 + 20));


        TimeRangeSeries expectedResult = new TimeRangeSeries(timeRangeList);
        assertTrue(expectedResult.equals(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalToAbsolute_MissingComma() throws Exception {
        // test:    passing a normal time with missing comma which does not match the specification
        // return:  throws an IllegalArgumentException

        String normalRange = "2 7:31-14:32";
        // missing comma ----> ^

        TimeFormat.normalToAbsolute(normalRange);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalToAbsolute_MissingDay() throws Exception {
        // test:    passing a normal time with missing day number which does not match the specification
        // return:  throws an IllegalArgumentException

        String normalRange = "7:31-14:32";
        // missing day -----> ^

        TimeFormat.normalToAbsolute(normalRange);
    }

    @Test
    public void testGetDayRange() throws Exception {
        // test:    getting a correct timeRange from number of day
        // return:  produce the correct range [i*24*60, ((i+1)*24*60-1)] where i is the number of day

        TimeRange day3 = new TimeRange(3*24*60, 4*24*60-1);

        assertTrue(TimeFormat.getDayRange(3).equals(day3));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetDayRange_InvalidInput() throws Exception {
        // test:    passing negative day number
        // return:  throws an IllegalArgumentException

        TimeFormat.getDayRange(-2);
    }


    @Test
    public void testAbsoluteToNormal_TimeRange() throws Exception {
        // test:    converting an Absolute Range to normal range
        // return:  produces the correct number in a String formatted to the normalTime format.

        TimeRange timeRange = new TimeRange(2*60*24 + 3*60 + 20, 2*60*24 + 5*60 + 40);
        String normalRange = "2, 3:20-5:40";

        assertEquals(normalRange, TimeFormat.absoluteToNormal(timeRange));
    }
    @Test
    public void testAbsoluteToNormal_TimeRangeSeries() throws Exception {
        // test:    converting an Absolute TimeRangeSeries to normal range
        // return:  produces the correct number in a String formatted to the normalTime format.

        List<TimeRange> ranges = new ArrayList<>(3);
        ranges.add(new TimeRange(2*60*24 + 3*60 + 20,2*60*24 + 4*60 + 30));
        ranges.add(new TimeRange(2*60*24 + 5*60 + 10,2*60*24 + 6*60 + 30));
        ranges.add(new TimeRange(2*60*24 + 15*60 + 5,2*60*24 + 18*60 + 15));
        TimeRangeSeries trs = new TimeRangeSeries(ranges);


        String normalRange = "2, 3:20-4:30\n"+"2, 5:10-6:30\n"+"2, 15:5-18:15";

        assertEquals(normalRange, TimeFormat.absoluteToNormal(trs));
    }


    @Test
    public void testAbsoluteToNormal_TimeRangeIntersectsMultipleDays() throws Exception {
        // test:    converting a TimeRange that intersects multiple days
        // return:  produce a String with two lines with each day in a line and the range is split correctly

        TimeRange timeRange = new TimeRange(2*60*24 + 9*60 + 20, 4*60*24 + 5*60 + 40);
        String normalRange = "2, 9:20-23:59\n" + "3, 00:00-23:59\n" + "4, 00:00-5:40";

        assertEquals(normalRange, TimeFormat.absoluteToNormal(timeRange));
    }
}
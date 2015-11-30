package symposium.model;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeRangeTest {

    @Test
    public void testConstructor_Normal() throws Exception {
        // Test:    calling constructor with appropriate values
        // return:  constructor should set correct values to correct members
        int start = 10;
        int end = 200;

        TimeRange tr = new TimeRange(start, end);
        assertTrue(tr.START == start);
        assertTrue(tr.END == end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeInput() throws Exception {
        // Test:    passing negative values as start or end
        // return:  throws an IllegalArgumentException

        int start = -1;
        int end = 1;
        new TimeRange(start, end);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EndSmallerThanStart() throws Exception {
        // Test:    passing end smaller than start
        // return:  throws an IllegalArgumentException
        int start = 10;
        int end = 1;
        new TimeRange(start, end);
        fail();
    }

    @Test
    public void testGetStart() throws Exception {
        // Test:    getting the start through getStart()
        // return:  returns same start value, when initializing the object
        int xStart = 0;
        int xEnd = 10;
        Range x = new TimeRange(xStart, xEnd);
        assertEquals(xStart, x.getStart());
    }

    @Test
    public void testGetEnd() throws Exception {
        // Test:    getting the end through getEnd()
        // return:  returns same end value, when initializing the object
        int xStart = 0;
        int xEnd = 10;
        Range x = new TimeRange(xStart, xEnd);
        assertEquals(xEnd, x.getEnd());
    }

    @Test
    public void testUnion_TwoDisjointRanges() throws Exception {
        // test:    union two disjoint timeRanges
        // return:  produce TimeRangeSeries object with the correct TimeRanges

        TimeRange x = new TimeRange(0, 10);
        TimeRange y = new TimeRange(20, 30);
        Range xy = x.union(y);

        List<TimeRange> xyList = new ArrayList<>(2);
        xyList.add(x);
        xyList.add(y);
        Range z = new TimeRangeSeries(xyList);

        assertTrue(z + " does not equal " + xy, z.equals(xy));
    }

    @Test
    public void testUnion_TwoIntersectingRanges() throws Exception {
        // test:    union two intersecting ranges
        // return:  produce correct TimeRange

        TimeRange x = new TimeRange(0, 10);
        TimeRange y = new TimeRange(5, 15);
        Range xy = x.union(y);

        Range z = new TimeRange(0, 15);

        assertTrue(z + " does not equal " + xy, z.equals(xy));
    }

    @Test
    public void testUnion_HandelTimeRangeSeries1() throws Exception {
        // test:    union handel TimeRange.union(TimeRangeSeries)
        // return:  produce correct Range object

        TimeRange x = new TimeRange(0, 5);
        TimeRange y = new TimeRange(7, 12);
        TimeRange z = new TimeRange(14, 20);

        List<TimeRange> xyList = new ArrayList<>(2);
        xyList.add(x);
        xyList.add(y);
        Range xy = new TimeRangeSeries(xyList);

        // so z.union(xy) == TimeRange.union(TimeRangeSeries)
        Range xyz = z.union(xy);

        List<TimeRange> xyzList = new ArrayList<>(2);
        xyzList.add(x);
        xyzList.add(y);
        xyzList.add(z);
        Range expected = new TimeRangeSeries(xyzList);

        assertTrue(xyz + " does not equal " + expected, xyz.equals(expected));
    }

    @Test
    public void testUnion_HandelTimeRangeSeries2() throws Exception {
        // test:    union handel TimeRange.union(TimeRangeSeries) if TimeRange intersect TimeRangeSeries
        // return:  produce correct Range value

        TimeRange x = new TimeRange(0, 5);
        TimeRange y = new TimeRange(7, 12);
        TimeRange z = new TimeRange(14, 20);

        List<TimeRange> xyzList = new ArrayList<>(3);
        xyzList.add(x);
        xyzList.add(y);
        xyzList.add(z);
        Range xyz = new TimeRangeSeries(xyzList);

        TimeRange w = new TimeRange(1, 8);

        Range xyzw = w.union(xyz);

        TimeRange q = new TimeRange(0, 12);
        List<TimeRange> qzList = new ArrayList<>(4);
        qzList.add(q);
        qzList.add(z);
        Range expected = new TimeRangeSeries(qzList);

        assertTrue(xyzw + " does not equal " + expected, xyzw.equals(expected));
    }

    @Test
    public void testUnion_HandelTimeRangeSeries3() throws Exception {
        // test:    union handel TimeRange.union(TimeRangeSeries) if TimeRange span all of TimeRangeSeries
        // return:  produce correct value (not error)

        TimeRange x = new TimeRange(0, 5);
        TimeRange y = new TimeRange(7, 12);
        TimeRange z = new TimeRange(14, 20);

        List<TimeRange> xyzList = new ArrayList<>(3);
        xyzList.add(x);
        xyzList.add(y);
        xyzList.add(z);

        Range xyz = new TimeRangeSeries(xyzList);

        TimeRange w = new TimeRange(0, 30);

        Range xyzw = w.union(xyz);

        assertTrue(xyzw + " does not equal " + w, xyzw.equals(w));
    }

    @Test
    public void testUnion_Collection() throws Exception {
        // test:    getting the union  time range with list of other time ranges
        // return:  produces correct Range object

        TimeRange x = new TimeRange(1, 10);
        TimeRange y = new TimeRange(5, 20);
        TimeRange z = new TimeRange(7, 20);

        Range xUnionYZ = x.union(Arrays.asList(y,z));

        TimeRange correctResult = new TimeRange(1,20);

        assertTrue(xUnionYZ.equals(correctResult));
    }

    @Test
    public void testIntersect_Disjoints() throws Exception {
        // test:    intersecting two disjoint TimeRange objects
        // return:  returns null
        TimeRange x = new TimeRange(1, 10);
        TimeRange y = new TimeRange(20, 30);

        Range xIntersectY = x.intersect(y);

        assertTrue("x = " + x + ", y = " + y, xIntersectY == null);

    }


    @Test
    public void testIntersect_Intersecting() throws Exception {
        // test:    intersecting two intersecting TimeRange objects
        // return:  produces correct TimeRange object

        TimeRange x = new TimeRange(1, 10);
        TimeRange y = new TimeRange(5, 20);

        Range xIntersectY = x.intersect(y);

        TimeRange z = new TimeRange(5, 10);

        assertTrue("x intersect y = " + xIntersectY + ", z = " + z, xIntersectY.equals(z));
    }

    @Test
    public void testIntersect_Collection() throws Exception {
        // test:    intersecting  time range with list of other time ranges
        // return:  produces correct TimeRange object

        TimeRange x = new TimeRange(1, 10);

        TimeRange y = new TimeRange(5, 20);
        TimeRange z = new TimeRange(7, 20);

        Range xIntersectYZ = x.intersect(Arrays.asList(y,z));

        TimeRange correctResult = new TimeRange(7,10);

        assertTrue(xIntersectYZ.equals(correctResult));
    }

    @Test
    public void testDoesIntersect_Disjoints() throws Exception {
        // test:    Two disjoint ranges
        // return:  returns false
        Range x = new TimeRange(1, 10);
        Range y = new TimeRange(11, 20);
        assertFalse("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_CompleteIn() throws Exception {
        // test:    one range is completely inside the other
        // return:  returns true
        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(1, 5);
        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_InBetween() throws Exception {
        // test:    part of one range is inside the other
        // return:  true

        Range x = new TimeRange(5, 10);
        Range y = new TimeRange(1, 6);
        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_JustOnePoint() throws Exception {
        // test:    just the edge minute is overlapped
        // return:  returns true
        Range x = new TimeRange(5, 10);
        Range y = new TimeRange(10, 60);
        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_HandelTimeRangeSeries() throws Exception {
        // test:    is TimeRange.doesIntersect(TimeRangeSeries) handled properly
        // return:  returns correct value
        int xStart = 5;
        int xEnd = 10;
        Range x = new TimeRange(xStart, xEnd);

        TimeRange y1 = new TimeRange(1, 50);
        TimeRange y2 = new TimeRange(100, 150);
        List<TimeRange> ranges = new ArrayList<>(2);
        ranges.add(y1);
        ranges.add(y2);
        Range y = new TimeRangeSeries(ranges);

        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testCompareTo_ThisLargerThanOther() throws Exception {
        // test:    comparing a timeRange with greater start to other with smaller start
        // return:  returns positive int
        int xStart = 5;
        int xEnd = 30;
        Range x = new TimeRange(xStart, xEnd);

        int yStart = 2;
        int yEnd = 10;
        Range y = new TimeRange(yStart, yEnd);

        int result = x.compareTo(y);

        assertTrue("x's start = " + x.getStart() + ", y's start = " + y.getStart(), result > 0);
    }

    @Test
    public void testCompareTo_OtherLargerThanThis() throws Exception {
        // test:    comparing a timeRange with smaller start with other with greater start
        // return:  returns negative int

        int xStart = 5;
        int xEnd = 30;
        Range x = new TimeRange(xStart, xEnd);

        int yStart = 2;
        int yEnd = 10;
        Range y = new TimeRange(yStart, yEnd);

        int result = y.compareTo(x);
        assertTrue("x's start = " + x.getStart() + ", y's start = " + y.getStart(), result < 0);
    }

    @Test
    public void testCompareTo_SameStartAndEnd() throws Exception {
        // test:    comparing two timeRange with identical start
        // return:  returns 0
        int xStart = 2;
        int xEnd = 30;
        Range x = new TimeRange(xStart, xEnd);

        int yStart = 2;
        int yEnd = 30;
        Range y = new TimeRange(yStart, yEnd);

        int result = y.compareTo(x);
        assertEquals("x = " + x + ", y = " + y, 0, result);
    }

    @Test
    public void testCompareTo_HandelTimeRangeSeries() throws Exception {
        // test:    is TimeRange.compareTo(TimeRangeSeries) handled properly
        // return:  returns correct value

        int xStart = 0;
        int xEnd = 30;
        Range x = new TimeRange(xStart, xEnd);
        int y1Start = 0;
        int y1End = 10;
        int y2Start = 20;
        int y2End = 30;
        TimeRange y1 = new TimeRange(y1Start, y1End);
        TimeRange y2 = new TimeRange(y2Start, y2End);
        List<TimeRange> ranges = new ArrayList<>(2);
        ranges.add(y1);
        ranges.add(y2);
        Range y = new TimeRangeSeries(ranges);
        int result = x.compareTo(y);
        assertTrue("x = " + x.getStart() + ", y = " + y.getStart(), result == 0);
    }

    @Test
    public void testIsInside_PointOutside() throws Exception {
        // test:    check a point that is outside the range
        // return:  returns false

        int xStart = 1;
        int xEnd = 10;
        int point = 100;

        Range x = new TimeRange(xStart, xEnd);

        assertFalse("point = " + point + ", x range: [" + x.getStart() + " , " + x.getEnd() + "]",
                x.isInside(point));
    }

    @Test
    public void testIsInside_PointInside() throws Exception {
        // test:    check a point that is inside the range
        // return:  returns true

        int xStart = 1;
        int xEnd = 10;
        int point = 5;

        Range x = new TimeRange(xStart, xEnd);

        assertTrue("point = " + point + ", x range: [" + x.getStart() + " , " + x.getEnd() + "]",
                x.isInside(point));
    }

    @Test
    public void testIsInside_NegativePoint() throws Exception {
        // test:    checking a negative point
        // return:  returns false, since the range is time, and time can't be negative.

        int xStart = 1;
        int xEnd = 10;
        int point = -5;
        Range x = new TimeRange(xStart, xEnd);
        assertFalse("point = " + point + ", x range: [" + x.getStart() + " , " + x.getEnd() + "]",
                x.isInside(point));
    }

    @Test
    public void testIsInside_IsStartIn() throws Exception {
        // test:    check if the start is in the range
        // return:  returns true
        int xStart = 1;
        int xEnd = 10;
        Range x = new TimeRange(xStart, xEnd);
        assertTrue("point = " + xStart + ", x range: [" + x.getStart() + " , " + x.getEnd() + "]", x.isInside(xStart));
    }

    @Test
    public void testIsInside_IsEndIn() throws Exception {
        // test:    check if the end is in the range
        // return:  returns true
        int xStart = 1;
        int xEnd = 10;
        Range x = new TimeRange(xStart, xEnd);
        assertTrue("point = " + xEnd + ", x = " + x, x.isInside(xEnd));
    }

    @Test
    public void testIterator_HasNext() throws Exception {
        // test:    checking hasNext before iterating through any objects
        // return:  returns true, since a range has at least one TimeRange component

        Range x = new TimeRange(0, 10);
        Iterator<TimeRange> y = x.iterator();
        assertTrue("x = " + x, y.hasNext());
    }

    @Test
    public void testIterator_Next() throws Exception {
        // test:    getting Iterator.next() from TimeRange object
        // return:  returns the same TimeRange object
        Range x = new TimeRange(0, 10);
        Iterator<TimeRange> y = x.iterator();
        assertTrue("x = " + x, x.equals(y.next()));
    }

    @Test
    public void testIterator_HasNextAfterNext() throws Exception {
        // test:    checking hasNext() after iterating once
        // return:  returns false, since TimeRange has only one TimeRange in the iterator.
        Range x = new TimeRange(0, 10);
        Iterator<TimeRange> y = x.iterator();
        y.next();
        assertFalse("x = " + x, y.hasNext());
    }

    @Test
    public void testEquals_SameObject() throws Exception {
        // test:    checking if a TimeRange equals itself.
        // return:  returns true
        Range x = new TimeRange(0, 10);
        assertTrue("x = " + x, x.equals(x));
    }

    @Test
    public void testEquals_SameRange() throws Exception {
        // test:    checking if a timeRange equals another timeRange which have the same start and end
        // return:  returns true
        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(0, 10);
        assertTrue("x = " + x + ", y = " + y, x.equals(y));
    }

    @Test
    public void testEquals_DiffrentRange() throws Exception {
        // test:    checking if a timeRange equals a different intersecting range
        // return:  return false

        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(5, 10);
        assertFalse("x = " + x + ", y = " + y, x.equals(y));
    }

    @Test
    public void testHashCode_SameRange() throws Exception {
        // test:    getting the hashcode of two equal TimeRanges
        // return:  returns same hashcode

        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(0, 10);
        assertEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testHashCode_DifferentDisjointRange() throws Exception {
        // test:    getting hashcode of two different ranges
        // return:  returns different hashcode
        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(20, 30);
        assertNotEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testHashCode_DifferentButSameStart() throws Exception {
        // test:    getting hashcode for two different ranges with the same start.
        // return:  returns different hashcodes

        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(0, 30);
        assertNotEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testHashCode_DifferentButSameEnd() throws Exception {
        // test:    getting hashcode for two different ranges with the same end.
        // return:  returns different hashcodes

        Range x = new TimeRange(0, 10);
        Range y = new TimeRange(5, 10);
        assertNotEquals(x.hashCode(), y.hashCode());
    }

}
package symposium.model;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeRangeSeriesTest {
    @Test()
    public void testConstructor_Normal() throws Exception {
        // test:    passing appropriate ranges
        // return:  constructor sets correct values to correct variables

        TimeRange x1 = new TimeRange(0, 10);
        TimeRange x2 = new TimeRange(20, 30);

        List<TimeRange> xList = new ArrayList<>();
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        Iterator<TimeRange> itr = x.iterator();

        assertTrue(itr.next().equals(x1));
        assertTrue(itr.next().equals(x2));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_Null() throws Exception {
        // test:    passing a null as ranges to the constructor
        // return:  throws a NullPointerException

        new TimeRangeSeries(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_Size1() throws Exception {
        // test:    passing one timeRange to constructor
        // return:  throws an IllegalArgumentException

        List<TimeRange> xList = new ArrayList<>(1);
        xList.add(new TimeRange(0, 10));

        new TimeRangeSeries(xList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_IsNotSimple() throws Exception {
        // test:    passing a non-simple ranges (intersecting TimeRanges)
        // return:  throws an IllegalArgumentException

        List<TimeRange> notSimpleRanges = new ArrayList<>(2);
        notSimpleRanges.add(new TimeRange(0, 10));
        notSimpleRanges.add(new TimeRange(5, 15));

        new TimeRangeSeries(notSimpleRanges);
    }

    @Test
    public void testGetStart() throws Exception {
        // Test:    getting the first point in timeRangeSeries
        // return:  returns the start of the first timeRange

        final int firstPoint = 7;

        List<TimeRange> xList = new ArrayList<>();
        xList.add(new TimeRange(firstPoint, 10));
        xList.add(new TimeRange(20, 30));

        Range x = new TimeRangeSeries(xList);
        assertEquals(firstPoint, x.getStart());
    }

    @Test
    public void testGetEnd() throws Exception {
        // Test:    getting the end of timeRangeSeries
        // return:  returns the end of the last timeRange

        final int lastPoint = 30;

        List<TimeRange> xList = new ArrayList<>();
        xList.add(new TimeRange(7, 10));
        xList.add(new TimeRange(20, lastPoint));

        Range x = new TimeRangeSeries(xList);
        assertEquals(lastPoint, x.getEnd());
    }

    @Test
    public void testCompareTo_ThisStartLargerThanOtherStart() throws Exception {
        // test:    comparing range with greater start than other with smaller one
        // return:  returns positive integer

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(10, 15);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(22, 30);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        int result = x.compareTo(y);
        assertTrue("x.compareTo(y) = " + result, result > 0);
    }

    @Test
    public void testCompareTo_ThisEndLargerThanOtherEnd() throws Exception {
        // test:    comparing range with same start but greater end to other with smaller end.
        // return:  returns positive integer

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 40);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(22, 30);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        int result = x.compareTo(y);
        assertTrue("x.compareTo(y) = " + result, result > 0);
    }

    @Test
    public void testCompareTo_OtherStartLargerThanThisStart() throws Exception {
        // test:    comparing range with smaller start to other with greater start
        // return:  positive integer

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(10, 15);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(22, 30);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        int result = y.compareTo(x);
        assertTrue("y.compareTo(x) = " + result, result < 0);
    }

    @Test
    public void testCompareTo_OtherEndLargerThanThisEnd() throws Exception {
        // test:    comparing a range with same start but smaller end to other
        // return:  positive integer

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 40);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(22, 30);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        int result = y.compareTo(x);
        assertTrue("y.compareTo(x) = " + result, result < 0);
    }

    @Test
    public void testCompareTo_SameStartAndEnd() throws Exception {
        // test:    comparing two timeRangeSeries with the same start and end
        // return:  returns 0

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(22, 30);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        int result = x.compareTo(y);
        assertTrue("x.compareTo(y) = " + result, result == 0);
    }

    @Test
    public void testCompareTo_HandelTimeRange() throws Exception {
        // test:    is TimeRange.compareTo(TimeRangeSeries) handled properly
        // return:  returns correct value

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        Range y = new TimeRange(2, 30);

        int result = x.compareTo(y);
        assertTrue("x.compareTo(y) = " + result, result < 0);
    }

    @Test
    public void testDoesIntersect_Disjoints() throws Exception {
        // test:    checking two disjoint TimeRangeSeries ranges
        // return:  returns false

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(17, 18);
        TimeRange y2 = new TimeRange(32, 40);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        assertFalse("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_CompleteIn() throws Exception {
        // test:    checking two TimeRangeSeries one completely in the other.
        // return:  returns true

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        TimeRange x3 = new TimeRange(35, 40);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(5, 10);
        TimeRange y2 = new TimeRange(25, 29);
        TimeRange y3 = new TimeRange(36, 39);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_InBetween() throws Exception {
        // test:    checking two TimeRangeSeries one in between the other.
        // return:  returns true

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 30);
        TimeRange x2 = new TimeRange(40, 60);
        TimeRange x3 = new TimeRange(70, 90);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(10, 20);
        TimeRange y2 = new TimeRange(25, 50);
        TimeRange y3 = new TimeRange(80, 100);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testDoesIntersect_HandelTimeRange() throws Exception {
        // test:    TimeRangeSeries.doesIntersect(TimeRange) handled properly.
        // return:  returns true

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 30);
        TimeRange x2 = new TimeRange(40, 60);
        TimeRange x3 = new TimeRange(70, 90);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y = new TimeRange(10, 20);

        assertTrue("x = " + x + ", y = " + y, x.doesIntersect(y));
    }

    @Test
    public void testIntersect_Disjoints() throws Exception {
        // test:    intersecting two disjoint TimeRangeSeries objects
        // return:  returns null

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(17, 18);
        TimeRange y2 = new TimeRange(32, 40);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        Range xIntersectY = x.intersect(y);

        assertEquals(null, xIntersectY);
    }

    @Test
    public void testIntersect_CompleteIn() throws Exception {
        // test:    intersecting two TimeRangeSeries one is completely in the other.
        // return:  correct intersection, which is the bigger range.

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        TimeRange x3 = new TimeRange(35, 40);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(5, 10);
        TimeRange y2 = new TimeRange(25, 29);
        TimeRange y3 = new TimeRange(36, 39);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        Range xIntersectY = x.intersect(y);
        assertTrue("expected = " + y + ", xIntersectY = " + xIntersectY, y.equals(xIntersectY));
    }

    @Test
    public void testIntersect_InBetween() throws Exception {
        // test:    intersecting wo TimeRangeSeries one is in between the other.
        // return:  produces correct intersection.

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(5, 10);
        TimeRange y2 = new TimeRange(22, 29);
        TimeRange y3 = new TimeRange(50, 70);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        Range xIntersectY = x.intersect(y);

        // expected is the TimeRangeSeries expected as a return
        // ex1-2 are the result from intersecting x and y
        TimeRange ex1 = new TimeRange(5, 10);
        TimeRange ex2 = new TimeRange(50, 60);
        List<TimeRange> exList = new ArrayList<>(2);
        exList.add(ex1);
        exList.add(ex2);
        Range expected = new TimeRangeSeries(exList);
        assertTrue("expected = " + expected + ", xIntersectY = " + xIntersectY, expected.equals(xIntersectY));
    }

    @Test
    public void testIntersect_HandelTimeRange() throws Exception {
        // test:    is TimeRangeSeries.intersect(TimeRange) handled properly
        // return:  returns correct value

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);


        TimeRange y = new TimeRange(5, 10);

        Range xIntersectY = x.intersect(y);

        assertTrue("expected = " + y + ", xIntersectY = " + xIntersectY, y.equals(xIntersectY));
    }

    @Test
    public void testIntersect_Collection() throws Exception {
        // test:    intersecting TimeRangeSeries with list of other time ranges
        // return:  produces correct Range object

        TimeRange x = new TimeRange(57, 93);
        TimeRange y = new TimeRange(50, 73);
        TimeRange z = new TimeRange(55, 75);

        Range timeSeries = new TimeRangeSeries(Arrays.asList(
                new TimeRange(70, 80),
                new TimeRange(30, 40),
                new TimeRange(50, 60)));

        Range intersection = timeSeries.intersect(Arrays.asList(x,y,z));
        Range correctResult = new TimeRangeSeries(Arrays.asList(
                new TimeRange(57,60),
                new TimeRange(70, 73)));

        assertTrue(intersection.equals(correctResult));
    }

    @Test
    public void testIterator_HasNext() throws Exception {
        // test:    checking Iterator.hasNext()
        // return:  returns true

        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(new TimeRange(0, 10));
        xList.add(new TimeRange(20, 30));

        Range x = new TimeRangeSeries(xList);

        Iterator<TimeRange> itr = x.iterator();


        assertTrue("1st Time",itr.hasNext());
        itr.next();
        assertTrue("2nd Time",itr.hasNext());
        itr.next();
        // iterator should finish now.
        assertFalse("3rd Time",itr.hasNext());
    }

    @Test
    public void testIterator_Next() throws Exception {
        // test:    getting iterator.next()
        // return:  returns correct values

        TimeRange x1 = new TimeRange(0, 10);
        TimeRange x2 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        Iterator<TimeRange> y = x.iterator();

        Range next_1 = y.next();
        assertTrue("expected = " + x1 + ", recevied = " + next_1, x1.equals(next_1));

        Range next_2 = y.next();
        assertTrue("expected = " + x2 + ", recevied = " + next_2, x2.equals(next_2));
    }


    @Test
    public void testUnion_Disjoints() throws Exception {
        // test:   getting the union of two disjoint TimeRangeSeries objects
        // return: produce correct union

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 150);
        TimeRange x2 = new TimeRange(200, 300);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(170, 180);
        TimeRange y2 = new TimeRange(320, 400);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        Range xUnionY = x.union(y);

        List<TimeRange> unionList = new ArrayList<>(4);
        unionList.add(x1);
        unionList.add(x2);
        unionList.add(y1);
        unionList.add(y2);
        Range union = new TimeRangeSeries(unionList);

        assertTrue("expected = " + union + ", xUnionY = " + xUnionY, union.equals(xUnionY));
    }


    @Test
    public void testUnion_CompleteIn() throws Exception {
        // test:    getting the union of two TimeRangeSeries one is completely in the other.
        // return:  produces correct union, which is the larger range.

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 15);
        TimeRange x2 = new TimeRange(20, 30);
        TimeRange x3 = new TimeRange(35, 40);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(5, 10);
        TimeRange y2 = new TimeRange(25, 29);
        TimeRange y3 = new TimeRange(36, 39);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        Range xUnionY = x.union(y);
        assertTrue("expected = " + x + ", x union y = " + xUnionY, x.equals(xUnionY));
    }

    @Test
    public void testUnion_InBetween() throws Exception {
        // test:    getting the union of two TimeRangeSeries one is in between the other.
        // return:  produces correct union.

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(5, 10);
        TimeRange y2 = new TimeRange(22, 29);
        TimeRange y3 = new TimeRange(50, 70);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        Range xUnionY = x.union(y);

        // expected is the TimeRangeSeries expected as a return
        // ex1 is the result from union x2 and y2
        TimeRange ex1 = new TimeRange(22, 40);
        List<TimeRange> exList = new ArrayList<>(3);
        exList.add(x1);
        exList.add(ex1);
        exList.add(y3);
        Range expected = new TimeRangeSeries(exList);
        assertTrue("expected = " + expected + ", xIntersectY = " + xUnionY, expected.equals(xUnionY));
    }

    @Test
    public void testUnion_HandelTimeRange() throws Exception {
        // test:    is TimeRangeSeries.union(TimeRange) handled properly
        // return:  produces correct value

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);


        TimeRange y = new TimeRange(15, 35);

        // expected is the TimeRangeSeries expected as a return
        // ex1 is the result from union x1-2 and y
        TimeRange ex1 = new TimeRange(0, 40);
        List<TimeRange> exList = new ArrayList<>(2);
        exList.add(ex1);
        exList.add(x3);
        Range expected = new TimeRangeSeries(exList);

        Range xUnionY = x.union(y);

        assertTrue("expected = " + expected + ", xUnionY = " + xUnionY, expected.equals(xUnionY));
    }

    @Test
    public void testUnion_Collection() throws Exception {
        // test:    getting the union of range with list of other time ranges
        // return:  produces correct Range object

        TimeRange x = new TimeRange(1, 10);
        TimeRange y = new TimeRange(5, 20);
        TimeRange z = new TimeRange(7, 20);

        TimeRange s1 = new TimeRange(30, 40);
        TimeRange s2 = new TimeRange(50, 60);
        TimeRange s3 = new TimeRange(70, 80);
        Range timeSeries = new TimeRangeSeries(Arrays.asList(s1,s2,s3));

        Range result = timeSeries.union(Arrays.asList(s1,s2,s3,x,y,z));
        Range correctResult = new TimeRangeSeries((Arrays.asList(s1,s2,s3, new TimeRange(1,20))));

        assertTrue(result.equals(correctResult));
    }

    @Test
    public void testIsInside_PointCompletelyOutside() throws Exception {
        // test:    checking a point outside the range
        // return:  returns false

        int point = 100;

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertFalse("point = " + point + ", x = " + x, x.isInside(point));
    }

    @Test
    public void testIsInside_PointInBetween() throws Exception {
        // test:    checking a point in a hole inside the timeRangeSeries
        // return:  returns false

        int point = 25;

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertFalse("point = " + point + ", x = " + x, x.isInside(point));
    }

    @Test
    public void testIsInside_PointInside() throws Exception {
        // test:    checking a point that is inside one of the timeRanges
        // return:  retursn true

        int point = 35;

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertTrue("point = " + point + ", x = " + x, x.isInside(point));
    }

    @Test
    public void testIsInside_NegativePoint() throws Exception {
        // test:    checking a negative point
        // return:  returns false, since the range is time, and time can't be negative.

        int point = -100;

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertFalse("point = " + point + ", x = " + x, x.isInside(point));
    }

    @Test
    public void testIsInside_IsStartIn() throws Exception {
        // test:    checking if the start in the range
        // return:  returns true

        int point = 0;

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertTrue("point = " + point + ", x = " + x, x.isInside(point));    }

    @Test
    public void testIsInside_IsEndIn() throws Exception {
        // test:    checking if the end in the range
        // return:  returns true

        int point = 60;

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertTrue("point = " + point + ", x = " + x, x.isInside(point));
    }


    @Test
    public void testSimplify() throws Exception {
        // test:    simplifying a non-simple list
        // return:  produces correct list of timeRanges

        // x is a set of the simplified x1-3 by TimeRangeSeries.simplify()
        TimeRange x1 = new TimeRange(0, 10);
        TimeRange x2 = new TimeRange(11, 20);
        TimeRange x3 = new TimeRange(15, 30);
        List<TimeRange> notSimpleList = new ArrayList<>(3);
        notSimpleList.add(x1);
        notSimpleList.add(x2);
        notSimpleList.add(x3);

        Collection<TimeRange> simplifiedList = TimeRangeSeries.simplify(notSimpleList);

        // simplified is a set of the simplified x1-3
        TimeRange simple = new TimeRange(0, 30);


        assertEquals(1, simplifiedList.size());
        assertTrue(simplifiedList.contains(simple));
    }

    @Test
    public void testIsSimple_SimpleList() throws Exception {
        // test:    checking a non simple list
        // return:  returns false

        // x is the TimeRangeSeries for x1-4
        // x1-4 start and end are chosen so that x1 and x2 will have some overlap
        // x3 will be in between x1 and x2 overlaping them both, and x4 is disjoint.
        TimeRange x1 = new TimeRange(5, 150);
        TimeRange x2 = new TimeRange(160, 200);
        TimeRange x3 = new TimeRange(210,220);
        TimeRange x4 = new TimeRange(300, 307);
        List<TimeRange> xList = new ArrayList<>(4);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        xList.add(x4);
        assertTrue(TimeRangeSeries.isSimple(xList));
    }

    @Test
    public void testIsSimple_Overlap() throws Exception {
        // test:    checking a non simple list because of overlapping timeRanges
        // return:  returns false

        TimeRange x1 = new TimeRange(9, 15);
        TimeRange x2 = new TimeRange(0, 10);
        TimeRange x3 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(4);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        assertFalse(TimeRangeSeries.isSimple(xList));
    }
    @Test
    public void testIsSimple_RangeSplit() throws Exception {
        // test:    checking a non simple list because of splitting a simple range like [1,5][6,10] which equals [1,10]
        // return:  returns false

        TimeRange x1 = new TimeRange(9, 15);
        TimeRange x2 = new TimeRange(16, 17);
        TimeRange x3 = new TimeRange(20, 30);
        List<TimeRange> xList = new ArrayList<>(4);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        assertFalse(TimeRangeSeries.isSimple(xList));
    }

    @Test
    public void testEquals_SameObject() throws Exception {
        // test:    checking an object with itself
        // return:  returns true

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        assertTrue("x = " + x, x.equals(x));
    }


    @Test
    public void testEquals_SameRange() throws Exception {
        // test:    checking two equal ranges but different objects
        // return:  true

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(30, 40);
        TimeRange y3 = new TimeRange(50, 60);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        assertTrue("x = " + x + ", y = " + y, x.equals(y));
    }

    @Test
    public void testEquals_DifferentRange() throws Exception {
        // test:    checking two different timeRangeSeries
        // return:  returns false

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(10, 20);
        TimeRange y2 = new TimeRange(30, 40);
        TimeRange y3 = new TimeRange(50, 60);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        assertFalse("x = " + x + ", y = " + y, x.equals(y));
    }



    @Test
    public void testHashCode_SameRange() throws Exception {
        // test:    getting the hashcodes of two equal ranges
        // return:  produces the same hashcode

        // x is the TimeRangeSeries for x1-3
        TimeRange x1 = new TimeRange(0, 20);
        TimeRange x2 = new TimeRange(30, 40);
        TimeRange x3 = new TimeRange(50, 60);
        List<TimeRange> xList = new ArrayList<>(3);
        xList.add(x1);
        xList.add(x2);
        xList.add(x3);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-3
        TimeRange y1 = new TimeRange(0, 20);
        TimeRange y2 = new TimeRange(30, 40);
        TimeRange y3 = new TimeRange(50, 60);
        List<TimeRange> yList = new ArrayList<>(3);
        yList.add(y1);
        yList.add(y2);
        yList.add(y3);
        Range y = new TimeRangeSeries(yList);

        assertEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testHashCode_DifferentDisjointRange() throws Exception {
        // test:    checking the hashcodes of different ranges
        // return:  produces two different hashcodes

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 10);
        TimeRange x2 = new TimeRange(30, 40);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(15, 25);
        TimeRange y2 = new TimeRange(50, 60);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        assertNotEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testHashCode_DiffrentButSameStart() throws Exception {
        // test:    checking two hashcodes with the same start but different ends
        // return:  returns different hashcodes

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 10);
        TimeRange x2 = new TimeRange(30, 40);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(0, 9);
        TimeRange y2 = new TimeRange(50, 60);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        assertNotEquals(x.hashCode(), y.hashCode());
    }

    @Test
    public void testHashCode_DifferentButSameEnd() throws Exception {
        // test:    checking two hashcodes with the same end but different starts
        // return:  returns different hashcodes

        // x is the TimeRangeSeries for x1-2
        TimeRange x1 = new TimeRange(0, 10);
        TimeRange x2 = new TimeRange(30, 40);
        List<TimeRange> xList = new ArrayList<>(2);
        xList.add(x1);
        xList.add(x2);
        Range x = new TimeRangeSeries(xList);

        // y is the TimeRangeSeries for y1-2
        TimeRange y1 = new TimeRange(15, 25);
        TimeRange y2 = new TimeRange(29, 40);
        List<TimeRange> yList = new ArrayList<>(2);
        yList.add(y1);
        yList.add(y2);
        Range y = new TimeRangeSeries(yList);

        assertNotEquals(x.hashCode(), y.hashCode());
    }
}
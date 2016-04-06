package symposium.model;

import java.util.*;

/**
 * An interface for absolute ranges.
 * Range is inclusive, i.e [1,2] includes 1 and 2.
 *  Therefore, we can have range with same start and end. it will be of length 1.
 */
public interface Range extends Comparable<Range>, Iterable<TimeRange> {

    /**
     * Gets earliest point in the range
     *
     * @return int; the earliest point in the range.
     */
    int getStart();

    /**
     * Gets the latest point in the range
     * It's not guaranteed that every point between getStart() and getEnd() are inside the range because
     * it can be TimeRangeSeries.
     *
     * @return int; the latest point in the range
     */
    int getEnd();

    /**
     * calculates the intersections between this range and other range/s.
     *
     * @param other other range/s to intersect
     * @return Range; New range with result, null if there is no intersection.
     */
    Range intersect(Range other);
    Range intersect(Collection<Range> other);

    /**
     * calculates the union between this range and other range/s.
     *
     * @param other other range/s to union
     * @return RAnge; New range with the result.
     *      The union of two TimeRange objects can be a TimeRangeSeries object. example [1,2]U[3,4]
     */
    Range union(Range other);
    Range union(Collection<Range> other);

    /**
     * check if two ranges intersect
     *
     * @param other other range to intersect
     * @return boolean; true if they intersect in at least one point, false otherwise
     */
    boolean doesIntersect(Range other);

    /**
     * check if a point is in a range
     *
     * @param point point in absolute time to check
     * @return boolean; true if the point is in the range, otherwise false
     */
    boolean isInside(int point);

    /**
     * create iterator to iterate through TimeRange objects
     * Used to iterate a range, regardless if it's TimeRange or TimeRangeSeries.
     * if it's TimeRange the iterator will only have one thing.
     * if it's TimeRangeSeries, the iterator should iterate through TimeRange objects making up the TimeRangeSeries Object
     *
     * @return Iterator over TimeRange objects
     */
    Iterator<TimeRange> iterator();

    /**
     * compare two ranges. ranges are compared by their start and end time.
     * A &gt; B if (A.start &gt; B.start) or (A.start == B.start and A.end &gt; B.end)
     * @param other
     * @return int; positive number if this &gt; other, 0 if this's start and end == other's start and end,
     * otherwise negative number.
     */
    int compareTo(Range other);

    /**
     * check if two ranges are equal
     *
     * @param other range to check
     * @return boolean; true if all starts and ends are identical, otherwise false.
     */
    boolean equals(Range other);

    /**
     * get the length of the included points in the length
     *
     * @return int; the length of the range. Min is 1
     */
    int length();

    /**
     * check if a range encloses another one
     *
     * @param range
     * @return boolean; true if it encloses it, otherwise false.
     */
    boolean doesEnclose(Range range);
}





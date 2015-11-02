package symposium.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * RangeLine are sorted by their earliest start time.
 */
public class RangeLine implements Comparable<RangeLine> {
    private final List<int[]> ranges;

    public RangeLine(List<int[]> ranges){
        this.ranges = ranges;
    }
    public RangeLine(int start, int end){
        this(new ArrayList<int[]>(new int[]{start, end}));
    }
    public RangeLine() {
        this(new ArrayList<int[]>());
    }

    public int compareTo(RangeLine rangeLine) {}

    public boolean doesIntersect(RangeLine rangeLine) {}
    public RangeLine intersect(RangeLine rangeLine) {}
    public RangeLine union(RangeLine rangeLine) {}
}

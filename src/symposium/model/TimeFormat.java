package symposium.model;

/**
 * # AbsoluteTime
 * an int that donates minute count from midnight of day 0.
 * int is more than enough to donate minutes, it gives us about 4000 years.
 *
 * # NormalTime
 * a string of the format
 * "<number of day>, <hr>:<mnt>-<hr>:<mnt>" or multiple lines of the same format.
 * hr is in 24 hour format and days starts with day 0
 * normal time does not have to be simple (see TimeRangeSeries.isSimple()), it will be simplified if necessary.
 *
 * Regex for valid normalTime "^(?: *\d+ *, *\d+:\d+-\d+:\d+ *)(?:\n+ *\d+ *, *\d+:\d+-\d+:\d+ *)*\s*$"
 */

import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class for operations about TimeFormat
 */
public final class TimeFormat {
    private TimeFormat() {} // TimeFormat should be static, no instances should be created

    /**
     * create a range including exactly every point in a day
     */
    public static TimeRange getDayRange(int dayNumber) {
        if (dayNumber < 0) {
            throw new IllegalArgumentException("Negative time does not exist");
        }
        return new TimeRange(dayNumber *  24 * 60, (dayNumber + 1) *  24 * 60 - 1);
    }

    /**
     * convert absoluteRange to normalRange
     */
    public static String absoluteToNormal(Range absoluteRange) {
        StringBuilder normalTime = new StringBuilder();
        Iterator<TimeRange> rangeItr = absoluteRange.iterator();

        while (rangeItr.hasNext()) {
            TimeRange range = rangeItr.next();

            int startDay = getNumberOfDay(range.getStart());
            int endDay = getNumberOfDay(range.getEnd());

            for(int d = startDay ; d <= endDay ; d++) {
                normalTime.append("\n").append(d).append(", ");

                if(d == startDay) {
                    normalTime.append(absoluteTimeToClockString(range.getStart()));
                } else {
                    normalTime.append("00:00");
                }

                normalTime.append('-');

                if(d == endDay) {
                    normalTime.append(absoluteTimeToClockString(range.getEnd()));
                } else {
                    normalTime.append("23:59");
                }
            }
        }
        normalTime.delete(0, 1); // because first character is "\n" because of loop.
        return normalTime.toString();
    }


    private static Pattern VALID_RANGE_PATTERN =
            Pattern.compile("^(?: *\\d+ *, *\\d+:\\d+-\\d+:\\d+ *)(?:\\n+ *\\d+ *, *\\d+:\\d+-\\d+:\\d+ *)*\\s*$");

    /**
     * convert normalRange to absolute
     */
    public static Range normalToAbsolute(String normalRange) {

        if (!VALID_RANGE_PATTERN.matcher(normalRange).matches()) {
            throw new IllegalArgumentException("Passed normal range does not match specification for normal range");
        }

        // clean normal range : remove all spaces and empty lines.
        normalRange = normalRange.replaceAll(" +","").replaceAll("\\n+","\n");

        SortedSet<TimeRange> resultRanges = new TreeSet<>();

        String[] lines = normalRange.split("\\n");
        for(String line : lines) {
            String[] parts = line.split("[,:-]");
            int day = Integer.parseInt(parts[0]);
            int start = timeComponentsToAbsolute(day, Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
            int end = timeComponentsToAbsolute(day, Integer.parseInt(parts[3]),Integer.parseInt(parts[4]));
            resultRanges.add(new TimeRange(start,end));
        }

        if (resultRanges.size() == 1) {
            return resultRanges.first();
        } else {
            if(! TimeRangeSeries.isSimple(resultRanges)) {
                resultRanges = TimeRangeSeries.simplify(resultRanges);
            }
            return new TimeRangeSeries(resultRanges);
        }
    }

    public static int timeComponentsToAbsolute(int day, int hours, int minutes) {
        return day * (24 * 60) + hours * 60 + minutes;
    }

    public static int getNumberOfDay(int timePoint) {
        return timePoint / (24 * 60);
    }

    private static String absoluteTimeToClockString(int absoluteTime) {
        absoluteTime = absoluteTime % (24 * 60);

        return absoluteTime / 60 + ":" + absoluteTime % 60;
    }
}

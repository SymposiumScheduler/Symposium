package symposium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import symposium.model.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import symposium.model.Panel;

public class Parser {
    public static void main(String[] args) {
        List<symposium.model.Panel> panels = new ArrayList<Panel>();
        List<Venue> venues = new ArrayList<Venue>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("data.txt"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray json_venues = (JSONArray) jsonObject.get("Venues");
            JSONArray json_venue_times = (JSONArray) jsonObject.get("Venue-Times");
            JSONArray json_panelists = (JSONArray) jsonObject.get("Panelists");
            JSONArray json_panels = (JSONArray) jsonObject.get("Panels");


            //System.out.println("Getting venue sizes...");
            Map<String, Integer> sizes = new HashMap<String, Integer>();
            for (Object o : json_venues) {
                JSONObject item = (JSONObject) o;
                String venue_name = (String) item.get("name");
                String venue_size = item.get("size").toString();
                sizes.put(venue_name, Integer.valueOf(venue_size));
            }


            //System.out.println("Setting up Venues...");
            Map<String, List<TimeRange>> ranges = new HashMap<String, List<TimeRange>>();
            for (Object o : json_venue_times) {
                JSONObject item = (JSONObject) o;
                String venue_name = (String) item.get("name");
                String venue_time = (String) item.get("time");
                System.out.println(venue_time);
                TimeRange timeRange = (TimeRange) TimeFormat.normalToAbsolute(venue_time);
                List<TimeRange> timeRanges; //list of all timeRanges for this venue
                if (ranges.containsKey(venue_name)) {
                    timeRanges = ranges.get(venue_name);
                } else {
                    timeRanges = new ArrayList<TimeRange>();
                }
                timeRanges.add(timeRange);
                ranges.put(venue_name, timeRanges);
            }
            for (String key : ranges.keySet()){
                int venue_size = sizes.get(key);
                List<TimeRange> timeRanges = ranges.get(key);
                venues.add(new Venue(key, venue_size, timeRanges));
            }


            //System.out.println("Setting up panelists...");
            Map<String, List<TimeRange>> panelists = new HashMap<String, List<TimeRange>>();
            for (Object o : json_panelists) {
                JSONObject item = (JSONObject) o;
                String panelist_name = (String) item.get("name");
                Boolean noob = (item.get("new") == "yes");
                JSONArray json_times = (JSONArray) item.get("times");
                List<TimeRange> panelist_times = new ArrayList<TimeRange>();
                for (Object time_slot : json_times) {
                    String panelist_time = (String) time_slot;
                    TimeRange timeRange = (TimeRange) TimeFormat.normalToAbsolute(panelist_time);
                    panelist_times.add(timeRange); //panelist_time
                }
                if (noob){
                    panelists.put("n_" + panelist_name, panelist_times);
                }
                else {
                    panelists.put(panelist_name, panelist_times);
                }
            }


            //System.out.println("Creating Panels...");
            for (Object o : json_panels) {
                JSONObject item = (JSONObject) o;
                String panel_name = (String) item.get("name");
                JSONArray panel_panelists = (JSONArray) item.get("panelists");
                JSONArray json_constraints = (JSONArray) item.get("constraints");
                String categories = (String) item.get("categories");

                List<String> names = new ArrayList<String>();
                List<Range> availability = new ArrayList<Range>();
                for (Object panelist : panel_panelists) {
                    String name = (String) panelist;
                    names.add(name);
                    Collection<TimeRange> times = panelists.get(name);
                    availability.add(new TimeRangeSeries(times));
                }
                Range intersection = availability.get(0).intersect(availability);

                List<String> categoryList = new ArrayList<String>();
                for (String category : categories.split(",")){
                    categoryList.add(category);
                }

                List<String> constraints = new ArrayList<String>();
                //TODO set panel constraints
                for (Object k : json_constraints) {
                    String constraint = (String) k;
                    constraints.add(constraint);
                }

                panels.add(new Panel(panel_name, names, intersection, categoryList, constraints));
            }


            output(venues, panels);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }


    //output function
    static void output(List<Venue> venues, List<Panel> panels){
        System.out.println("Venue Availability:");
        for (Venue venue : venues){
            System.out.println("\t" + venue.NAME + ": ");
            List<VenueTime> freeTimes = venue.getFreeVenueTimes();
            for (VenueTime times : freeTimes){
                System.out.println("\t\t" + times.TIME.getStart() + " - " + times.TIME.getEnd());
            }
        }
        System.out.println("");
        System.out.println("Panel Availability:");
        for (Panel panel : panels){
            System.out.println("\t" + panel.getName() + ": ");
            for (TimeRange range : panel.getAvailability()){
                System.out.println("\t\t" + range.START + " - " + range.END);
            }
        }
        System.out.println("Done");
    }
}


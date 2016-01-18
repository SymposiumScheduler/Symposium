package symposium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import symposium.model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import symposium.model.Panel;

public class Parser {
    public static void initScheduleData(String inputFile) {
        List<symposium.model.Panel> panels = new ArrayList<Panel>();
        List<Venue> venues = new ArrayList<Venue>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(inputFile));
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
            List<String> new_panelists = new ArrayList<String>();
            for (Object o : json_panelists) {
                JSONObject item = (JSONObject) o;
                String panelist_name = (String) item.get("name");
                JSONArray json_times = (JSONArray) item.get("times");
                List<TimeRange> panelist_times = new ArrayList<TimeRange>();
                for (Object time_slot : json_times) {
                    String panelist_time = (String) time_slot;
                    TimeRange timeRange = (TimeRange) TimeFormat.normalToAbsolute(panelist_time);
                    panelist_times.add(timeRange); //panelist_time
                }
                if (item.get("new") == "yes"){
                    new_panelists.add(panelist_name);
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
                String categories = (String) item.get("category");
                int new_count = 0;

                List<String> names = new ArrayList<String>();
                List<Range> availability = new ArrayList<Range>();
                for (Object panelist : panel_panelists) {
                    String name = (String) panelist;
                    names.add(name);
                    Collection<TimeRange> times = panelists.get(name);
                    availability.add(new TimeRangeSeries(times));
                    if (new_panelists.contains(name)){
                        new_count += 1;
                    }
                }
                Range intersection = null;
                if(availability.size() > 0) {
                    intersection = availability.get(0).intersect(availability);
                }

                List<String> categoryList = new ArrayList<String>();
                for (String category : categories.split(",")){
                    categoryList.add(category);
                }

                List<String> constraints = new ArrayList<String>();
                for (Object k : json_constraints) {
                    String constraint = (String) k;
                    constraints.add(constraint);
                }
                if (new_count > 1){
                    constraints.add("New-Panelist");
                }
                panels.add(new Panel(panel_name, names, intersection, categoryList, constraints));
            }
            ScheduleData.init(venues, panels, 4); // FIXME: 4 should be changed to the number of days in the whole space (might not be necessary anymore)
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


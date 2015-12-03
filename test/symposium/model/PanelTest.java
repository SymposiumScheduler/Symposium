package symposium.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by yousef-alsber on 11/30/15.
 *
 *  Panel(String name, List<String> panelists, Range availability, List<String> category, List<String> constraints)
 */

public class PanelTest {


    @Test
    public void testConstructor() throws Exception {

        String panelName = "NewPanel";
        List<String> newCategory = new ArrayList<String>();
        newCategory.add("newCategory");

        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        List<String> panelists = new ArrayList<String>();
        panelists.add("Joey");
        panelists.add("Yousef");


        Range range = new TimeRange(10, 20);

        Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);


        assertTrue(panel.NAME.equals(panelName));
        assertTrue(panel.PANELISTS.get(0).equals("Joey"));
        assertTrue(panel.AVAILABILITY.equals(range));
        assertTrue(panel.CATEGORIES.get(0).equals("newCategory"));
        assertTrue(panel.CONSTRAINTS.get(0).equals("New"));




        //  assertTrue(panel.getDifficulty() == 0);
        // panel.setDifficulty(3);
        // assertTrue(panel.getDifficulty() == 3);
       // assertTrue(panel.getVenueTime() == null);


        // I don't know what the hell to test next given the panel class is not compatible with anything

    }


    @Test
    public void testSetDifficulty() throws Exception {

        String panelName = "NewPanel";
        List<String> newCategory = new ArrayList<String>();
        newCategory.add("newCategory");

        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        List<String> panelists = new ArrayList<String>();
        panelists.add("Joey");
        panelists.add("Yousef");


        Range range = new TimeRange(10, 20);

        Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

        panel.setDifficulty(2);

        assertTrue(panel.difficulty == 2);


    }

     @Test
     public void testGetDifficulty() throws Exception {

         String panelName = "NewPanel";
         List<String> newCategory = new ArrayList<String>();
         newCategory.add("newCategory");

         List<String> constraints = new ArrayList<String>();
         constraints.add("New");
         List<String> panelists = new ArrayList<String>();
         panelists.add("Joey");
         panelists.add("Yousef");


         Range range = new TimeRange(10, 20);

         Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);

         panel.setDifficulty(2);

         assertTrue(panel.getDifficulty() == 2);

     }


     @Test
     public void testSetVenueTime() throws Exception {

         String panelName = "NewPanel";
         List<String> newCategory = new ArrayList<String>();
         newCategory.add("newCategory");

         List<String> constraints = new ArrayList<String>();
         constraints.add("New");
         List<String> panelists = new ArrayList<String>();
         panelists.add("Joey");
         panelists.add("Yousef");


         Range range = new TimeRange(10, 20);

         Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);


         List<TimeRange> ranges = new ArrayList<>(3);
         ranges.add(new TimeRange(10,20));
         ranges.add(new TimeRange(30,40));
         ranges.add(new TimeRange(50,60));

         Venue venue = new Venue("NewVenue", 5, ranges);

         VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);

         panel.setVenueTime(vt);

         assertTrue(panel.assignedVenueTime.equals(vt));

     }

    @Test
    public void testGetVenueTime() throws Exception {

        String panelName = "NewPanel";
        List<String> newCategory = new ArrayList<String>();
        newCategory.add("newCategory");

        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        List<String> panelists = new ArrayList<String>();
        panelists.add("Joey");
        panelists.add("Yousef");


        Range range = new TimeRange(10, 20);

        Panel panel = new Panel(panelName, panelists, range, newCategory, constraints);


        List<TimeRange> ranges = new ArrayList<>(3);
        ranges.add(new TimeRange(10,20));
        ranges.add(new TimeRange(30,40));
        ranges.add(new TimeRange(50,60));

        Venue venue = new Venue("NewVenue", 5, ranges);

        VenueTime vt = new VenueTime(new TimeRange(10, 20), venue);

        panel.setVenueTime(vt);

        assertTrue(panel.getVenueTime().equals(vt));

    }


    @Test
    public void testCompareTo() throws Exception {
       // I don't know what is the method supposed to do.

    }
}
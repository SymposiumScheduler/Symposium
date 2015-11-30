package symposium.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by yousef-alsber on 11/30/15.
 */

public class PanelTest {


    @Test
    public void testConstructor() throws Exception {

        String panelName = "NewPanel";
        String newCategory = "newCategory";
        List<String> constraints = new ArrayList<String>();
        constraints.add("New");
        Map<String, TimeRangeSeries> panalists = new HashMap<String, TimeRangeSeries>();

        Panel panel = new Panel(panelName, panalists, newCategory, constraints);


        assertTrue(panel.name.equals(panelName));
        assertTrue(panel.category.equals(newCategory));
        assertTrue(panel.constraints.get(0).equals("New"));
        assertTrue(panel.getDifficulty() == 0);
        panel.setDifficulty(3);
        assertTrue(panel.getDifficulty() == 3);
        assertTrue(panel.getVenueTime() == null);


        // I don't know what the hell to test next given the panel class is not compatible with anything

    }

    /**
     *
     * General rule for testing no need to test setters and getters if they don't have any logic in them
     *
     * @Test
    public void testSetDifficulty() throws Exception {


    }

     @Test
     public void testGetDifficulty() throws Exception {
     // Method not implemented

     }


     @Test


     @Test
     public void testSetVenueTime() throws Exception {

     }

     *
     * @throws Exception
     */


    @Test
    public void testCompareTo() throws Exception {
       // I don't know what is the method supposed to do.

    }
}
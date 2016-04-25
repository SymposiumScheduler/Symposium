package symposium;

import org.junit.Test;
import symposium.model.Panel;
import symposium.model.ScheduleData;
import static org.junit.Assert.*;

public class DummySchedulerTest {
    @Test
    public void testMakeSchedule() throws Exception {
        String inputFilePath = "datas/dataTest.json";
        int[] diffValues = new int[5];
        diffValues[0] = 10;
        diffValues[1] = 100000;
        diffValues[2] = 100000;
        diffValues[3] = 10000000;
        diffValues[4] = 100;

        Parser.parse(inputFilePath);

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();

        assertTrue(ScheduleData.instance().getAssignedPanels().size() == 5);
        assertTrue(ScheduleData.instance().getUnschedulablePanels().size() == 1);
        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testSetDifficultyTrivial() throws Exception {
        String inputFilePath = "datas/dataTest.json";
        int[] diffValues = new int[5];
        diffValues[0] = 10;
        diffValues[1] = 100000;
        diffValues[2] = 100000;
        diffValues[3] = 10000000;
        diffValues[4] = 100;

        Parser.parse(inputFilePath);

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();

        for (Panel p: ScheduleData.instance().getAssignedPanels()) {
            assertTrue(p.getDifficulty() == 0);
        }

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testSetDifficultyNonTrivial() throws Exception {
        String inputFilePath = "datas/dataLargeTest.json";
        int[] diffValues = new int[5];
        diffValues[0] = 10;
        diffValues[1] = 100000;
        diffValues[2] = 100000;
        diffValues[3] = 10000000;
        diffValues[4] = 100;

        Parser.parse(inputFilePath);

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();

        for (Panel p: ScheduleData.instance().getAssignedPanels()) {
            assertTrue(p.getDifficulty() == 0 || p.getDifficulty() > 2000);
        }

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testIndividualDifficultyFactors() {
        String inputFilePath = "datas/dataLargeTest.json";
        int[] diffValues = new int[5];
        diffValues[0] = 10;
        diffValues[1] = 100000;
        diffValues[2] = 100000;
        diffValues[3] = 10000000;
        diffValues[4] = 100;

        Parser.parse(inputFilePath);

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();

        for (Panel p: ScheduleData.instance().getAssignedPanels()) {
            assertTrue(p.getDifficulty() == 0 || p.getDifficulty() > 2000);
            if (p.toString().contains("SO MANY GUITARS")) {
                assertTrue(p.getDifficulty() > 2000);
                assertTrue(bs.availabilityDifficulty(p) > 6000);
                assertTrue(bs.sizeConstraintDifficulty(p) == 10);
                assertTrue(bs.TimeConstraintDifficulty(p) == 0);
                assertTrue(bs.venueConstraintDifficulty(p) > 10000 );
            } else if (p.toString().contains("Performance - Rene")) {
                assertTrue(p.getDifficulty() > 2000);
                System.out.println(bs.sizeConstraintDifficulty(p));
                assertTrue(bs.availabilityDifficulty(p) > 5000);
                assertTrue(bs.sizeConstraintDifficulty(p) == 10);
                assertTrue(bs.TimeConstraintDifficulty(p) == 0);
                assertTrue(bs.venueConstraintDifficulty(p) > 10000);
            }
        }

        ScheduleData.deleteScheduleData();
    }

    @Test
    public void testMessages() throws Exception {
        String inputFilePath = "datas/dataTest.json";
        int[] diffValues = new int[5];
        diffValues[0] = 10;
        diffValues[1] = 100000;
        diffValues[2] = 100000;
        diffValues[3] = 10000000;
        diffValues[4] = 100;

        Parser.parse(inputFilePath);

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();

        assertTrue(ScheduleData.instance().getAssignedPanels().get(0).toString().contains("New Panelist Constraint"));
        assertTrue(ScheduleData.instance().getAssignedPanels().get(0).toString().contains("Athena Edmonds [14624], Aaron Balick [14628]"));
        assertTrue(ScheduleData.instance().getUnschedulablePanels().get(0).toString().contains("Venue Time Duration Filter"));
        assertTrue(ScheduleData.instance().getUnschedulablePanels().get(0).toString().contains("SpecificTimeFilter"));
        assertTrue(ScheduleData.instance().getUnschedulablePanels().get(0).toString().contains("1 times"));
        assertTrue(ScheduleData.instance().getUnschedulablePanels().get(0).toString().contains("4 times"));

        ScheduleData.deleteScheduleData();
    }
}

package symposium;

import symposium.model.ScheduleData;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        evolutionary();


        //  normalStart();
    }

    public static void evolutionary(){

        int[] optimaladjustAND = new int[4];
        int[] optimalscoreAND = new int[2];
        int[] adjustAND = new int[4];

        int[] optimaladjustOR = new int[4];
        int[] optimalscoreOR = new int[2];
        int[] adjustOR = new int[4];

        int[] optimaladjustMes = new int[4];
        int[] optimalscoreMes = new int[2];
        int[] adjustMes = new int[4];

        int[][] provisionaladjustment = new int[5][4];

        optimaladjustAND[0] = 1;
        optimaladjustAND[1] = 1;
        optimaladjustAND[2] = 1;
        optimaladjustAND[3] = 1;
        optimalscoreAND[0] = 1000000;
        optimalscoreAND[1] = 1000000;

        optimaladjustOR[0] = 1;
        optimaladjustOR[1] = 1;
        optimaladjustOR[2] = 1;
        optimaladjustOR[3] = 1;
        optimalscoreOR[0] = 1000000;
        optimalscoreOR[1] = 1000000;

        optimaladjustMes[0] = 1;
        optimaladjustMes[1] = 1;
        optimaladjustMes[2] = 1;
        optimaladjustMes[3] = 1;
        optimalscoreMes[0] = 1000000;
        optimalscoreMes[1] = 1000000;


        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            adjustAND[0] = optimaladjustAND[0];
            adjustAND[1] = optimaladjustAND[1];
            adjustAND[2] = optimaladjustAND[2];
            adjustAND[3] = optimaladjustAND[3];

            adjustOR[0] = optimaladjustOR[0];
            adjustOR[1] = optimaladjustOR[1];
            adjustOR[2] = optimaladjustOR[2];
            adjustOR[3] = optimaladjustOR[3];

            adjustMes[0] = optimaladjustMes[0];
            adjustMes[1] = optimaladjustMes[1];
            adjustMes[2] = optimaladjustMes[2];
            adjustMes[3] = optimaladjustMes[3];

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    provisionaladjustment[j][k] = (int) Math.round(Math.random() * 3 - 1);
                }

                // Reading parsing json files
                final String INPUT_FILE = "datas/data.txt";
                Parser.parse(INPUT_FILE);
                // Schedule data is initiated
                for (int m = 0; m < 4; m++) {
                    adjustAND[m] = adjustAND[m] * (int) Math.pow(10, provisionaladjustment[j][m]);
                    adjustOR[m] = adjustOR[m] * (int) Math.pow(10, provisionaladjustment[j][m]);
                    adjustMes[m] = adjustMes[m] * (int) Math.pow(10, provisionaladjustment[j][m]);
                }

                DummyScheduler bs = new DummyScheduler(adjustAND);
                bs.makeSchedule();


                int[] score = ScheduleData.instance().calculateScheduleOptimization();
                if (score[0] < optimalscoreAND[0] && score[1] < optimalscoreAND[1]) {
                    optimalscoreAND[0] = score[0];
                    optimalscoreAND[1] = score[1];
                    optimaladjustAND[0] = adjustAND[0];
                    optimaladjustAND[1] = adjustAND[1];
                    optimaladjustAND[2] = adjustAND[2];
                    optimaladjustAND[3] = adjustAND[3];
                }

                if (score[0] < optimalscoreOR[0] || score[1] < optimalscoreOR[1]) {
                    optimalscoreOR[0] = score[0];
                    optimalscoreOR[1] = score[1];
                    optimaladjustOR[0] = adjustOR[0];
                    optimaladjustOR[1] = adjustOR[1];
                    optimaladjustOR[2] = adjustOR[2];
                    optimaladjustOR[3] = adjustOR[3];
                }

                if (score[0] < optimalscoreMes[0]) {
                    optimalscoreMes[0] = score[0];
                    optimalscoreMes[1] = score[1];
                    optimaladjustMes[0] = adjustMes[0];
                    optimaladjustMes[1] = adjustMes[1];
                    optimaladjustMes[2] = adjustMes[2];
                    optimaladjustMes[3] = adjustMes[3];
                }

                ScheduleData.deleteScheduleData();
            }
        }
        System.out.println("\n-------------------THE RESULTS BY DECREASING BOTH VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscoreAND[0]);
        System.out.println("Unscheduled = " + optimalscoreAND[1]);
        System.out.println("Scale for Size = " + optimaladjustAND[0]);
        System.out.println("Scale for Venue = " + optimaladjustAND[1]);
        System.out.println("Scale for Time = " + optimaladjustAND[2]);
        System.out.println("Scale for Availability = " + optimaladjustAND[3]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING EITHER VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscoreOR[0]);
        System.out.println("Unscheduled = " + optimalscoreOR[1]);
        System.out.println("Scale for Size = " + optimaladjustOR[0]);
        System.out.println("Scale for Venue = " + optimaladjustOR[1]);
        System.out.println("Scale for Time = " + optimaladjustOR[2]);
        System.out.println("Scale for Availability  = " + optimaladjustOR[3]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING ONLY VIOLATIONS-------------------");
        System.out.println("Violations = " + optimalscoreMes[0]);
        System.out.println("Unscheduled = " + optimalscoreMes[1]);
        System.out.println("Scale for Size = " + optimaladjustMes[0]);
        System.out.println("Scale for Venue = " + optimaladjustMes[1]);
        System.out.println("Scale for Time = " + optimaladjustMes[2]);
        System.out.println("Scale for Availability = " + optimaladjustMes[3]);

    }

    public static void normalStart(){
        final String INPUT_FILE = "datas/data.txt";
        Parser.parse(INPUT_FILE);
        // Schedule data is initiated
        DummyScheduler bs = new DummyScheduler();
        bs.makeSchedule();
        // Print report
        System.out.print(Report.INSTANCE);

    }
}

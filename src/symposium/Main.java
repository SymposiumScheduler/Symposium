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
        int[][] optimaladjust = new int[3][4];
        int[][] optimalscore = new int[3][2];
        int[] adjust = new int[4];

        int[][] provisionaladjustment = new int[5][4];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                optimaladjust[i][j] = 1;
            }
            for (int j=0; j < 2; j++) {
                optimalscore[i][j] = 1000000;
            }
        }

        for (int i = 0; i < 100; i++) {
            adjust[0] = optimaladjust[0][0];
            adjust[1] = optimaladjust[0][1];
            adjust[2] = optimaladjust[0][2];
            adjust[3] = optimaladjust[0][3];

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 4; k++) {
                    provisionaladjustment[j][k] = (int) Math.round(Math.random() * 2 - 1);
                }

                // Reading parsing json files
                final String INPUT_FILE = "datas/data.txt";
                Parser.parse(INPUT_FILE);
                // Schedule data is initiated
                for (int m = 0; m < 4; m++) {
                    adjust[m] = (int) ((double) adjust[m] * Math.pow(10, provisionaladjustment[j][m]));
                    if (adjust[m] == 0) {
                        adjust[m] = 1;
                    }
                }

                DummyScheduler bs = new DummyScheduler(adjust);
                bs.makeSchedule();


                int[] score = ScheduleData.instance().calculateScheduleOptimization();
                boolean check = false;
                for (int m = 0; m < 3; m++) {
                    if (m == 0) {
                        check = (score[0] < optimalscore[0][0] && score[1] < optimalscore[0][1]);
                    } else if (m == 1) {
                        check = (score[0] < optimalscore[1][0] || score[1] < optimalscore[1][1]);
                    } else if (m == 2) {
                        check = (score[0] < optimalscore[2][0]);
                    }

                    if (check) {
                        optimalscore[m][0] = score[0];
                        optimalscore[m][1] = score[1];
                        optimaladjust[m][0] = adjust[0];
                        optimaladjust[m][1] = adjust[1];
                        optimaladjust[m][2] = adjust[2];
                        optimaladjust[m][3] = adjust[3];
                    }
                }
                ScheduleData.deleteScheduleData();
            }
        }
        System.out.println("\n-------------------THE RESULTS BY DECREASING BOTH VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscore[0][0]);
        System.out.println("Unscheduled = " + optimalscore[0][1]);
        System.out.println("Scale for Size = " + optimaladjust[0][0]);
        System.out.println("Scale for Venue = " + optimaladjust[0][1]);
        System.out.println("Scale for Time = " + optimaladjust[0][2]);
        System.out.println("Scale for Availability = " + optimaladjust[0][3]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING EITHER VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscore[1][0]);
        System.out.println("Unscheduled = " + optimalscore[1][1]);
        System.out.println("Scale for Size = " + optimaladjust[1][0]);
        System.out.println("Scale for Venue = " + optimaladjust[1][1]);
        System.out.println("Scale for Time = " + optimaladjust[1][2]);
        System.out.println("Scale for Availability  = " + optimaladjust[1][3]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING ONLY VIOLATIONS-------------------");
        System.out.println("Violations = " + optimalscore[2][0]);
        System.out.println("Unscheduled = " + optimalscore[2][1]);
        System.out.println("Scale for Size = " + optimaladjust[2][0]);
        System.out.println("Scale for Venue = " + optimaladjust[2][1]);
        System.out.println("Scale for Time = " + optimaladjust[2][2]);
        System.out.println("Scale for Availability = " + optimaladjust[2][3]);

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
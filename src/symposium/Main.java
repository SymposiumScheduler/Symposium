package symposium;

import symposium.model.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        boolean evolutionFlag = false;
        String inputFilePath = "datas/data2016.json"; // default file
        for (String arg: args) {
            if (arg.equals("--evolution")) {
                evolutionFlag = true;
            } else {
                inputFilePath = arg;
            }
        }

        if (evolutionFlag) {
            evolutionary(inputFilePath);
        } else {
            standard(inputFilePath);
        }
    }

    public static void standard(String inputFilePath) {
        int[] diffValues = new int[5];
        diffValues[0] = 10;
        diffValues[1] = 100000;
        diffValues[2] = 100000;
        diffValues[3] = 100000000;
        diffValues[4] = 100; // panelist

        // Reading parsing json files
        Parser.parse(inputFilePath);
        // Schedule data is initiated

        DummyScheduler bs = new DummyScheduler(diffValues);
        bs.makeSchedule();
        //long elapsedTime = System.nanoTime() - initTime;

        // Print report
        System.out.println(Report.INSTANCE.toString());

        System.out.println(_DebugStatus.__debugStats());
        //System.out.println("\n\n\nTIME= " + (elapsedTime/(double)1000000000) + " ± 0.05 s");
    }

    public static void evolutionary(String inputFilePath){
        int[][] optimaladjust = new int[3][5];
        int[][] optimalscore = new int[3][2];
        int[] adjust = new int[5];

        int[][] provisionaladjustment = new int[10][5];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                optimaladjust[i][j] = 1;
            }
            for (int j=0; j < 2; j++) {
                optimalscore[i][j] = 1000000;
            }
        }

        for (int i = 0; i < 10; i++) {
            adjust[0] = optimaladjust[0][0];
            adjust[1] = optimaladjust[0][1];
            adjust[2] = optimaladjust[0][2];
            adjust[3] = optimaladjust[0][3];
            adjust[4] = optimaladjust[0][4];

            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < adjust.length; k++) {
                    provisionaladjustment[j][k] = (int) Math.round(Math.random());
                }

                Parser.parse(inputFilePath);
                // Schedule data is initiated
                for (int m = 0; m < adjust.length; m++) {
                    adjust[m] = adjust[m] * (int) Math.pow(8, provisionaladjustment[j][m]);
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
                        optimaladjust[m][4] = adjust[4];
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
        System.out.println("Scale for Panelists = " + optimaladjust[0][4]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING EITHER VIOLATIONS AND UNSCHEDULED-------------------");
        System.out.println("Violations = " + optimalscore[1][0]);
        System.out.println("Unscheduled = " + optimalscore[1][1]);
        System.out.println("Scale for Size = " + optimaladjust[1][0]);
        System.out.println("Scale for Venue = " + optimaladjust[1][1]);
        System.out.println("Scale for Time = " + optimaladjust[1][2]);
        System.out.println("Scale for Availability  = " + optimaladjust[1][3]);
        System.out.println("Scale for Panelists = " + optimaladjust[0][4]);

        System.out.println("\n-------------------THE RESULTS BY DECREASING ONLY VIOLATIONS-------------------");
        System.out.println("Violations = " + optimalscore[2][0]);
        System.out.println("Unscheduled = " + optimalscore[2][1]);
        System.out.println("Scale for Size = " + optimaladjust[2][0]);
        System.out.println("Scale for Venue = " + optimaladjust[2][1]);
        System.out.println("Scale for Time = " + optimaladjust[2][2]);
        System.out.println("Scale for Availability = " + optimaladjust[2][3]);
        System.out.println("Scale for Panelists = " + optimaladjust[0][4]);
    }
}

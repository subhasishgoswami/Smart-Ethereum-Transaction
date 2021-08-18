package org.codes.dataset;

import org.web3j.abi.datatypes.Int;

import java.io.*;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionTime {


    public static void main(String[] args) throws IOException {
        String pathToBlockCsv = "blocks_10985851.csv";
        BufferedReader blockCsvReader = new BufferedReader(new FileReader(pathToBlockCsv));
        String row;
        Map<Long, Long> blockToMinedTime = new HashMap<>();
        while ((row = blockCsvReader.readLine()) != null) {
            String[] data = row.split(",");
//            System.out.println(Arrays.toString(data));

            String date = data[1].substring(0, 23);
            String pattern = "MMM-dd-yyyy hh:mm:ss a";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            blockToMinedTime.put(Long.parseLong(data[0]), toEpochMilli(dateTime));
        }

        Map<Long,String[]> blockToBlockMineDuration = new HashMap<>();
        String pathToBlockMineDurationCsv = "blockMineDuration.csv";
        BufferedReader blockMineDurationCsvReader = new BufferedReader(new FileReader(pathToBlockMineDurationCsv));
        row = blockMineDurationCsvReader.readLine();
        while ((row = blockMineDurationCsvReader.readLine()) != null) {
            String[] data = row.split(",");
            blockToBlockMineDuration.put(Long.parseLong(data[0]),data);
//            System.out.println(Arrays.toString(blockToBlockMineDuration.get(Long.parseLong(data[0]))));
        }

        String pathToTransactionCsv = "dataset_final.csv";
        BufferedReader transactionCsvReader = new BufferedReader(new FileReader(pathToTransactionCsv));
        row = transactionCsvReader.readLine();
        Map<Long,String> blockToDifficulty= new HashMap<>();

        long[] transactionCountAppear = new long[96];
        double[] gasPriceSumHourlyAppear = new double[96];
        long[] waitingTimeHourlyAppear = new long[96];

        long[] transactionCountAdded = new long[96];
        double[] gasPriceSumHourlyAdded = new double[96];
        long[] waitingTimeHourlyAdded = new long[96];
        BigInteger[] blockDifficultyAdded = new BigInteger[96];
        for (int i = 0; i < 24; i++) {
//            blockDifficultyAdded[i]="0";
            blockDifficultyAdded[i] = new BigInteger("0");
        }
        PrintWriter printWriter = new PrintWriter(new File("transactionTime.csv"));
        PrintWriter errorTransaction = new PrintWriter(new File("errorTransaction.csv"));
        int wastedCounts = 0 , i = 0;
        printWriter.println("tx_hash,status,block,wt(secs),value(Eth),gas_price,gas_limit,difficulty,CA(0)/EA(1),previousBlockDifficulty,ISOTimeAppear,appearEpochTimestamp,appearHour,ISOBlockTime,BlockAddedHour,blockTimestamp,time2mine,last20,last40,last60,last120,last240,last300,last360,last420,last480,last540,last600,last3600");
        while ((row = transactionCsvReader.readLine()) != null) {
            try {
                i++;
                String[] data = row.split(",");
                String previousBlockDifficulty = null;
                LocalDateTime localBlockAddTime=null;
                LocalDateTime localAppearTime=null;
    //            System.out.println(data[0]+" "+i);
                long transactionBlock = Long.parseLong(data[2]);
//                System.out.println(data[0]);
                blockToDifficulty.put(transactionBlock,data[7]);
                String[] blockMineDurationData = blockToBlockMineDuration.get(transactionBlock);
                previousBlockDifficulty = blockToDifficulty.get(transactionBlock-1)==null?"unk":blockToDifficulty.get(transactionBlock-1);
                long blockAddedTime = blockToMinedTime.get(transactionBlock);
                localBlockAddTime =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(blockAddedTime),
                                TimeZone.getDefault().toZoneId());
                String ISO_TimeAdded="";
                ISO_TimeAdded = DateTimeFormatter.ISO_DATE_TIME.format(localBlockAddTime);
                if (localBlockAddTime.getMinute() < 15) {
                    transactionCountAdded[localBlockAddTime.getHour()*4]++;
                    gasPriceSumHourlyAdded[localBlockAddTime.getHour()*4] += Double.parseDouble(data[5]);
                } else if (localBlockAddTime.getMinute() < 30) {
                    transactionCountAdded[localBlockAddTime.getHour()*4+1]++;
                    gasPriceSumHourlyAdded[localBlockAddTime.getHour()*4+1] += Double.parseDouble(data[5]);
                } else if (localBlockAddTime.getMinute() < 45) {
                    transactionCountAdded[localBlockAddTime.getHour()*4+2]++;
                    gasPriceSumHourlyAdded[localBlockAddTime.getHour()*4+2] += Double.parseDouble(data[5]);
                } else {
                    transactionCountAdded[localBlockAddTime.getHour()*4+3]++;
                    gasPriceSumHourlyAdded[localBlockAddTime.getHour()*4+3] += Double.parseDouble(data[5]);
                }
                BigInteger blockDifficulty = new BigInteger(data[7]);
    //            blockDifficultyAdded[localBlockAddTime.getHour()]=blockDifficultyAdded[localBlockAddTime.getHour()].add(new BigInteger(data[8]));
    //            try {
                    long waitTime = Long.parseLong(data[3]);

                    long appearTime = blockToMinedTime.get(transactionBlock) - waitTime * 1000;



                    localAppearTime =
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(appearTime),
                                    TimeZone.getDefault().toZoneId());
                    String ISO_TimeAppear = DateTimeFormatter.ISO_DATE_TIME.format(localAppearTime);

    //
                    if (localAppearTime.getMinute() < 15) {
                        transactionCountAppear[localAppearTime.getHour()*4]++;
                        gasPriceSumHourlyAppear[localAppearTime.getHour()*4] += Double.parseDouble(data[5]);
                        waitingTimeHourlyAppear[localAppearTime.getHour()*4] += Long.parseLong(data[3]);
                    } else if (localAppearTime.getMinute() < 30) {
                        transactionCountAppear[localAppearTime.getHour()*4+1]++;
                        gasPriceSumHourlyAppear[localAppearTime.getHour()*4+1] += Double.parseDouble(data[5]);
                        waitingTimeHourlyAppear[localAppearTime.getHour()*4+1] += Long.parseLong(data[3]);
                    } else if (localAppearTime.getMinute() < 45) {
                        transactionCountAppear[localAppearTime.getHour()*4+2]++;
                        gasPriceSumHourlyAppear[localAppearTime.getHour()*4+2] += Double.parseDouble(data[5]);
                        waitingTimeHourlyAppear[localAppearTime.getHour()*4+2] += Long.parseLong(data[3]);
                    } else {
                        transactionCountAppear[localAppearTime.getHour()*4+3]++;
                        gasPriceSumHourlyAppear[localAppearTime.getHour()*4+3] += Double.parseDouble(data[5]);
                        waitingTimeHourlyAppear[localAppearTime.getHour()*4+3] += Long.parseLong(data[3]);
                    }
                    if (localBlockAddTime.getMinute() < 15) {
                        waitingTimeHourlyAdded[localBlockAddTime.getHour()*4] += Long.parseLong(data[3]);
                    } else if (localBlockAddTime.getMinute() < 30) {
                        waitingTimeHourlyAdded[localBlockAddTime.getHour()*4+1] += Long.parseLong(data[3]);
                    } else if (localBlockAddTime.getMinute() < 45) {
                        waitingTimeHourlyAdded[localBlockAddTime.getHour()*4+2] += Long.parseLong(data[3]);
                    } else {
                        waitingTimeHourlyAdded[localBlockAddTime.getHour()*4+3] += Long.parseLong(data[3]);
                    }
                    String txCountAvgDeterminer="";

    //                System.out.println(blockDifficultyAdded[localBlockAddTime.getHour()]);
                    printWriter.println(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5] + "," + data[6] + "," + data[7] + "," + data[8] + "," +  previousBlockDifficulty + "," + ISO_TimeAppear + "," + appearTime + "," + localAppearTime.getHour() + "," + ISO_TimeAdded + "," +localBlockAddTime.getHour() +"," + blockMineDurationData[1] + "," + blockMineDurationData[2] + "," + blockMineDurationData[3] +  "," + blockMineDurationData[4] +  "," + blockMineDurationData[5]+  "," + blockMineDurationData[6] +  "," + blockMineDurationData[7] +   "," + blockMineDurationData[8]+  "," + blockMineDurationData[9]+  "," + blockMineDurationData[10] +  "," + blockMineDurationData[11] +  "," + blockMineDurationData[12] +  "," + blockMineDurationData[13] +  "," + blockMineDurationData[14]);
            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(row);
                wastedCounts ++;
                if (!(e.getMessage().equals("For input string: \"unk\""))) {
                    System.out.println(e.getMessage());
                    errorTransaction.println(row);
                }
            }
        }
        System.out.println(blockToDifficulty);
        errorTransaction.close();
        printWriter.close();
        System.out.println(i + " wastedCountsTx = "+wastedCounts);
        System.out.println(Arrays.toString(blockToBlockMineDuration.get(10985913)));

        for (int j = 0; j < 24; j++) {
            System.out.println(blockDifficultyAdded[j]);
        }

        System.out.println();
        System.out.println();

        System.out.println("hourAppear,txCountAppear,wtTimeAvgAppear,gasPriceAvgAppear");
        for (int j = 0; j < 96; j++) {
//            System.out.println(transactionCountAppear[j]+","+(gasPriceSumHourlyAppear[j]/transactionCountAppear[j]));

            System.out.printf("%f,",j*1.0/4);
            System.out.printf("%d,", transactionCountAppear[j]);
            System.out.printf("%d,", waitingTimeHourlyAppear[j]/ transactionCountAppear[j]);
            System.out.printf("%.12f \n",(gasPriceSumHourlyAppear[j]/ transactionCountAppear[j]) );
        }
        System.out.println("\n");

        System.out.println("hour,txCountAdded,wtTimeAvgAdded,gasPriceAvgAdded");
        for (int j = 0; j < 96; j++) {
//            System.out.println(transactionCountAppear[j]+","+(gasPriceSumHourlyAppear[j]/transactionCountAppear[j]));
//            BigInteger avgDifficulty = new BigInteger();
            String avgDifficulty="";
//            try {
//                avgDifficulty = blockDifficultyAdded[j].divide(new BigInteger("" + transactionCountAdded[j])).toString();
//            } catch (Exception e) {
//                System.out.printf(j+",0,0,0,0\n");
//                continue;
//            }
            System.out.printf("%f,",j*1.0/4);
            System.out.printf("%d,", transactionCountAdded[j]);
            System.out.printf("%d,", waitingTimeHourlyAdded[j]);
            System.out.printf("%.12f \n",(gasPriceSumHourlyAdded[j]) );
        }

    }

    private static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
    }

}
// net Tx: 489667 wastedCountsTx = 30419
// minimum block 	10985913
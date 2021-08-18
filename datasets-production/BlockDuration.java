package dataset;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BlockDuration {

    static class BlockFileData {
        private int blockNumber;
        private long timestamp;
        private int timeToMine;
        private int mined20;
        private int mined40;
        private int mined60;
        private int mined120;
        private int mined180;
        private int mined240;
        private int mined300;
        private int mined360;
        private int mined420;
        private int mined480;
        private int mined540;
        private int mined600;
        private int mined3600;

        public BlockFileData(int blockNumber, long timestamp, int timeToMine) {
            this.blockNumber = blockNumber;
            this.timestamp = timestamp;
            this.timeToMine = timeToMine;
        }

        public int getMined20() {
            return mined20;
        }

        public void setMined20(int mined20) {
            this.mined20 = mined20;
        }

        public int getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            this.blockNumber = blockNumber;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getMined40() {
            return mined40;
        }

        public void setMined40(int mined40) {
            this.mined40 = mined40;
        }

        public int getMined60() {
            return mined60;
        }

        public void setMined60(int mined60) {
            this.mined60 = mined60;
        }

        public int getMined120() {
            return mined120;
        }

        public void setMined120(int mined120) {
            this.mined120 = mined120;
        }

        public int getTimeToMine() {
            return timeToMine;
        }

        public void setTimeToMine(int timeToMine) {
            this.timeToMine = timeToMine;
        }

        public int getMined180() {
            return mined180;
        }

        public void setMined180(int mined180) {
            this.mined180 = mined180;
        }

        public int getMined240() {
            return mined240;
        }

        public void setMined240(int mined240) {
            this.mined240 = mined240;
        }

        public int getMined300() {
            return mined300;
        }

        public void setMined300(int mined300) {
            this.mined300 = mined300;
        }

        public int getMined360() {
            return mined360;
        }

        public void setMined360(int mined360) {
            this.mined360 = mined360;
        }

        public int getMined420() {
            return mined420;
        }

        public void setMined420(int mined420) {
            this.mined420 = mined420;
        }

        public int getMined480() {
            return mined480;
        }

            public void setMined480(int mined480) {
            this.mined480 = mined480;
        }

        public int getMined540() {
            return mined540;
        }

        public void setMined540(int mined540) {
            this.mined540 = mined540;
        }

        public int getMined600() {
            return mined600;
        }

        public void setMined600(int mined600) {
            this.mined600 = mined600;
        }

        public int getMined3600() {
            return mined3600;
        }

        public void setMined3600(int mined3600) {
            this.mined3600 = mined3600;
        }

        @Override
        public String toString() {
            return
                    blockNumber +
                            "," + timestamp +
                            "," + timeToMine +
                            "," + mined20 +
                            "," + mined40 +
                            "," + mined60 +
                            "," + mined120 +
                            "," + mined240 +
                            "," + mined300 +
                            "," + mined360 +
                            "," + mined420 +
                            "," + mined480 +
                            "," + mined540 +
                            "," + mined600 +
                            "," + mined3600
                     ;
        }
    }

    public static void main(String[] args) throws IOException {
        String pathToCsv = "blocks_10985851.csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        PrintWriter printWriter = new PrintWriter(new File("blockMineDuration.csv"));
        String row;
        List<BlockFileData> fileData = new  ArrayList<>();
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
//            System.out.println(Arrays.toString(data));

            String date = data[1].substring(0,23);
            String pattern = "MMM-dd-yyyy hh:mm:ss a";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime dateTime3 = LocalDateTime.parse(date, formatter);

            BlockFileData blockFileData = new BlockFileData(Integer.parseInt(data[0]),toEpochMilli(dateTime3),Integer.parseInt(data[2]));
            fileData.add(blockFileData);
        }
        Collections.sort(fileData,(Comparator.comparingLong(BlockFileData::getTimestamp)));
        for (int i = 0; i < fileData.size(); i++) {
            long timeDuration20  =  fileData.get(i).timestamp-20000;
            long timeDuration40  =  fileData.get(i).timestamp-40000;
            long timeDuration60  =  fileData.get(i).timestamp-60000;
            long timeDuration120 =  fileData.get(i).timestamp-120000;
            long timeDuration180 =  fileData.get(i).timestamp-180000;
            long timeDuration240 =  fileData.get(i).timestamp-240000;
            long timeDuration300 =  fileData.get(i).timestamp-300000;
            long timeDuration360 =  fileData.get(i).timestamp-360000;
            long timeDuration420 =  fileData.get(i).timestamp-420000;
            long timeDuration480 =  fileData.get(i).timestamp-480000;
            long timeDuration540 =  fileData.get(i).timestamp-540000;
            long timeDuration600 =  fileData.get(i).timestamp-600000;
            long timeDuration3600 =  fileData.get(i).timestamp-3600000;

            fileData.get(i).setMined20(getBlockCount(fileData,i,timeDuration20));
            fileData.get(i).setMined40(getBlockCount(fileData,i,timeDuration40));
            fileData.get(i).setMined60(getBlockCount(fileData,i,timeDuration60));
            fileData.get(i).setMined120(getBlockCount(fileData,i,timeDuration120));
            fileData.get(i).setMined180(getBlockCount(fileData,i,timeDuration180));
            fileData.get(i).setMined240(getBlockCount(fileData,i,timeDuration240));
            fileData.get(i).setMined300(getBlockCount(fileData,i,timeDuration300));
            fileData.get(i).setMined360(getBlockCount(fileData,i,timeDuration360));
            fileData.get(i).setMined420(getBlockCount(fileData,i,timeDuration420));
            fileData.get(i).setMined480(getBlockCount(fileData,i,timeDuration480));
            fileData.get(i).setMined540(getBlockCount(fileData,i,timeDuration540));
            fileData.get(i).setMined600(getBlockCount(fileData,i,timeDuration600));
            fileData.get(i).setMined3600(getBlockCount(fileData,i,timeDuration3600));
        }
        printWriter.println("blockNumber,timestamp,time2mine,last20,last40,last60,last120,last240,last300,last360,last420,last480,last540,last600,last3600");
        for (int i = 0; i < fileData.size(); i++) {
            printWriter.println(fileData.get(i).toString());
        }
        printWriter.flush();
        System.out.println(fileData);
    }

    private static int getBlockCount (List<BlockFileData> fileData,int i,long timeDuration) {
        int ind = Collections.binarySearch(fileData,new BlockFileData(0,timeDuration,0),Comparator.comparingLong(BlockFileData::getTimestamp));
        int blockCount = 0;
        if (ind >= 0) {
            blockCount = i - ind  +1;
        } else {
            blockCount = i - (ind*-1)  +2;
        }
        return blockCount;
    }
    private static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
    }

}

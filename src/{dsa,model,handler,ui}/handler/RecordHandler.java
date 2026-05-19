package handler;

import model.GameRecord;
import dsa.LinkedList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class RecordHandler {

    private LinkedList<GameRecord> records;
    private static final String RECORDS_FILE = "minesweeper_records.txt";

    public RecordHandler() {
        this.records = new LinkedList<>();
        loadRecords();
    }

    public void addRecord(GameRecord record) {
        records.insertLast(record);
        saveRecords();
    }

    public LinkedList<GameRecord> getAllRecords() {
        return records;
    }

    public LinkedList<GameRecord> getSortedByScore() {
        LinkedList<GameRecord> sorted = new LinkedList<>();
        for (int i = 0; i < records.size(); i++) {
            sorted.insertLast(records.get(i));
        }
        sorted.insertionSort((a, b) -> b.getScore() - a.getScore());
        return sorted;
    }

    public LinkedList<GameRecord> getSortedByTime() {
        LinkedList<GameRecord> sorted = new LinkedList<>();
        for (int i = 0; i < records.size(); i++) {
            sorted.insertLast(records.get(i));
        }
        sorted.insertionSort((a, b) -> Long.compare(a.getTimeTaken(), b.getTimeTaken()));
        return sorted;
    }

    public LinkedList<GameRecord> getWinRecords() {
        LinkedList<GameRecord> wins = new LinkedList<>();
        for (int i = 0; i < records.size(); i++) {
            GameRecord record = records.get(i);
            if (record.isWon()) {
                wins.insertLast(record);
            }
        }
        return wins;
    }

    public int getTotalGames() {
        return records.size();
    }

    public int getTotalWins() {
        int count = 0;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).isWon()) {
                count++;
            }
        }
        return count;
    }

    public double getWinRate() {
        if (records.isEmpty()) {
            return 0.0;
        }
        return (double) getTotalWins() / getTotalGames() * 100;
    }

    private void saveRecords() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE));
            for (int i = 0; i < records.size(); i++) {
                writer.write(records.get(i).toSaveString());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecords() {
        File file = new File(RECORDS_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    GameRecord record = GameRecord.fromSaveString(line.trim());
                    if (record != null) {
                        records.insertLast(record);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearRecords() {
        records.clear();
        saveRecords();
    }
}

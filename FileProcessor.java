import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileProcessor implements Runnable {
    private Path filePath;
    private Set<String> uniqueWords;
    private List<String> words;

    public FileProcessor(Path filePath, Set<String> uniqueWords, List<String> words) {
        this.filePath = filePath;
        this.uniqueWords = uniqueWords;
        this.words = words;
    }

    @Override
    public void run() {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] wordArray = line.split("\\s+");
                synchronized (uniqueWords) {
                    Collections.addAll(uniqueWords, wordArray);
                }
                synchronized (words) {
                    Collections.addAll(words, wordArray);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

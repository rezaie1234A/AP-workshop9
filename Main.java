import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        List<Path> filePaths = new ArrayList<>();
        File file = new File(("src/files11"));
        File[] files = file.listFiles();
        for (File file1 : files) {
            filePaths.add(file1.toPath());
        }

        Set<String> uniqueWords = Collections.synchronizedSet(new HashSet<>());
        List<String> words = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (Path filePath : filePaths) {
            FileProcessor fileProcessor = new FileProcessor(filePath, uniqueWords, words);
            executorService.execute(fileProcessor);
        }
        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processResults(uniqueWords, words);
    }

    private static void processResults(Set<String> uniqueWords, List<String> words) {
        int totalUniqueWords = uniqueWords.size();
        String longestWord = "";
        for (String word : words) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }
        String shortestWord = longestWord;
        for (String word : words) {
            if (word.length() < shortestWord.length()) {
                shortestWord = word;
            }
        }
        int totalLength = 0;
        for (String word : words) {
            totalLength += word.length();
        }
        double averageLength = totalLength / (double) words.size();
        System.out.println("Total unique words: " + totalUniqueWords);
        System.out.println("Longest word: " + longestWord + " (Length: " + longestWord.length() + ")");
        System.out.println("Shortest word: " + shortestWord + " (Length: " + shortestWord.length() + ")");
        System.out.println("Average word length: " + averageLength);
    }
}

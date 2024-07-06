import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final String CHARS = "abc";
    private static final int TEXTS = 10_000;
    private static final int LENGTH = 100_000;
    private static final char A = 'a';
    private static final char B = 'b';
    private static final char C = 'c';

    static String maxTextA = null;
    static String maxTextB = null;
    static String maxTextC = null;

    static int maxChA = 0;
    static int maxChB = 0;
    static int maxChC = 0;

    static BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < TEXTS; i++) {
                String text = generateText(CHARS, LENGTH);
                aQueue.add(text);
                bQueue.add(text);
                cQueue.add(text);
            }
        }).start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int howMany(String text, char ch) {
        return (int) text.chars()
                .filter(c -> c == ch)
                .count();
    }

    public static void checkThread(char ch, ArrayBlockingQueue chQueue) {

        new Thread(() -> {
            while (!chQueue.isEmpty()) {
                String text = null;
                try {
                    text = chQueue.take().toString();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int tmpMax = howMany(text, ch);
                if (tmpMax > maxChA) {
                    maxChA = tmpMax;
                    maxTextA = text;
                }
            }
        }).start();
    }
}

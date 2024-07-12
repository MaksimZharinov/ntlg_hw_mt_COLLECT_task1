import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final String CHARS = "abc";
    private static final int TEXTS = 10_000;    //10_000
    private static final int LENGTH = 100_000;  //100_000
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
                try {
                    aQueue.put(text);
                    bQueue.put(text);
                    cQueue.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        checkThread(A, aQueue);
        checkThread(B, bQueue);
        checkThread(C, cQueue);


        //printResult();
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

    public static void checkThread(char ch, BlockingQueue<String> chQueue) {

        Thread thread = new Thread(() -> {
            String text = null;
            try {
                text = chQueue.take();
            } catch (InterruptedException e) {
                return;
            }
            int tmpMax = howMany(text, ch);
            if (tmpMax > maxChA) {
                maxChA = tmpMax;
                maxTextA = text;
            }
        });
        thread.start();
    }

    public static void printResult() {
        System.out.println("The maximum number of 'a' characters is: " +
                maxChA +
                "\nCharacters are contained in the text: " +
                maxTextA);
        System.out.println("The maximum number of 'b' characters is: " +
                maxChB +
                "\nCharacters are contained in the text: " +
                maxTextB);
        System.out.println("The maximum number of 'c' characters is: " +
                maxChC +
                "\nCharacters are contained in the text: " +
                maxTextC);
    }
}

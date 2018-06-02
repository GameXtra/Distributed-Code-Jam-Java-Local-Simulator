import java.util.Random;

public class baby_blocks {
    public baby_blocks() {
    }

    private static long[] nn = new long[10_000_001];

    public static long GetNumberOfBlocks() {
        return 10_000_001L;
    }

    public static void set() {
        Random rand = new Random();
        for (int i = 0; i < 10_000_001; i++) {
            nn[i] = 1 + rand.nextInt(10000000);
        }
    }

    public static long GetBlockWeight(long i) {
        return 1_000_000_000;
    }
}
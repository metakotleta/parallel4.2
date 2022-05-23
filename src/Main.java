import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static final int PROCS = 4;
    public static final long MAIN_SLEEP = 1000;
    public static Random random = new Random();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Thread.sleep(10000);
        int[] array = generateArray(50000, 100000);
        ConcurrentHashMap<Integer, Integer> realFast = new ConcurrentHashMap<>();
        Map<Integer, Integer> notSoFast = Collections.synchronizedMap(new HashMap<>());
        ExecutorService executorFast = Executors.newFixedThreadPool(PROCS);
        ExecutorService executorSlow = Executors.newFixedThreadPool(PROCS);
        List<Future<Result>> futureList = new ArrayList<>();

        ReaderWriter rw = new ReaderWriter(array, PROCS);

        startAndPrintResult(executorFast, rw, realFast, futureList, true);
        startAndPrintResult(executorFast, rw, realFast, futureList, false);
        startAndPrintResult(executorSlow, rw, notSoFast, futureList, true);
        startAndPrintResult(executorSlow, rw, notSoFast, futureList, false);
        executorFast.shutdown();
        executorSlow.shutdown();
    }

    public static int[] generateArray(int dimension, int maxValue) {
        int[] array = new int[dimension];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(maxValue);
        }
        return array;
    }

    public static void startAndPrintResult(ExecutorService executor, ReaderWriter rw,
                                                Map<Integer, Integer> map,  List<Future<Result>> futureList, boolean mode) throws ExecutionException, InterruptedException {
        long max = 0;
        long min = Long.MAX_VALUE;
        rw.setMap(map);
        for (int i = 0; i < PROCS; i++) {
            int mod = i;
            if (mode) {
                futureList.add(executor.submit(() -> rw.write(mod)));
            } else {
                futureList.add(executor.submit(() -> rw.read(mod)));
            }
        }
        for (int i = 0; i < futureList.size(); i++) {
            Result temp = futureList.get(i).get();
            min = Math.min(min, temp.start);
            max = Math.max(max, temp.end);
        }
        if (mode) {
            System.out.printf("Время выполнения записи для %s: %d мс\n", map.getClass().getSimpleName(), max - min);
        } else {
            System.out.printf("Время выполнения чтения для %s: %d мс\n", map.getClass().getSimpleName(), max - min);
            map.clear();
        }
        futureList.clear();
    }

}

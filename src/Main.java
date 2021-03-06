import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static final int DIM = 100000000;
    public static final int MAX_VALUE = 1000000;
    public static final int PROCS = 8;
    public static Random random = new Random();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Thread.sleep(5000);
        int[] array = generateArray(DIM, MAX_VALUE);
        ConcurrentHashMap<Integer, Integer> realFast = new ConcurrentHashMap<>();
        Map<Integer, Integer> notSoFast = Collections.synchronizedMap(new HashMap<>());

        ExecutorService executorFast = Executors.newFixedThreadPool(PROCS);
        ExecutorService executorSlow = Executors.newFixedThreadPool(PROCS);

        List<Future<Result>> futureList = new ArrayList<>();

        ReaderWriter rw = new ReaderWriter(array, PROCS, realFast);
        startAndPrintResult(executorFast, rw, futureList, true);
        startAndPrintResult(executorFast, rw, futureList, false);
        executorFast.shutdown();

        rw = new ReaderWriter(array, PROCS, notSoFast);
        startAndPrintResult(executorSlow, rw, futureList, true);
        startAndPrintResult(executorSlow, rw, futureList, false);
        executorSlow.shutdown();
    }

    public static int[] generateArray(int dimension, int maxValue) {
        int[] array = new int[dimension];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(maxValue);
        }
        return array;
    }

    public static void startAndPrintResult(ExecutorService executor, ReaderWriter rw,  List<Future<Result>> futureList,
                                           boolean mode) throws ExecutionException, InterruptedException {
        long max = 0;
        long min = Long.MAX_VALUE;
        for (int i = 0; i < PROCS; i++) {
            int mod = i;
            if (mode) {
                futureList.add(executor.submit(() -> rw.write(mod)));
            } else {
                futureList.add(executor.submit(() -> rw.read(mod)));
            }
        }
        for (Future<Result> resultFuture : futureList) {
            Result temp = resultFuture.get();
            min = Math.min(min, temp.start);
            max = Math.max(max, temp.end);
        }
        if (mode) {
            System.out.printf("?????????? ???????????????????? ???????????? ?????? %s: %d ????\n", rw.getMap().getClass().getSimpleName(), max - min);
        } else {
            System.out.printf("?????????? ???????????????????? ???????????? ?????? %s: %d ????\n", rw.getMap().getClass().getSimpleName(), max - min);
            rw.getMap().clear();
        }
        futureList.clear();
    }

}

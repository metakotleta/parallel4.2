import java.util.Map;

public class ReaderWriter {
    private int[] array;
    private Map<Integer, Integer> map;
    private int parts;

    public ReaderWriter(int[] array, int parts, Map<Integer, Integer> map) {
        this.array = array;
        this.parts = parts;
        this.map = map;
    }

    public Result write(int mod) {
        long start = System.currentTimeMillis();
        for (int i = mod; i < array.length; i += parts) {
            map.put(i, array[i]);
        }
        long end = System.currentTimeMillis();
        System.out.printf("%s: Запись завершена.\n", Thread.currentThread().getName());
        return new Result(start, end);
    }

    public Result read(int mod) {
        long start = System.currentTimeMillis();
        for (int i = mod; i < map.size(); i += parts) {
            map.get(i);
        }
        long end = System.currentTimeMillis();
        System.out.printf("%s: Чтение завершено.\n", Thread.currentThread().getName());
        return new Result(start, end);
    }
}

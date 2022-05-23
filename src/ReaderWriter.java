import java.util.Map;

public class ReaderWriter {
    private int[] array;
    private Map<Integer, Integer> map;
    private int parts;

    public ReaderWriter(int[] array, int parts) {
        this.array = array;
        this.parts = parts;
    }

    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }

    public Result write(int mod) {
        int count = 0;
        long start = System.currentTimeMillis();
        for (int i = mod; i < array.length; i += parts) {
            count++;
            map.put(i, array[i]);
        }
        long end = System.currentTimeMillis();
        System.out.printf("%s: Запись завершена, элеметов добавлено: %d.\n", Thread.currentThread().getName(), count);
        return new Result(start, end);
    }

    public Result read(int mod) {
        int count = 0;
        long start = System.currentTimeMillis();
        for (int i = mod; i < map.size(); i += parts) {
            count++;
            map.get(i);
        }
        long end = System.currentTimeMillis();
        System.out.printf("%s: Чтение завершено, %d элементов прочитано.\n", Thread.currentThread().getName(), count);
        return new Result(start, end);
    }
}

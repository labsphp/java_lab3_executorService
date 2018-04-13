package lab3.executorService.findMin;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
    private static AtomicInteger minIndex = new AtomicInteger();

    private static void findMin(int arr[], int start, int end) {
        for (int i = start; i < end; i++) {
            int oldValue, newValue, oldIndex, newIndex;
            do {
                if (arr[i] < min.get()) {
                    do {
                        oldValue = min.get();
                        newValue = arr[i];
                    } while (!min.compareAndSet(oldValue, newValue));
                    oldIndex = minIndex.get();
                    newIndex = i;
                } else {
                    break;
                }
            } while (!minIndex.compareAndSet(oldIndex, newIndex));
        }
    }

    public static void main(String[] argc) throws Exception {
        int countElem = 20;
        int[] arr = new int[countElem];
        Random random = new Random();
        for (int i = 0; i < countElem; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<?> future1 = executor.submit(() -> findMin(arr, 0, countElem / 2));
        Future<?> future2 = executor.submit(() -> findMin(arr, countElem / 2, arr.length));

        executor.awaitTermination(300, TimeUnit.MILLISECONDS);
        executor.shutdown();

        System.out.println("Min значение: " + min);
        System.out.println("Индекс min значениея: " + minIndex);
    }
}

package lab3.executorService.findMax;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static AtomicInteger max = new AtomicInteger(Integer.MIN_VALUE);
    private static AtomicInteger maxIndex = new AtomicInteger();

    private static void findMax(int arr[], int start, int end) {
        for (int i = start; i < end; i++) {
            int oldValue, newValue, oldIndex, newIndex;
            do {
                if (arr[i] > max.get()) {
                    do {
                        oldValue = max.get();
                        newValue = arr[i];
                    } while (!max.compareAndSet(oldValue, newValue));
                    oldIndex = maxIndex.get();
                    newIndex = i;
                } else {
                    break;
                }
            } while (!maxIndex.compareAndSet(oldIndex, newIndex));
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
        Future<?> future1 = executor.submit(() -> findMax(arr, 0, countElem / 2));
        Future<?> future2 = executor.submit(() -> findMax(arr, countElem / 2, arr.length));

        executor.awaitTermination(300, TimeUnit.MILLISECONDS);
        executor.shutdown();

        System.out.println("Max значение: " + max);
        System.out.println("Индекс max значениея: " + maxIndex);
    }
}

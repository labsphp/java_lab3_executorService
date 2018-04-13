package lab3.executorService.find_values;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger count = new AtomicInteger(0);
    private static int condition = 40;

    //Нахождение кол-ва элементов по условию: элементы > 40
    static void filter(int arr[], int start, int end) {
        for (int i = start; i < end; i++) {
            int oldValue, newValue;
            do {
                if (arr[i] > condition) {
                    oldValue = count.get();
                    newValue = oldValue + 1;
                } else {
                    break;
                }
            } while (!count.compareAndSet(oldValue, newValue));
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
        Future<?> future1 = executor.submit(() -> filter(arr, 0, countElem / 2));
        Future<?> future2 = executor.submit(() -> filter(arr, countElem / 2, arr.length));

        executor.awaitTermination(300, TimeUnit.MILLISECONDS);
        executor.shutdown();
        System.out.println("Колличество элементов, которые > 40: " + count.get());
    }
}

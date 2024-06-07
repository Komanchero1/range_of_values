import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        //объявляем переменную типа ExecutorService для получения доступа
        // к методам интерфейса ExecutorService позволяющие управлять и
        // выполнять задачи в многопоточности
        //вызов метода newFixedThreadPoo создает и возвращает новый экземпляр
        // ExecutorService  с фиксированным пулом потоков.
        ExecutorService threadPool = Executors.newFixedThreadPool(25);
        long startTs = System.currentTimeMillis(); // start time
        //создаем список для заполнения его  объектами Future в дальнейшем используем
        // эти объекты для получения  результатов их работы
        List<Future<Integer>> futures = new ArrayList<>();
        for (String text : texts) {
            //методу  submit передаем в качестве параметра метод  getMaxSubstringWithoutBTask
            // который содержит логику проверки длины строки  , затем метод add
            // добавляет результат выполнения в коллекцию futures для параллельного выполнения 25 задач
            //
            futures.add(threadPool.submit((getMaxSubstringWithoutBTask(text))));
        }
        int totalMaxSize = 0;
        for (Future<Integer> future : futures) {
            totalMaxSize += future.get();
        }
        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");
        System.out.println("Total max size: " + totalMaxSize);
        //останавливаем работу threadPool дождавшись завершения всех запущенных задач
        threadPool.shutdown();
    }

    //выносим логику определения длины строки в отдельный метод
    public static Callable<Integer> getMaxSubstringWithoutBTask(String text) {
        return () -> {
            int maxSize = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {
                        continue;
                    }
                    boolean bFound = false;
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
            return maxSize;
        };
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
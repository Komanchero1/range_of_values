import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];//создаем массив типа String на 25 ячеек для хранения созданных строк
        for (int i = 0; i < texts.length; i++) {//заполняем массив строками
            texts[i] = generateText("aab", 30_000);//генерируем рандомно строку
        }
        //создаем список для хранения потоков
        List<Thread> threads = new ArrayList<>();

        long startTs = System.currentTimeMillis(); // start time
        //переберем массив строк созданных рандомно
        for (String text : texts) {
            //создаем экземпляр класса Thread передаем в него метод getThread(text)
            // со строкой , метод создает поток с задачей
            Thread thread = getThread(text);
            threads.add(thread); // Добавляем поток в список
            thread.start(); // Запускаем поток
        }


        for (Thread thread : threads) {//перебираем потоки
            thread.join(); // Ожидаем завершения всех потоков

        }


        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");//выводим в консоль время работы всех потоков
    }

    //выносим логику проверки строки на наличие символов "а" в отдельный метод
    private static Thread getThread(String text) {
        Runnable task = () -> { //реализуем метод интерфейса  Runnable
            int maxSize = 0;//инициализируем переменную для хранения максимальной длины подстроки без символа "b"
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {//если условие соблюдается пропускаем итерацию
                        continue;
                    }
                    boolean bFound = false;//инициализируем переменную для отслеживания символа "b"
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {//если встречается символ "b"
                            bFound = true;//переменной присваивается true
                            break;//цикл прерывается
                        }
                    }
                    //если bFound равен false и длина подстроки больше чем текущее
                    // значение maxSize, то maxSize обновляется
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            //выводится в консоль начало строки до 100 символов
            // и максимальная длина подстроки без символа "b"
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
        };


        Thread thread = new Thread(task); // Создаем поток с задачей
        return thread;
    }

    //метод для рандомно генерируемой строки с заданными параметрами
    public static String generateText(String letters, int length) {
        Random random = new Random();//создаем объект класса  Random для использования метода random
        StringBuilder text = new StringBuilder(); // создаем объект StringBuilder для формирования строки
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));//формируем строку
        }
        return text.toString();//возвращаем строку
    }
}
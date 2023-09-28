import com.packag.UserOnline;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    static final int startYear = 2020;
    static final int endYear = 2022;

    public static void main(String[] args) {
        Main main = new Main();
//        System.out.println(main.findMaxOnline());
//        System.out.println(main.findMaxOnlineDateSimple());
        System.out.println(main.findMaxOnlineDateHard());
    }

    public static void updateValue(Map<LocalDate, Integer> map, LocalDate key, Integer value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, value);
        }
    }

    public static HashMap<LocalDate, Integer> fillHashMap(List<UserOnline> onlineList) {
        HashMap<LocalDate, Integer> staticHashMap = new HashMap<>();
        for (UserOnline list : onlineList) {
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = ChronoUnit.DAYS.addTo(i, 1)) {
                updateValue(staticHashMap, i, 1);
            }
        }
        return staticHashMap;
    }

    public static SortedMap<LocalDate, Integer> fillValuesHard(List<UserOnline> onlineList, LocalDate startDate, long days) {
        SortedMap<LocalDate, Integer> staticSortedMap = fiilByZerOMap(startDate, days);
        for (UserOnline list : onlineList) {
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = ChronoUnit.DAYS.addTo(i, 1)) {
                updateValue(staticSortedMap, i, 1);
            }
        }
        return staticSortedMap;
    }

    public static TreeMap<LocalDate, Integer> fiilByZerOMap(LocalDate start, long days) {
        TreeMap<LocalDate, Integer> staticTreeMap = new TreeMap<>();
        long i = 0;
        while (i < days) {
            staticTreeMap.put(ChronoUnit.DAYS.addTo(start, 1), 0);
            i++;
        }
        return staticTreeMap;
    }

    public int findMaxOnline() {
        // Найти самое большое количество онлайна одновременно
        Random random = new Random();
        int i = random.nextInt(1000, 1200);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i)
                .sorted((o1, o2) -> Math.toIntExact(ChronoUnit.DAYS.between(o2.getStartSession(), o1.getStartSession())))
                .toList();
        HashMap<LocalDate, Integer> staticHashMap = fillHashMap(onlineList);
        Integer maxUsers = Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                        .get()).findFirst()
                .get();
        System.out.println(onlineList);
        System.out.println(staticHashMap);
        System.out.println(maxUsers);
        return maxUsers;
    }

    public LocalDate findMaxOnlineDateSimple() {

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)

        Random random = new Random();
        int i = random.nextInt(1000, 1200);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i)
                .sorted((o1, o2) -> Math.toIntExact(ChronoUnit.DAYS.between(o2.getStartSession(), o1.getStartSession())))
                .toList();

        HashMap<LocalDate, Integer> staticHashMap = fillHashMap(onlineList);
        System.out.println(staticHashMap);
        LocalDate finalDateMaxUser = Stream.of(staticHashMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(Stream.of(staticHashMap.values())
                        .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                                .get()).findFirst()
                        .get()))
                .map(Map.Entry::getKey).min((d1, d2) -> Math.toIntExact(ChronoUnit.DAYS.between(d2, d1))).get();
        System.out.println(onlineList);
        System.out.println(staticHashMap);
        System.out.println(finalDateMaxUser);
        return finalDateMaxUser;
    }

    public List<LocalDate> findMaxOnlineDateHard() {

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)

        Random random = new Random();
        int i = random.nextInt(10, 15);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i).toList();
        List<UserOnline> onlineListDesc = onlineList
                .stream()
                .sorted((o1, o2) -> Math.toIntExact(ChronoUnit.DAYS.between(o2.getStartSession(), o1.getStartSession())))
                .toList();
        List<UserOnline> onlineListAsc = onlineList
                .stream()
                .sorted((o1, o2) -> Math.toIntExact(ChronoUnit.DAYS.between(o2.getEndSession(), o1.getEndSession())))
                .toList();
        System.out.println(onlineList);
        System.out.println(onlineListAsc);
        System.out.println(onlineListDesc);
        LocalDate startDate = onlineListDesc.get(0).getStartSession();
        LocalDate endDate = onlineListAsc.get(onlineListAsc.size() - 1).getEndSession();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        SortedMap<LocalDate, Integer> staticSortedMap = fillValuesHard(onlineListDesc, startDate, days);


        System.out.println(staticSortedMap);

        Integer maxUsers = Stream.of(staticSortedMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                        .get()).findFirst()
                .get();
        System.out.println(maxUsers);
        List<LocalDate> finalDateMaxUser = Stream.of(staticSortedMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxUsers)).map(Map.Entry::getKey).sorted((d1, d2) -> Math.toIntExact(ChronoUnit.DAYS.between(d2, d1))).toList();
        return finalDateMaxUser;
    }

    public static UserOnline generateUser() {
        Random random = new Random();

        int randomYearStart = random.nextInt(startYear, endYear);
        int randomMonthStart = random.nextInt(1, 12);
        int randomDayStart = random.nextInt(1, Month.of(randomMonthStart).maxLength());
        int randomYearEnd = random.nextInt(startYear, endYear);
        int randomMonthEnd = random.nextInt(1, 12);
        int randomDayEnd = random.nextInt(1, Month.of(randomMonthEnd).maxLength());

        LocalDate startUserOnline = LocalDate.of(randomYearStart, randomMonthStart, randomDayStart);
        LocalDate endUserOnline = LocalDate.of(randomYearEnd, randomMonthEnd, randomDayEnd);

        if (startUserOnline.isAfter(endUserOnline)) {
            LocalDate temp = startUserOnline;
            startUserOnline = endUserOnline;
            endUserOnline = temp;
        }

        return new UserOnline(startUserOnline, endUserOnline);
    }
}
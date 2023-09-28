import com.packag.UserOnline;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static final int startYear = 2020;
    static final int endYear = 2022;

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(main.findMaxOnline());
        System.out.println(main.findMaxOnlineDateSimple());

    }

    public static void updateValue(Map<LocalDate, Integer> map, LocalDate key, Integer value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, value);
        }
    }
    public static HashMap<LocalDate, Integer> fillHashMap (List<UserOnline> onlineListDesc) {
        HashMap<LocalDate, Integer> staticHashMap = new HashMap<>();
        for (UserOnline list : onlineListDesc) {
            for (LocalDate j = list.getStartSession(); j.isBefore(list.getEndSession()); j = ChronoUnit.DAYS.addTo(j, 1)) {
                updateValue(staticHashMap, j, 1);
            }
        }
        return staticHashMap;
    }

    public int findMaxOnline() {
        // Найти самое большое количество онлайна одновременно
        Random random = new Random();
        int i = random.nextInt(20, 30);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i).toList();
        List<UserOnline> onlineListDesc = onlineList
                .stream()
                .sorted((o1, o2) -> Math.toIntExact(ChronoUnit.DAYS.between(o2.getStartSession(), o1.getStartSession())))
                .toList();
        System.out.println(onlineList);
        System.out.println(onlineListDesc);

        HashMap<LocalDate, Integer> staticHashMap = fillHashMap(onlineListDesc);

        System.out.println(staticHashMap);
        Integer maxUsers = Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                .get()).findFirst()
                .get();
        LocalDate finalDateMaxUser = Stream.of(staticHashMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxUsers))
                .map(Map.Entry::getKey).min((d1, d2) -> Math.toIntExact(ChronoUnit.DAYS.between(d2, d1))).get();
        System.out.println(finalDateMaxUser);
        return maxUsers;
    }

    public LocalDate findMaxOnlineDateSimple() {

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)

        Random random = new Random();
        int i = random.nextInt(20, 30);
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
        System.out.println(onlineListDesc);
        LocalDate startDate = onlineListDesc.get(0).getStartSession();
        LocalDate endDate = onlineListAsc.get(onlineListAsc.size() - 1).getEndSession();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        HashMap<LocalDate, Integer> staticHashMap = new HashMap<>();
        for (UserOnline list : onlineListDesc) {
            for (LocalDate j = list.getStartSession(); j.isBefore(list.getEndSession()); j = ChronoUnit.DAYS.addTo(j, 1)) {
                updateValue(staticHashMap, j, 1);
            }
        }
        System.out.println(staticHashMap);
        Integer maxUsers = Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                .get()).findFirst()
                .get();
        System.out.println(maxUsers);
        LocalDate finalDateMaxUser = Stream.of(staticHashMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxUsers))
                .map(Map.Entry::getKey).min((d1, d2) -> Math.toIntExact(ChronoUnit.DAYS.between(d2, d1))).get();
        return finalDateMaxUser;
    }

    public LocalDate findMaxOnlineDateHard() {

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)

        Random random = new Random();
        int i = random.nextInt(20, 30);
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
        System.out.println(onlineListDesc);
        LocalDate startDate = onlineListDesc.get(0).getStartSession();
        LocalDate endDate = onlineListAsc.get(onlineListAsc.size() - 1).getEndSession();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        HashMap<LocalDate, Integer> staticHashMap = new HashMap<>();
        for (UserOnline list : onlineListDesc) {
            for (LocalDate j = list.getStartSession(); j.isBefore(list.getEndSession()); j = ChronoUnit.DAYS.addTo(j, 1)) {
                updateValue(staticHashMap, j, 1);
            }
        }
        System.out.println(staticHashMap);
        Integer maxUsers = Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                .get()).findFirst()
                .get();
        System.out.println(maxUsers);
        LocalDate finalDateMaxUser = Stream.of(staticHashMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxUsers))
                .map(Map.Entry::getKey).min((d1, d2) -> Math.toIntExact(ChronoUnit.DAYS.between(d2, d1))).get();
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
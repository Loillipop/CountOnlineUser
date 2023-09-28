import com.packag.UserOnline;

import java.text.CollationElementIterator;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static final int startYear = 2020;
    static final int endYear = 2022;

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(main.findMaxOnline());
        System.out.println(main.findMaxOnlineDateSimple());
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
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = addOneDayToLocaDate(i)) {
                updateValue(staticHashMap, i, 1);
            }
        }
        return staticHashMap;
    }

    public static SortedMap<LocalDate, Integer> fillValuesHard(List<UserOnline> onlineList, LocalDate startDate, int days) {
        SortedMap<LocalDate, Integer> staticSortedMap = fiilByZerOMap(startDate, days);
        for (UserOnline list : onlineList) {
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = addOneDayToLocaDate(i)) {
                updateValue(staticSortedMap, i, 1);
            }
        }
        return staticSortedMap;
    }

    public static int countDayInterval(LocalDate date1, LocalDate date2) {
        return Math.toIntExact(ChronoUnit.DAYS.between(date1, date2));
    }

    public static LocalDate addOneDayToLocaDate(LocalDate date) {
        return date.plusDays(1);
    }

    public static TreeMap<LocalDate, Integer> fiilByZerOMap(LocalDate start, int days) {
        TreeMap<LocalDate, Integer> staticTreeMap = new TreeMap<>();
        long i = 0;
        while (i < days) {
            staticTreeMap.put(addOneDayToLocaDate(start), 0);
            i++;
        }
        return staticTreeMap;
    }

    public static String splitListtoMaxUsers(List<LocalDate> finalDateMaxUser, List<UserOnline> onlineListDesc, Integer maxUsers) {
        if (maxUsers == 1 || finalDateMaxUser.size() == 1) {
            return "Количество пользователей всегда:" + maxUsers + ". Пересечений нет";
        } else if (finalDateMaxUser.size() == 2) {
            if (finalDateMaxUser.get(1).isEqual(addOneDayToLocaDate(finalDateMaxUser.get(0)))) {
                return finalDateMaxUser.get(0) + " - " + finalDateMaxUser.get(1) + " --> " + 2;
            } else return finalDateMaxUser.get(0) + " --> " + 2;
        } else {
            LinkedList<LocalDate> finList = new LinkedList<>();
            LinkedList<LocalDate> tempList = new LinkedList<>();
            for (int j = 1; j < finalDateMaxUser.size() - 1; j++) {
                if (finalDateMaxUser.get(j).isEqual(addOneDayToLocaDate(finalDateMaxUser.get(j - 1)))) {
                    if (j == 1) {
                        tempList.add(finalDateMaxUser.get(j - 1));
                    }
                    tempList.add(finalDateMaxUser.get(j));
                } else {
                    finList.clear();
                    tempList.addAll(finList);
                    tempList.clear();
                }
            }
            finList.addAll(tempList);
            return finList.get(0) + " - " + finList.get(finList.size() - 1) + "--> " + maxUsers;
        }
    }

    public int findMaxOnline() {
        // Найти самое большое количество онлайна одновременно
        Random random = new Random();
        int i = random.nextInt(1000, 1200);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i)
                .sorted((o1, o2) -> countDayInterval(o2.getStartSession(), o1.getStartSession()))
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
                .sorted((o1, o2) -> countDayInterval(o2.getStartSession(), o1.getStartSession()))
                .toList();

        HashMap<LocalDate, Integer> staticHashMap = fillHashMap(onlineList);
        System.out.println(staticHashMap);
        LocalDate finalDateMaxUser = Stream.of(staticHashMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(Stream.of(staticHashMap.values())
                        .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                                .get()).findFirst()
                        .get()))
                .map(Map.Entry::getKey).min((d1, d2) -> countDayInterval(d2, d1))
                .get();
        System.out.println(onlineList);
        System.out.println(staticHashMap);
        System.out.println(finalDateMaxUser);
        return finalDateMaxUser;
    }

    public String findMaxOnlineDateHard() {

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)

        Random random = new Random();
        int i = random.nextInt(2, 3);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i).toList();
        List<UserOnline> onlineListDesc = onlineList
                .stream()
                .sorted((o1, o2) -> countDayInterval(o2.getStartSession(), o1.getStartSession()))
                .toList();
        List<UserOnline> onlineListAsc = onlineList
                .stream()
                .sorted((o1, o2) -> countDayInterval(o2.getEndSession(), o1.getEndSession()))
                .toList();
        LocalDate startDate = onlineListDesc.get(0).getStartSession();
        LocalDate endDate = onlineListAsc.get(onlineListAsc.size() - 1).getEndSession();
        int days = countDayInterval(startDate, endDate);
        SortedMap<LocalDate, Integer> staticSortedMap = fillValuesHard(onlineListDesc, startDate, days);
        Integer maxUsers = Stream.of(staticSortedMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                        .get()).findFirst()
                .get();
        System.out.println(maxUsers);
        List<LocalDate> finalDateMaxUser = Stream.of(staticSortedMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxUsers)).map(Map.Entry::getKey).sorted((d1, d2) -> countDayInterval(d2, d1)).toList();
        System.out.println(finalDateMaxUser);
        return splitListtoMaxUsers(finalDateMaxUser, onlineListDesc, maxUsers);
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
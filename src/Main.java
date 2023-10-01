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
        System.out.println("Максимальное количество задротов онлайн");
        System.out.println(main.findMaxOnline());
        System.out.println("Первая дата с самым большим количеством задротов онлайна");
        System.out.println(main.findMaxOnlineDateSimple());
        System.out.println("Период с самым большим количеством задротов онлайна");
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
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = i.plusDays(1)) {
                updateValue(staticHashMap, i, 1);
            }
        }
        return staticHashMap;
    }

    public static SortedMap<LocalDate, Integer> fillHashMap(List<UserOnline> onlineList, LocalDate startDate, int days) {
        SortedMap<LocalDate, Integer> statisticMap = fiilByZerOMap(startDate, days);
        for (UserOnline list : onlineList) {
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = i.plusDays(1)) {
                updateValue(statisticMap, i, 1);
            }
        }
        return statisticMap;
    }


    public static int countDayInterval(LocalDate date1, LocalDate date2) {
        return Math.toIntExact(ChronoUnit.DAYS.between(date1, date2));
    }

    public static SortedMap<LocalDate, Integer> fiilByZerOMap(LocalDate start, int days) {
        SortedMap<LocalDate, Integer> staticTreeMap = new TreeMap<>() {
        };
        long i = 0;
        while (i < days) {
            staticTreeMap.put(start.plusDays(1), 0);
            i++;
        }
        return staticTreeMap;
    }

    public static List<LocalDate> countMaxPeriod(List<LocalDate> finalDateMaxUser) {
        List<LocalDate> tempList = new ArrayList<>();
        List<LocalDate> finallist = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < finalDateMaxUser.size() - 1; i++) {
            if (finalDateMaxUser.get(i).plusDays(1).isEqual(finalDateMaxUser.get(++i))) {
                if (count == 0) {
                    tempList.add(finalDateMaxUser.get(i - 1));
                    tempList.add(finalDateMaxUser.get(i));
                    count += 2;
                    i--;
                } else {
                    tempList.add(finalDateMaxUser.get(i));
                    count += 1;
                    i--;
                }
            } else {
                if (tempList.isEmpty()) {
                    count = 0;
                    --i;
                } else {
                    if (tempList.size() < finalDateMaxUser.size() - tempList.size()) {
                        if (finallist.isEmpty()) {
                            finallist.addAll(tempList);
                            tempList.clear();
                            count = 0;
                            --i;
                        } else if (finallist.size() < tempList.size()) {
                            finallist.clear();
                            finallist.addAll(tempList);
                            tempList.clear();
                            count = 0;
                            --i;
                        } else if (finallist.size() == tempList.size()) {
                            tempList.clear();
                            count = 0;
                            --i;
                        } else {
                            tempList.clear();
                            count = 0;
                            --i;
                        }
                    } else if (tempList.size() >= finalDateMaxUser.size() - tempList.size()) {
                        break;
                    }
                }
            }
        }
        if (tempList.isEmpty() && finallist.isEmpty()) {
            tempList.add(finalDateMaxUser.get(0));
            return tempList;
        } else if (finallist.size() >= tempList.size()) {
            return finallist;
        } else return tempList;
    }

    public static String splitListtoMaxUsersPEriod(List<LocalDate> finalDateMaxUser, Integer maxUsers) {
        if (maxUsers == 1) {
            return finalDateMaxUser.get(0) + " - " + finalDateMaxUser.get(1) + " --> " + maxUsers;
        } else if (finalDateMaxUser.size() == 1) {
            return finalDateMaxUser.get(0) + " --> " + maxUsers;
        } else if (finalDateMaxUser.size() == 2) {
            if (finalDateMaxUser.get(0).plusDays(1).isEqual(finalDateMaxUser.get(1))) {
                return finalDateMaxUser.get(0) + " - " + finalDateMaxUser.get(1) + " --> " + maxUsers;
            } else return finalDateMaxUser.get(0) + " --> " + maxUsers;
        } else {
            List<LocalDate> finalList = countMaxPeriod(finalDateMaxUser);
            if (finalList.size() == 1) {
                return finalList.get(0) + "--> " + maxUsers;
            } else return finalList.get(0) + " - " + finalList.get(finalList.size() - 1) + "--> " + maxUsers;
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
        return Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                        .get()).findFirst()
                .get();
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
        LocalDate finalDateMaxUser = Stream.of(staticHashMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                .get()).findFirst()
                .get()))
                .map(Map.Entry::getKey).min((d1, d2) -> countDayInterval(d2, d1))
                .get();

        return finalDateMaxUser;
    }

    public String findMaxOnlineDateHard() {

        // Найти дату самого большого онлайна(первый день в диапазоне, когда было одновременно онлайн самое большое кол-во людей)
        // Более сложный вариант, это найти диапазон дат наибольшего онлайна
        // (то есть к примеру дата начала самого большого онлайна 05.05.2023, дата завершения 10.07.2023)

        Random random = new Random();
        int i = random.nextInt(1000, 2000);
        List<UserOnline> onlineList = Stream.generate(Main::generateUser)
                .limit(i)
                .sorted((o1, o2) -> countDayInterval(o2.getStartSession(), o1.getStartSession()))
                .toList();
        List<UserOnline> onlineListAsc = onlineList
                .stream()
                .sorted((o1, o2) -> countDayInterval(o2.getEndSession(), o1.getEndSession()))
                .toList();

        LocalDate startDate = onlineList.get(0).getStartSession();
        LocalDate endDate = onlineListAsc.get(onlineListAsc.size() - 1).getEndSession();
        int days = countDayInterval(startDate, endDate);

        SortedMap<LocalDate, Integer> sortedMap = fillHashMap(onlineList, startDate, days);
        Integer maxUsers = Stream.of(sortedMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                .get()).findFirst()
                .get();
        List<LocalDate> finalDateMaxUser = Stream.of(sortedMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxUsers))
                .map(Map.Entry::getKey)
                .toList();

        return splitListtoMaxUsersPEriod(finalDateMaxUser, maxUsers);
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
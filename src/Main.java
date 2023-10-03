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
        List<UserOnline> onlineList = main.getSortedOnlineList();
        System.out.println(onlineList);
        int maxOnline = main.findMaxOnline(onlineList);
        System.out.println("Максимальное количество задротов онлайн");
        System.out.println(maxOnline);
        System.out.println("Первая дата с самым большим количеством задротов онлайна");
        System.out.println(main.findMaxOnlineDateSimple(onlineList) + " -> " + maxOnline);
        System.out.println("Период с самым большим количеством задротов онлайна");
        System.out.println(main.findMaxOnlineDateHard(onlineList, maxOnline) + " -> " + maxOnline);
    }

    private List<UserOnline> getSortedOnlineList() {
        Random random = new Random();
        int i = random.nextInt(4, 10);

        return Stream.generate(Main::generateUser).
                limit(i).sorted((o1, o2) -> countDayInterval(o2.getStartSession(), o1.getStartSession()))
                .toList();
    }

    private void updateValue(Map<LocalDate, Integer> map, LocalDate key, Integer value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, value);
        }
    }

    private HashMap<LocalDate, Integer> fillHashMap(List<UserOnline> onlineList) {
        HashMap<LocalDate, Integer> staticHashMap = new HashMap<>();
        for (UserOnline list : onlineList) {
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = i.plusDays(1)) {
                updateValue(staticHashMap, i, 1);
            }
        }
        return staticHashMap;
    }

    private SortedMap<LocalDate, Integer> fillHashMap(List<UserOnline> onlineList, LocalDate startDate, int days) {
        SortedMap<LocalDate, Integer> statisticMap = fiilByZerOMap(startDate, days);
        for (UserOnline list : onlineList) {
            for (LocalDate i = list.getStartSession(); i.isBefore(list.getEndSession()); i = i.plusDays(1)) {
                updateValue(statisticMap, i, 1);
            }
        }
        return statisticMap;
    }

    private  int countDayInterval(LocalDate date1, LocalDate date2) {
        return Math.toIntExact(ChronoUnit.DAYS.between(date1, date2));
    }

    private SortedMap<LocalDate, Integer> fiilByZerOMap(LocalDate start, int days) {
        SortedMap<LocalDate, Integer> staticTreeMap = new TreeMap<>() {
        };
        long i = 0;
        while (i < days) {
            staticTreeMap.put(start.plusDays(1), 0);
            i++;
        }
        return staticTreeMap;
    }

    private List<LocalDate> countMaxPeriod(List<LocalDate> finalDateMaxUser) {
        List<LocalDate> tempList = new ArrayList<>();
        List<LocalDate> finallist = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < finalDateMaxUser.size() - 1; i++) {
            if (finalDateMaxUser.get(i).plusDays(1).isEqual(finalDateMaxUser.get(++i))) {
                if (count == 0) {
                    tempList.add(finalDateMaxUser.get(i - 1));
                    tempList.add(finalDateMaxUser.get(i));
                    count += 2;
                } else {
                    tempList.add(finalDateMaxUser.get(i));
                    count += 1;
                }
                i--;
            } else {
                if (tempList.size() < finalDateMaxUser.size() - tempList.size()) {
                    if (finallist.isEmpty()) {
                        finallist.addAll(tempList);
                        tempList.clear();
                    } else if (finallist.size() < tempList.size()) {
                        finallist.clear();
                        finallist.addAll(tempList);
                        tempList.clear();
                    } else if (finallist.size() == tempList.size()) {
                        tempList.clear();
                    } else tempList.clear();
                } else if (tempList.size() >= finalDateMaxUser.size() - tempList.size()) {
                    break;
                }
                count = 0;
                --i;
            }
        }
        if (tempList.isEmpty() && finallist.isEmpty()) {
            tempList.add(finalDateMaxUser.get(0));
            return tempList;
        } else if (finallist.size() >= tempList.size()) {
            return finallist;
        } else return tempList;
    }

    private List<LocalDate> splitListtoMaxUsersPEriod(List<LocalDate> finalDateMaxUser) {

        if (finalDateMaxUser.size() == 1) {
            return finalDateMaxUser.stream().limit(1).toList();
        } else if (finalDateMaxUser.size() == 2) {
            if (finalDateMaxUser.get(0).plusDays(1).isEqual(finalDateMaxUser.get(1))) {
                return finalDateMaxUser.stream().limit(2).toList();
            } else return finalDateMaxUser.stream().limit(1).toList();
        } else {
            List<LocalDate> temp = countMaxPeriod(finalDateMaxUser);
            if (temp.size() == 1) {
                return temp.stream().limit(1).toList();
            } else return temp;
        }
    }

    public int findMaxOnline(List<UserOnline> onlineList) {
        // Найти самое большое количество онлайна одновременно
        HashMap<LocalDate, Integer> staticHashMap = fillHashMap(onlineList);
        return Stream.of(staticHashMap.values())
                .map(e -> e.stream().max(Comparator.comparingInt(Integer::intValue))
                        .get()).findFirst()
                .get();
    }

    public LocalDate findMaxOnlineDateSimple(List<UserOnline> onlineList) {

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

    public List<LocalDate> findMaxOnlineDateHard(List<UserOnline> onlineList, int maxOnline) {

        List<UserOnline> onlineListAsc = onlineList
                .stream()
                .sorted((o1, o2) -> countDayInterval(o2.getEndSession(), o1.getEndSession()))
                .toList();

        LocalDate startDate = onlineList.get(0).getStartSession();
        LocalDate endDate = onlineListAsc.get(onlineListAsc.size() - 1).getEndSession();
        int days = countDayInterval(startDate, endDate);

        SortedMap<LocalDate, Integer> sortedMap = fillHashMap(onlineList, startDate, days);

        List<LocalDate> finalDateMaxUser = Stream.of(sortedMap.entrySet())
                .flatMap(Collection::stream)
                .filter(v -> v.getValue().equals(maxOnline))
                .map(Map.Entry::getKey)
                .toList();

        return splitListtoMaxUsersPEriod(finalDateMaxUser);
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
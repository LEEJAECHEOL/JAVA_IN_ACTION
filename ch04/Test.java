package ch04;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Test {

  public static void main(String[] args) {
    List<Dish> menu = Arrays.asList(
      new Dish("메뉴 1", true, 120),
      new Dish("메뉴 2", false, 150),
      new Dish("메뉴 3", true, 300),
      new Dish("메뉴 4", true, 350),
      new Dish("메뉴 5", false, 420),
      new Dish("메뉴 6", true, 520)
      );
    List<Dish> filteredMenu = menu.stream()
      .filter(dish -> dish.getCalories() < 320)
      .collect(Collectors.toList());
    System.out.println("filteredMenu : " + filteredMenu);
    // TAKEWHILE 활용(자바 9 문법) : 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해 스트림을 슬라이스 할 수 있음.
    // 위의 menu는 칼로리 순으로 정렬이 되어 있음. -> 320보다 작은 요리가 나올 때 반복 작업을 중단!
//    List<Dish> takeWhiledMenu = menu.stream()
//      .takeWhile(dish -> dish.getCalories() < 320)
//      .collect(Collectors.toList());
//    System.out.println(takeWhiledMenu.toString());

    // DROPWHILE(자바 9 문법) : takeWhle의 정반대 작업을 수행 : 320보다 작은 나머지 요소
//    List<Dish> takeWhiledMenu = menu.stream()
//      .dropWhile(dish -> dish.getCalories() < 320)
//      .collect(Collectors.toList());
//    System.out.println(takeWhiledMenu.toString());

    // 스트림 축소 : limit
    List<Dish> limitedMenu = menu.stream()
      .filter(dish -> dish.getCalories() < 320)
      .limit(2)
      .collect(Collectors.toList());
    System.out.println("limitedMenu : " + limitedMenu);

    // 요소 건너뛰기 : skip
    List<Dish> skippedMenu = menu.stream()
      .filter(dish -> dish.getCalories() < 320)
      .skip(2)
      .collect(Collectors.toList());
    System.out.println("skippedMenu : " + skippedMenu);

    // map
    List<String> mappedMenuName = menu.stream()
      .map(Dish::getName)
      .collect(Collectors.toList());
    System.out.println(mappedMenuName);

    // flatMap
    // 아래의 코드로 설명하자면 그냥 map만 통해서 split을하면 각각의 배열로된 List 2개가 나온다 그러면 리스트 안에 리스트가 있게 되므로 형식이 달라짐
    // 하지만 flatMap을 사용하면 2개의 리스트를 하나로 만들어주어서 List<String> 타입으로 받을 수 있다.
    List<String> words = Arrays.asList("Hello", "World");
    List<String> uniqueChar = words.stream()
      .map(word->word.split(""))
      .flatMap(Arrays::stream)
      .distinct()
      .collect(Collectors.toList());
    System.out.println(uniqueChar);

    // anyMatch
    if(menu.stream().anyMatch(Dish::isVegetarian)){
      System.out.println("vegetarian!");
    }

    // allMatch
//    boolean isHeathy = menu.stream().allMatch(dish -> dish.getCalories() < 1000);
//    System.out.println(isHeathy);

    // noneMatch
    boolean isHeathy = menu.stream().noneMatch(dish -> dish.getCalories() > 1000);
    System.out.println(isHeathy);

    // findAny
    // 쇼트서킷을 이용해 결과를 찾는 즉시 실행을 종료!
    Optional<Dish> findAnyMenu = menu.stream()
      .filter(Dish::isVegetarian)
      .findAny();

    // findFirst
    Optional<Dish> findFirstMenu = menu.stream()
      .filter(dish -> dish.getCalories() < 320)
      .findFirst();
    System.out.println("findFirstMenu : " + findFirstMenu);

    // 리듀싱 요소의 합

    List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);
    int sum1 = numbers.stream().reduce(0, Integer::sum);
    Optional<Integer> sum2 = numbers.stream().reduce((a, b)-> (a + b));
    System.out.println(sum1);
    System.out.println(sum2);

    // 최솟값, 최댓값
    Optional<Integer> min = numbers.stream().reduce(Integer::min);
    System.out.println(min);
    Optional<Integer> max = numbers.stream().reduce(Integer::max);
    System.out.println(max);

    // 실습
    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");

    List<Transaction> transactions = Arrays.asList(
      new Transaction(brian, 2011, 300),
      new Transaction(raoul, 2012, 1000),
      new Transaction(raoul, 2011, 400),
      new Transaction(mario, 2012, 710),
      new Transaction(mario, 2012, 700),
      new Transaction(alan, 2012, 950)
    );

    System.out.println(
      transactions.stream()
      .filter(transaction -> transaction.getYear() == 2011)
      .sorted(Comparator.comparing(Transaction::getValue))
      .collect(Collectors.toList())
    );
    System.out.println(
      transactions.stream()
      .map(transaction -> transaction.getTrader().getCity())
      .distinct()
      .collect(Collectors.toList())
    );
    System.out.println(
      transactions.stream()
        .map(transaction -> transaction.getTrader().getCity())
        .distinct()
        .collect(Collectors.toList())
    );
    System.out.println(
      transactions.stream()
      .map(transaction -> transaction.getTrader().getName())
      .distinct()
      .sorted()
      .collect(Collectors.toList())
    );
    System.out.println(
      transactions.stream()
      .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"))
    );
    System.out.println(
      transactions.stream()
      .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
      .map(Transaction::getValue)
      .collect(Collectors.toList())
    );
    System.out.println(
      transactions.stream()
      .map(Transaction::getValue)
      .reduce(Integer::max)
    );
    System.out.println(
      transactions.stream()
        .map(Transaction::getValue)
        .reduce(Integer::min)
    );





  }
}

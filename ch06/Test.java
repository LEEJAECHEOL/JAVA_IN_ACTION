package ch06;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

  static class Dish{
    private String name;
    private boolean vegetarian;
    private int calories;
    private String type;

    public Dish(String name, boolean vegetarian, int calories, String type) {
      this.name = name;
      this.vegetarian = vegetarian;
      this.calories = calories;
      this.type = type;
    }

    @Override
    public String toString() {
      return "Dish{" +
        "name='" + name + '\'' +
        ", vegetarian=" + vegetarian +
        ", calories=" + calories +
        ", type='" + type + '\'' +
        '}';
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setVegetarian(boolean vegetarian) {
      this.vegetarian = vegetarian;
    }

    public void setCalories(int calories) {
      this.calories = calories;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public boolean isVegetarian() {
      return vegetarian;
    }

    public int getCalories() {
      return calories;
    }

    public String getType() {
      return type;
    }
  }

  public static void main(String[] args) {
    List<Dish> menu = Arrays.asList(
      new Dish("메뉴 1", true, 120, "FISH"),
      new Dish("메뉴 2", false, 150, "OTHER"),
      new Dish("메뉴 3", true, 300, "OTHER"),
      new Dish("메뉴 4", true, 350, "OTHER"),
      new Dish("메뉴 5", false, 420, "MEAT"),
      new Dish("메뉴 6", true, 520, "MEAT")
      );
    double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
    System.out.println("avgCalories : " + avgCalories);

    IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
    System.out.println(menuStatistics);

    String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(","));
    System.out.println(shortMenu);

    Optional<Dish> mostCalories = menu.stream().collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
    System.out.println(mostCalories);
    System.out.println(
      menu.stream()
        .filter(dish -> dish.getCalories() > 500)
        .collect(Collectors.groupingBy(Dish::getType))
    );
    System.out.println(
      menu.stream().collect(
        Collectors.groupingBy(Dish::getType,
          Collectors.mapping(Dish::getName, Collectors.toList())
        )
      )
    );
    System.out.println(
      menu.stream().collect(
        Collectors.groupingBy(Dish::getType,
          Collectors.groupingBy(dish -> {
            if(dish.getCalories() <= 300){
              return "DIET";
            }else if(dish.getCalories() <= 500){
              return "NORMAL";
            }else{
              return "FAT";
            }
          })
        )
      )
    );

    System.out.println(
      menu.stream().collect(
        Collectors.groupingBy(Dish::getType, Collectors.counting())
      )
    );

    System.out.println(
      menu.stream()
          .collect(Collectors.groupingBy(
            Dish::getType,
            Collectors.collectingAndThen(
              Collectors.maxBy(Comparator.comparing(Dish::getCalories)),
              Optional::get
            )
          ))
    );

    System.out.println(
      menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian))
    );

    List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());
    System.out.println(dishes);

  }
}

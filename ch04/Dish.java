package ch04;

public class Dish {
  private String name;
  private boolean vegetarian;
  private int calories;

  public Dish(String name, boolean vegetarian, int calories) {
    this.name = name;
    this.vegetarian = vegetarian;
    this.calories = calories;
  }

  public void setVegetarian(boolean vegetarian) {
    this.vegetarian = vegetarian;
  }

  public boolean isVegetarian() {
    return vegetarian;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCalories(int calories) {
    this.calories = calories;
  }

  public String getName() {
    return name;
  }

  public int getCalories() {
    return calories;
  }

  @Override
  public String toString() {
    return "Dish{" +
      "name='" + name + '\'' +
      ", calories=" + calories +
      '}';
  }
}

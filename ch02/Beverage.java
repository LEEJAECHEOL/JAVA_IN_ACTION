package ch02;

public class Beverage {
  public String name; // 음료 명
  public String kind; // 음료 종류 ex. coffee, smoothie, tea...
  public int price;   // 가격
  public String taste;  // 맛

  public Beverage(String name, String kind, int price, String taste) {
    this.name = name;
    this.kind = kind;
    this.price = price;
    this.taste = taste;
  }

  @Override
  public String toString() {
    return "Beverage{" +
      "name='" + name + '\'' +
      ", kind='" + kind + '\'' +
      ", price=" + price +
      ", taste='" + taste + '\'' +
      '}';
  }
}

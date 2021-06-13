package ch02;

public class BeveragePricePredicate implements BeveragePredicate {
  @Override
  public boolean filter(Beverage beverage) {
    return beverage.price > 2000;
  }
}

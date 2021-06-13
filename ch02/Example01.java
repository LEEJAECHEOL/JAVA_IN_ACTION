package ch02;

import java.util.ArrayList;
import java.util.List;

public class Example01 {

  // 첫번째 가격이 2000이상 음료만 보여달라
  public static List<Beverage> filterByPrice(List<Beverage> beverages, int price){
    List<Beverage> result = new ArrayList<>();
    for(Beverage beverage : beverages){
      if(beverage.price > 2000){
        result.add(beverage);
      }
    }
    return result;
  }

  // 두번째 가격이 2000이상 이면서 종류가 스무디인 음료만 보여달라
  public static List<Beverage> filterByPriceAndKind(List<Beverage> beverages, int price, String kind){
    List<Beverage> result = new ArrayList<>();
    for(Beverage beverage : beverages){
      if(beverage.price == price && beverage.kind.equals(kind)){
        result.add(beverage);
      }
    }
    return result;
  }

//  public static List<Beverage> filterBevarage(List<Beverage> beverages, BeveragePredicate predicate){
//    List<Beverage> result = new ArrayList<>();
//    for(Beverage beverage : beverages){
//      if(predicate.filter(beverage)){
//        result.add(beverage);
//      }
//    }
//    return result;
//  }
  public static <T> List<T> filterBevarage(List<T> list, Predict<T> predicate){
    List<T> result = new ArrayList<>();
    for(T item : list){
      if(predicate.filter(item)){
        result.add(item);
      }
    }
    return result;
  }


  public static void main(String[] args) {
    List<Beverage> beverages = new ArrayList<>();
    beverages.add(new Beverage("아메리카노", "커피", 1500, "쓰"));
    beverages.add(new Beverage("카페라떼", "라떼", 2500, "달다"));
    beverages.add(new Beverage("사과스무디", "스무디", 4000, "사과맛"));
    beverages.add(new Beverage("녹차", "차", 3000, "녹차맛"));
    beverages.add(new Beverage("초코라떼", "라떼", 4000, "초코맛"));
    beverages.add(new Beverage("딸기스무디", "스무디", 4500, "딸기맛"));

    System.out.println(filterByPrice(beverages, 4000).toString());
    System.out.println(filterByPriceAndKind(beverages, 4000, "스무디").toString());

//    System.out.println(filterBevarage(beverages, new BeveragePricePredicate()));
//
//    List<Beverage> filteredBevarages = filterBevarage(beverages, new BeveragePredicate() {
//      @Override
//      public boolean filter(Beverage beverage) {
//        return beverage.price > 2000;
//      }
//    });
//    System.out.println(filteredBevarages);
    List<Beverage> filteredBevarages2 = filterBevarage(beverages, beverage -> beverage.price > 2000);
    System.out.println(filteredBevarages2);

  }
}

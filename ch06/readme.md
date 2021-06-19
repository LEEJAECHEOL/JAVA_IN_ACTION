## 스트림으로 데이터 수집
<hr />

```
중간 연산 
 - 한 스트림을 다른 스트림으로 변환하는 연산
 - 여러 연산을 연결
 - 스트림 파이프라인을 구성하며, 스트림의 요소를 소비하지 않는다.

최종연산
 - 스트림의 요소를 소비해서 최종 결과를 도출
```

### 컬렉터란 무엇인가
Collector 인터페이스 구현은 스트림의 요소를 어떤 식으로 도출할지 지정함.
1. 고급 리듀싱 기능을 수행하는 컬렉터
 - 높은 수준의 조합성과 재사용성
 - 스트림에 collect를 호출하면 스트림의 요소에 리듀싱 연산이 수행
2. 미리 정의된 컬렉터
```
 - 스트림 요소를 하나의 값으로 리듀스하고 요약
 - 요소 그룹화
 - 요소 분할 : 프레디케이트를 그룹화 함수로 사용
```
### 리듀싱과 요약
- 미리 정의된 컬렉터를 이용
- 요약 연산
```java
Collectors.summingInt, summingLong, summingDouble
Collectors.averagingInt, averagingLong, averagingDouble
  
double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
System.out.println("avgCalories : " + avgCalories);

summarizingInt, summarizingLong, summarizingDouble 
  
IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
System.out.println(menuStatistics);
```
```
avgCalories : 310.0
IntSummaryStatistics{count=6, sum=1860, min=120, average=310.000000, max=520}
```
- 문자열 연결 : joining
```java
String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(","));
System.out.println(shortMenu);
```
```
메뉴 1,메뉴 2,메뉴 3,메뉴 4,메뉴 5,메뉴 6
```
- 범용 리듀싱 요약 연산 : Collectors.reducing
  - 컬텍터에서 사용하는 이유는 <b>프로그래밍적 편의성</b> 때문!!
    
```java
Optional<Dish> mostCalories = menu.stream()
                                  .collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
System.out.println(mostCalories);
```
```
Optional[Dish{name='메뉴 6', calories=520}]
```
```
collect와 ruduce
 - collect : 도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드
 - reduce : 두 값을 하나로 도출하는 불변형 연산
 
컬렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행할 수 있다!
제네릭 와일드카드 '?' : 컬텍터의 누적자 형식이 알려지지 않았음을 의미 -> 자유로움을 의미 
```
### 그룹화
Collectors.groupingBy 분류 함수
- 그룹화된 오소 조작
  
  (Collectors.filtering 은 Java 9버전부터 사용가능)

  collect() 내에서 filtering하면 목록이 비어 있는 목록도 항목으로 추가됨.
  
  ex.    {MEAT= [...], FISH=[], OTHER=[]} 
```java
    System.out.println(
      menu.stream()
          .filter(dish -> dish.getCalories() > 500)
          .collect(Collectors.groupingBy(Dish::getType))
    );
    System.out.println(
      menu.stream().collect(
        Collectors.groupingBy(
          Dish::getType,
          Collectors.mapping(Dish::getName, Collectors.toList())
        )
      )
    );
```
```
{MEAT=[Dish{name='메뉴 6', vegetarian=true, calories=520, type='MEAT'}]}
{OTHER=[메뉴 2, 메뉴 3, 메뉴 4], FISH=[메뉴 1], MEAT=[메뉴 5, 메뉴 6]}
```
- 다수준 그룹화

  두 인수를 받는 팩토리 메소드 Collectors.groupingBy를 이용해서 항목을 다수준으로 그룹화할 수 있다.

  바깥쪽 groupingBy 메서드에서 스트림 항목을 분류할 두번 째 기준을 정의하는 내부 groupingBy를 전달해서 두수준으로 스트림의 항목을 그룹화 할 수 있다.
```java
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
```
```
{
  OTHER={
    DIET=[
      Dish{name='메뉴 2', vegetarian=false, calories=150, type='OTHER'}, 
      Dish{name='메뉴 3', vegetarian=true, calories=300, type='OTHER'}
    ], 
    NORMAL=[
      Dish{name='메뉴 4', vegetarian=true, calories=350, type='OTHER'}
    ]
  }, 
  FISH={
    DIET=[
      Dish{name='메뉴 1', vegetarian=true, calories=120, type='FISH'}
    ]
  }, 
  MEAT={
    FAT=[
      Dish{name='메뉴 6', vegetarian=true, calories=520, type='MEAT'}
    ], 
    NORMAL=[
      Dish{name='메뉴 5', vegetarian=false, calories=420, type='MEAT'}
    ]
  }
}
```
- 서브그룹으로 데이터 수집
  
  첫번째 groupingBy로 넘겨주는 컬렉터 형식은 제한이 없음
  
  groupingBy(f) -> groupingBy(f, toList()) 축약형
```java
    System.out.println(
      menu.stream().collect(
        Collectors.groupingBy(Dish::getType, Collectors.counting())
      )
    );
```
```
{OTHER=3, FISH=1, MEAT=2}
```
- 컬렉터 결과를 다른 형식에 적용 : Collectors.collectingAndThen

  적용할 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환

```java

  System.out.println(
    menu.stream()
        .collect(Collectors.groupingBy(
          Dish::getType, // 분류함
          Collectors.collectingAndThen(
            Collectors.maxBy(Comparator.comparing(Dish::getCalories)),수 // 감싸인 컬렉터
            Optional::get // 변환 함수
          )
        ))
  );
```
```
{
  OTHER=Dish{name='메뉴 4', vegetarian=true, calories=350, type='OTHER'}, 
  FISH=Dish{name='메뉴 1', vegetarian=true, calories=120, type='FISH'}, 
  MEAT=Dish{name='메뉴 6', vegetarian=true, calories=520, type='MEAT'}
}
```
```
리듀싱 컬렉터는 절대 Optional.empty()를 반환하지 않는다!!
```
### 분할
분할은 분할 함수라 불리는 프레디케이트를 분류 함수로 사용하는 특수한 그룹화 기능
```java
    System.out.println(
      menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian))
    );
```
```
{
  false=[Dish{name='메뉴 2', vegetarian=false, calories=150, type='OTHER'}, Dish{name='메뉴 5', vegetarian=false, calories=420, type='MEAT'}], 
  true=[Dish{name='메뉴 1', vegetarian=true, calories=120, type='FISH'}, Dish{name='메뉴 3', vegetarian=true, calories=300, type='OTHER'}, Dish{name='메뉴 4', vegetarian=true, calories=350, type='OTHER'}, Dish{name='메뉴 6', vegetarian=true, calories=520, type='MEAT'}]}
```
 - 장점
   - 분할 함수가 반환하는 참, 거짓 두가지 요소의 스트림 리스트를 모두 유지한다는 것!

### Collector 인터페이스
Collector 인터페이스는 리듀싱 연산(즉, 컬렉터)을 어떨게 구현할지 제공하는 메서드 집합으로 구성
```
public interface Collector<T, A, R> {
  Supplier<A> supplier();
  BiConsumer<A, T> accumulator();
  BinaryOperator<A> combiner();
  Function<A, R> finisher();
  Set<Characteristics> characteristics();
}
```
- T : 수집될 스트림 항목의 제네릭 형식
- A : 누적자, 즉 수집과정에서 중간 결과를 누적하는 객체의 형식
- R : 수집 연산 결과 객체의 형식(대개 컬렉션 형식임)이다.
- supplier : 새로운 결과 컨테이너 만들기
  - 빈 결과로 이루어진 Supplier를 반환
  - supplier는 수집과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수
  
- accumulator : 결과 컨테이너에 요소 추가
  - 리듀싱 연산을 수헹하는 함수를 반환
  
- finisher : 최종 변환값을 결과 컨테이너로 적용하기
  - 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 
    누적 과정을 끝낼 때 호출할 함수를 반환
   
- combiner : 두 결과 컨테이너 병합
  - 스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의
  - 즉 스트림의 두번째 서브파트에서 수집항 항목을 첫번째 서브파트 결과 뒤에 추가
  - 리듀싱을 병렬로 수행할 수 있음. -> Spliterator를 사용
    - 스트림을 분할해야 하는지 정의하는 조건이 거짓으로 바뀌기 전까지 원래 스트림을 재귀적으로 분할
      
      (보통 분할된 작업의 크기가 너무 작아지면 병렬 수행 속도는 순차 수행의 속도보다 느려짐. 
      
      일반적으로 프로세싱 코어의 개수를 초과하는 병렬 작업은 효율적이지 않음.)
    - 모든 서브스트림의 각 요소에 리듀싱 연산을 순차적으로 적용해서 서브스트림을 병렬로 처리할 수있음
    - 마지막에는 컬렉터의 combiner메서드가 반환하는 함수로 모든 결과를 부분결과를 쌍으로 합침
      
      (즉 분할된 모든 서브스트림의 결과를 합치면서 연산이 완료)

- characteristics
  - 컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 반환
  - Characteristics는 스트림을 병렬로 리듀스할 것인지 별럴로 리듀스 한다면 아딴 최적화를 선택해야 할지 힌트를 제공
    - <b>UNORDERED</b> : 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향 X
    - <b>CONCURRENT</b> : 다중 스레드에서 accumulator 함수를 동시에 호출가능, 이 컬렉터는 스트림의 병렬 리듀싱을 수행할 수 있
    - <b>IDENTITY_FINISH</b> : 음리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있음.
  
- 응용하기
  - ToListCollector.java참고
```java
    List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());
    System.out.println(dishes);
```
```
[
  Dish{name='메뉴 1', vegetarian=true, calories=120, type='FISH'}, 
  Dish{name='메뉴 2', vegetarian=false, calories=150, type='OTHER'}, 
  Dish{name='메뉴 3', vegetarian=true, calories=300, type='OTHER'}, 
  Dish{name='메뉴 4', vegetarian=true, calories=350, type='OTHER'}, 
  Dish{name='메뉴 5', vegetarian=false, calories=420, type='MEAT'}, 
  Dish{name='메뉴 6', vegetarian=true, calories=520, type='MEAT'}
]
```
  - 컬렉터 구현하지 않고 커스텀 수집 수행 -> 가독성이 떨어짐. 그냥 만들어서 쓰자




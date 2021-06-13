
### 스트림이란??
 - '데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소' 로 정의할 수 있음.
```
 연속된 요소 : 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 제공함.
            컬렉션의 주제는 데이터, 스트림의 주제는 계산
 소스 : 데이터 제공 소스로부터 데이터를 소비함
       리스트로 스트림을 만들면 스트림의 요소는 리스트의 요소와 같은 순서를 유지
 데이터 처리 연산 : 일반적으로 지원하는 연산과 데이터베이스와 비슷한 연산을 지원함.
                ex. filter, map, reduce, find, match, sort 등
```
 #### &nbsp;&nbsp;2가지 특징

- 파이프라이닝
  - 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있도록 스트림 자신을 반환

- 내부반복 : 
  - 이 말대로 내부 반복을 지원
```  
  filter : 람다를 인수로 받아 스트림에서 특정 요소를 제외
  map : 람다를 이용해서 한 요소를 다른 요소로 변환하거나 정보를 추출
  limit : 스트림 크기 축소
  collect : 스트림을 다른 형식으로 변환
```  
### 스트림과 컬렉션
 - 스트림과 컬렉션 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공
   
   여기서의 '연속된'은 순차적으로 값에 접근한다는 것!
 
#### 데이터를 언제 계산하는가?
| | |
|---|---|
|컬렉션|현재 자료구조가 포함하는 모든 값을 메모리에 저장하는 구조 <br /> -> 이 말은 컬렉션에 추가하기 전에 계산해야함!!|
|스트림|요청할 때만 요소를 계산하는 고정된 자료 구조임! <br /> -> 사용자가 요청하는 값만 스트림에서 추출한다는 것이 핵심!!|

<br>

 1. 딱 한번만 탐색할 수 있음!
 2. 외부 반복과 내부 반복
  - 컬렉션
    - 사용자가 직접 요소를 반복해야 함. -> 이를 외부 반복이라 함
    - 명시적으로 컬렉션 항목을 하나씩 가져와서 처리
    - 병렬성을 스스로 관리
  - 스트림
    - 반복을 알아서 처리하고 결과 스트림값을 어딘가 저장해줌 -> 내부 반복임
      
    - 데이터 표현과 하드웨어를 활용한 병렬성 구현을 자동으로 선택!
  
### 스트림 연산
1. 중간 연산
    -  중간 연산은 다른 스트림은 반환
    -  스트림 파이프라인에 실행하기 전까지는 아무 연산을 수행하지 않음

2. 최종연산
    - 스트림 파이프라인을 실행하고 결과를 도출
    
3. 스트림 이용하기
    - 데이터 소스, 중간연산 연결, 최종연산

<br />


---


### 스트림 활용
#### 1. 필터링
    1. 프레디케이트로 필터링
       - filter : 프레디케이트를 인수로 받아서 일치하는 모든 요소를 포함하는 스트림 반환
    2. 고유 요소 필터링
        - distinct : 중복을 필터링
    
<br />


#### 2. 스트림 슬라이싱

``` java
List<Dish> filteredMenu = menu.stream()
  .filter(dish -> dish.getCalories() < 320)
  .collect(Collectors.toList());
```
```
filteredMenu : [Dish{name='메뉴 1', calories=120}, Dish{name='메뉴 2', calories=150}, Dish{name='메뉴 3', calories=300}]
```

- TAKEWHILE (java 9)
```java
List<Dish> takeWhiledMenu = menu.stream()
  .takeWhile(dish -> dish.getCalories() < 320)
  .collect(Collectors.toList());
```
- TAKEWHILE (java 9)
```java
List<Dish> takeWhiledMenu = menu.stream()
  .dropWhile(dish -> dish.getCalories() < 320)
  .collect(Collectors.toList());
```
- 스트림 축소 - limit
```java
List<Dish> limitedMenu = menu.stream()
  .filter(dish -> dish.getCalories() < 320)
  .limit(2)
  .collect(Collectors.toList());
```
```
limitedMenu : [Dish{name='메뉴 1', calories=120}, Dish{name='메뉴 2', calories=150}]
```
- 요소 건너뛰기 - skip
```java
List<Dish> skippedMenu = menu.stream()
  .filter(dish -> dish.getCalories() < 320)
  .skip(2)
  .collect(Collectors.toList());
```
```
skippedMenu : [Dish{name='메뉴 3', calories=300}]
```

<br />

#### 3. 매핑
특정 객체에서 특정 데이터를 선택하는 작업은 데이터 처리과정에서 자주 수행되는 연산임!

(map, flatMap메서드)

 1. map
```java
    List<String> mappedMenuName = menu.stream()
      .map(Dish::getName)
      .collect(Collectors.toList());
```
```
[메뉴 1, 메뉴 2, 메뉴 3, 메뉴 4, 메뉴 5, 메뉴 6]
```
 2. flatMap
```java
// 아래의 코드로 설명하자면 그냥 map만 통해서 split을하면 각각의 배열로된 List 2개가 나온다. 
// 그러면 리스트 안에 리스트가 있게 되므로 형식이 달라짐
// 하지만 flatMap을 사용하면 2개의 리스트를 하나로 만들어주어서 List<String> 타입으로 받을 수 있다.
List<String> words = Arrays.asList("Hello", "World");
List<String> uniqueChar = words.stream()
  .map(word->word.split(""))
  .flatMap(Arrays::stream)
  .distinct()
  .collect(Collectors.toList());
```
```
[H, e, l, o, W, r, d]
```
<br />

#### 4. 검색과 매칭
특정 속성이 데이터 집합에 있는지 여부를 검색하는 데이터 처리!! (최종연산임!)

(allMatch, anyMatch, noneMatch, findFirst, findAny 등)

 1. 적어도 한 요소와 일치하는지 확인 - anyMatch(boolean을 리턴하므로 최종 연산)
```java
if(menu.stream().anyMatch(Dish::isVegetarian)){
  System.out.println("vegetarian!");
}
```
```
vegetarian!
```

<br>

 2. 모든 요소와 일치하는지 검사 - allMatch
```java
boolean isHeathy = menu.stream().allMatch(dish -> dish.getCalories() < 1000);
```
```
true
```

<br>

  3. noneMatch - allMatch반대역할 수행
```java
boolean isHeathy = menu.stream().noneMatch(dish -> dish.getCalories() > 1000);
```
```
true
```

요기서!! anyMatch, allMatch, noneMatch는 쇼트 서킷 기법!!

&nbsp;&nbsp;-> <b>즉, 자바스 &&, || 같은 연산을 활용한다! </b>

<br>

 4. 요소 검색 - findAny
    -   쇼트서킷을 이용해 결과를 찾는 즉시 실행을 종료!
```java
Optional<Dish> findAnyMenu = menu.stream()
  .filter(Dish::isVegetarian)
  .findAny();
```
```
Optional[Dish{name='메뉴 1', calories=120}]

요기서 Optional이란?
 - 값의 존재나 부재 여부를 표현하는 컨테이너 클레스
 - 그냥 값이 존재하는지 확인하고 값이 없을 때 어떻게 처리할지 강제하는 기능을 제공한다는 것만 알자!!
```

<br>

 5. 첫번째 요소 찾기 - findFirst
```java
Optional<Dish> findFirstMenu = menu.stream()
  .filter(dish -> dish.getCalories() < 320)
  .findFirst();
System.out.println("findFirstMenu : " + findFirstMenu);
```
```
findFirstMenu : Optional[Dish{name='메뉴 1', calories=120}]
```

<br>


#### 5. 리듀싱

- 스트림 요소를 조합해서 더 복잡한 질의를 표현할 때 사용!

 1. 요소의 합
```java
List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);
int sum1 = numbers.stream().reduce(0, Integer::sum); // 21
Optional<Integer> sum2 = numbers.stream().reduce((a, b)-> (a + b)); // 21
```

 2. 최솟값, 최댓값
```java
Optional<Integer> min = numbers.stream().reduce(Integer::min);
System.out.println(min); // 1
Optional<Integer> max = numbers.stream().reduce(Integer::max);
System.out.println(max); // 6
```

 - reduce 메서드의 장점과 별렬화
   
   - reduce를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행할 수 있게 된다.
   - stream -> parallelStream으로 바꾸면 스트림의 모든 요소를 병렬로 만듬
   - 병렬로 실행할려면 대가를 치러야 함!!
     
     - reduce에 넘겨준 람다의 상태가 바뀌지 말아야 함!
     - 연산이 어떤 순서로 실핼되더라도 결과가 바뀌지 않는 구조여야 함
    

 - 스트림 연산 : 상태 없음과 상태 있음

   - map, filter 등은 내부 상태를 갖지 않는 연산
   - reduce, sum, max같은 연산은 내부 상태가 필요
   - sorted, distinct는 모든 요소가 버퍼에 추가되어 있어야 함.
    
<br>
    
#### 6. 실전 연습
1. 2011년 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리
```java
transactions.stream()
    .filter(transaction -> transaction.getYear() == 2011)
    .sorted(Comparator.comparing(Transaction::getValue))
    .collect(Collectors.toList())
```
2. 거래자가 근무하는 모든 도시를 중복 없이 나열
```java
transactions.stream()
    .map(transaction -> transaction.getTrader().getCity())
    .distinct()
    .collect(Collectors.toList())
```
3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순 정렬
```java
transactions.stream()
    .map(transaction -> transaction.getTrader().getCity())
    .distinct()
    .collect(Collectors.toList())
```
4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환
```java
transactions.stream()
    .map(transaction -> transaction.getTrader().getName())
    .distinct()
    .sorted()
    .collect(Collectors.toList())
```
5. 밀라노에 거래자가 있는가?
```java
transactions.stream()
  .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"))
```
6. 케임브리지에 거주하는 거래자의 모든 트랜잭션 값을 출력
```java
transactions.stream()
    .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
    .map(Transaction::getValue)
    .collect(Collectors.toList())
```
7. 전체 트랜잭션 중 최대값은 얼마?
```java
transactions.stream()
    .map(Transaction::getValue)
    .reduce(Integer::max)
```
8. 전체 트랜잭션 중 최솟값은 얼마?
```java
transactions.stream()
    .map(Transaction::getValue)
    .reduce(Integer::min)
```

<br>

#### 7. 숫자형 스트림

- reduce(Integer::sum) -> sum()으로 사용가능

 1. 기본형 특화 스트림
    
    - int -> IntStream
    - double -> DoubleStream
    - long -> LongStream
    - 위의 3개는 숫자 관련 리듀싱 연산 수행 메서드를 제공함.
    
    - 숫자스트림으로 매핑 (mapToInt, mapToDouble, mapToLong)
    - 객체 스트림으로 복원하기(boxed 메서드)
    - 기본값 : OptionalInt, OptionalDouble, OptionalLong

 2. 숫자 범위
    - rangeClosed(시작값, 종료값)
    
<br>

#### 8. 스트림 만들기
 - 이런게 있다!! 사용할 때 찾아보자
```
 1. 값으로 스트림 만들기 -> Stream.of()
 2. null이 될 수 있는 객체로 스트림 만들기 -> Stram.ofNullable()
 3. 배열로 스트림 만들기 -> Arrays.stram()
 4. 파일로 스트림 만들기 
 5. 함수로 무한 스트림 만들기 -> Stream.iterate, Stream.generate (limit와 함계 연결해서 사용)
```


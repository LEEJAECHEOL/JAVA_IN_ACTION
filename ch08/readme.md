### 컬렉션 팩토리
- Arrays.asList() : 리스트
    - 고정 크기의 리스트를 만듬
    - 요소를 추가하거나 삭제할 수 없음
    - 요소를 추가하면 UnsupportedOperationException 발생
    

- new HashSet<>(Arrays.asList()) : 집합

#### 자바 9에서 사용 가능
1. 리스트 팩토리
    - List.of()
```
List<String> list = List.of("1", "2", "3");
=> 변경할 수 없는 리스트
```
2. 집합 팩토리
    - Set.of()
```
Set<String> set = Set.of("1", "2", "3");
중복 요소 X -> IllegalArgumentException발생 
집합은 오직 고유의 요소만 포함할 수 있다는 원칙!
```
3. 맵 팩토리
    - Map.ofEntries() : 인수 Map.entry(K, V) 를 받음
```
Map<String, Integer> map = Map.ofEntries(
    entry("1", 1),
    entry("2", 2),
    entry("3", 3)
);
Map.entry는 Map.Entry 객체를 만드는 새로운 팩토리 메서드
```

<br>

#### 자바 8!!

### 리스트와 집합 처리
 - removeIf : 요소 제거
 - replaceAll : 요소 변경
 - sort : 리스트 정렬

<br>

#### 리스트

1. removeIf
    - removeIf 메서드는 삭제할 요소를 가리키는 프레디케이트를  인수로 받음
```
List<Board> boards = new ArrayList<>(
  Arrays.asList(
    new Board("제목1", "내용1", true),
    new Board("제목2", "내용2", true),
    new Board("제목3", "내용3", false),
    new Board("제목4", "내용4", false),
    new Board("제목5", "내용5", true),
    new Board("제목6", "내용6", false)
  )
);
boards.removeIf(board -> board.isDelYn());
```
```
[   
  Board{title='제목3', content='내용3', delYn=false}, 
  Board{title='제목4', content='내용4', delYn=false}, 
  Board{title='제목6', content='내용6', delYn=false}
]
```
    
2. replaceAll
    - 리스트의 각 요소를 새로운 요소로 바꿀 수 있음.
```java
boards.replaceAll(board -> {
  board.setContent(board.getContent() + " 입니다.");
  return board;
});
```
```
[
  Board{title='제목3', content='내용3입니다.', delYn=false}, 
  Board{title='제목4', content='내용4입니다.', delYn=false}, 
  Board{title='제목6', content='내용6입니다.', delYn=false}
]
```

#### 맵
1. forEach 
```java
    Map<String, String> maps = new HashMap();
    maps.put("1", "프랑스");
    maps.put("2", "한국");
    maps.put("3", "미국");
    maps.put("4", "아랍에미리트");
    maps.put("5", "일본");

    maps.forEach((key, name)-> System.out.println("key : " + key + ", 나라 : " + name));
```
2. 정렬 메서드
    1. Entry.comparingByValue
    2. Entry.comparingByKey
    
```java
    maps.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .forEachOrdered(System.out::println);
```
```
3=미국
4=아랍에미리트
5=일본
1=프랑스
2=한국
```
3. getOrDefault 메서드

    - 기존에 찾으려는 키가 존재하지 않으면 널이 반환되므로 NullPointerException을 방지하려고 체크해야함.
    - getOrDefault를 사용하면 쉽게 해결 가능
    - 첫번째 인수 : 키, 두번째 인수 : 기본값
    - 키가 존재하느냐의 여부에 따라 두번째 인수가 반환될지 결정
    
```java
  System.out.println(
    maps.getOrDefault("6", "없음")
  ); // 없음
  System.out.println(
    maps.getOrDefault("2", "없음")
  ); // 한국
```

4. 계산 패턴
    1. computeIfAbsent
       - 제공된 키에 해당하는 값이 없으면, 키를 이용해 새 값을 계산하고 맵에 추가
    2. computeIfPresent
       - 제공된 키가 존재하면 새 값을 계산하고 맵에 추가
    3. compute
       - 제공된 키로 새 값을 계산하고 맵에 저장 (키가 없으면 NullPointerException 발생)
    
```java
  maps.computeIfAbsent("6", (key) -> maps.put(key, "태국"));
  System.out.println(maps); // {1=프랑스, 2=한국, 3=미국, 4=아랍에미리트, 5=일본, 6=태국}

  maps.computeIfPresent("6", (key, value)-> value + " 중복");
  System.out.println(maps); // {1=프랑스, 2=한국, 3=미국, 4=아랍에미리트, 5=일본, 6=태국 중복}

```
5. 삭제 패턴
 - remove : remove(key, valye)

6. 교체 패턴
    1. replaceAll
    2. replace
```java
  maps.replaceAll((key, value) -> value + " replaceAll");
  System.out.println(maps); // {1=프랑스 replaceAll, 2=한국 replaceAll, 3=미국 replaceAll, 4=아랍에미리트 replaceAll, 5=일본 replaceAll}

  maps.replace("2", "대한민국");
  System.out.println(maps); // {1=프랑스, 2=대한민국, 3=미국, 4=아랍에미리트, 5=일본}
```

7. 합침 
    - putAll
    - merge
    
```java
Map<String, Integer> map1 = new HashMap<>();
map1.put("A", 100);
map1.put("B", 200);
map1.put("C", 300);
Map<String, Integer> map2 = new HashMap<>();
map2.put("C", 400);
map2.put("D", 500);
map2.put("E", 600);

map1.putAll(map2);
System.out.println(map1); // {A=100, B=200, C=400, D=500, E=600}

// merge와 forEach를 사용하면 중복된 값을 어떻게 처리할지 처리할 수 있음.  
map2.forEach((key, value) -> map1.merge(key, value, (v1, v2) -> v1 + v2));
System.out.println(map1); // {A=100, B=200, C=700, D=500, E=600}
```
<br>

#### 개선된 ConcurrentHashMap
ConcurrentHashMap 클래스는 동시성 친화적이며 최신 기술을 반영한 HashMap 버전임.
1. 리듀스와 검색
    1. forEach : 각 (키, 값)쌍에 주어진 액션을 실행
    2. reduce : 모든 (키, 값)쌍을 제공된 리듀스 함수를 이용해 결과로 합침
    3. search : 널이 아닌 값을 반환할 때까지 각 (키, 값)쌍에 함수를 적용
2. 계수
   - mappingCount : 맵의 매핑 개수를 반환
3. 집합뷰
    - keySet : ConcurrentHashMap을 집합 뷰로 반환

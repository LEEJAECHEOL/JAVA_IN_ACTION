### 값이 없는 상황을 어떻게 처리할까?
1. 보수적인 자세로 NullPointerException 줄이기
2. null 때문에 발생하는 문제
```
에러의 근원이다.
코드를 어지럽힌다.
아무 의미가 없다.
자바 철학에 위배
형식 시스템에 구멍을 만든다.
```
3. 다른 언어는 null대신 무엇을 사용하나?
 - ?.(그루비같은 어어)  Maybe(하스켈)등

### Optional
- Optional은 선택형값을 캡술화하는 클래스

- 값이 있으면 값을 감싼다.

- 값이 없으면 Optional.empty 메서드로 Optional을 반환
```
null
 - null을 참조하려 하면 NullPointerException 발생
 
Optional.empty()
 - Optional 객체 이기 때문에 다양하게 활용 가능
```
- Optional을 이용하면 값이 없는 상황에 우리 데이터에 문제가 있는 것인지 아닌지 명확하게 구분할 수 있다.
- Optional의 역할은 더 이해하기 쉬운 API를 설계하도록 돕는 것!


### Optional 적용 패턴
1. Optional 객체 만들기
    - 빈 Optional : Optional.empty()
    - null이 아닌 값으로 Optional만들기 : Optional.of()
    - null값으로 Optional만들기 : Optional.ofNullable()
    

2. 맵으로 Optional의 값을 추출하고 변환하기 : map()
    - 스트림의 map 메서드와 개념적으로 비슷
    

3. flatMap으로 Optional 객체 연결
    - flatMap은 인수로 받은 함수를 적용해서 생성된 각각의 스트림에서 콘텐츠만 남김
    - (함수를 적용해서 생성된 모든 스트림이 하나의 스트림으로 병합되어 평준화됨)
    
```
public String getCarInsuranceName(Optional<Person> person) {
  return person.flatMap(Person::getCar)
               .flatMap(Car::getInsurance)
               .map(Insurance::getName)
               .orElse("Unknown");    
}
```

Optional을 이용해서 값이 없는 상황을 처리함! -> 가독성이 좋음!
```
도메인 모델에 Optional을 사용했을 때 데이터를 직렬화할 수 없는 이유
 - Optional의 용도가 선택형 반환값을 지원하는 것!!
 - Optional클래스는 필드 형식으로 사용할 것을 가정하지 않았으므로 Serializable 인터페이스를 구현하지 않음
 - 직렬화 모델이 필요하다면 Optional로 값을 반환받을 수 있는 메서드를 추가하는 방식을 권장
```


<br>

4. Optional 스트림 조작 (자바9)
 - Optional.stream() 지원!

5. 디폴트 액션과 Optional 언랩
 - get() : 
   - 값이 있으면 가져오고 없으면 NoSuchElementException 발생 
   - 안전하지 않음 null이랑 다를게 없음
 - orElse()
   - 값을 포함하지 않을 때 기본값을 설정 가능
 - orElseGet()
   - orElse에 대응하는 게으른 버전 = 값이 없을 때만 Supplier가 발
   - 기본값이 반드시 필요한 상황에 사용
 - orElseThrow()생
   - 값이 없을 때 예외 발생 (예외 종류 선택)
 - ifPresent()
   - 값이 존재할 때 인수로 넘겨준 동작 실행, 없으면 아무 일도 안함
 - ifPresentOrElse() (자바 9)
   -  비어 있을 때 실행할 수 있는 Runnable을 인수로 받는다.
    
6. filter()
 - 종종 객체의 메서드를 호출해서 어떤 프로퍼티를 확인해야 할 때!

### Optional을 사용한 실용 예제
1. 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기
2. 예외와 Optiona 클래스
    - Optional을 사용하면 try/catch없이 가독성이 좋게 구현가능
    
3. 기본형 Optional을 사용하지 말아야하는 이유 (OptionalInt, OptionalDouble 등을 말함)
 - Optional의 최대 요소 수는 한 개이므로 Optional에서는 기본형 특화 클래스로 성능 개선을 할 수 없다.



### 더 보면 좋은 것!

http://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/

1. isPresent()-get() 대신 orElse()/orElseGet()/orElseThrow()

2. orElse(new ...) 대신 orElseGet(() -> new ...)

3. 단지 값을 얻을 목적이라면 Optional 대신 null 비교
 - 단순히 값 또는 null을 얻을 목적이라면 Optional 대신 null 비교를 쓰자.

4. Optional 대신 비어있는 컬렉션 반환
    - 컬렉션을 반환하는 Spring Data JPA Repository 메서드는 null을 반환하지 않고 비어있는 컬렉션을 반환해주므로 Optional로 감싸서 반환할 필요가 없다

5. Optional을 필드로 사용 금지

6. Optional을 생성자나 메서드 인자로 사용 금지

7. Optional을 컬렉션의 원소로 사용 금지

8. of(), ofNullable() 혼동 주의
    - of(X)은 X가 null이 아님이 확실할 때만 사용해야 한다.
    - ofNullable(X)은 X가 null일 수도 있을 때만 사용한다.
    
9. Optional<T> 대신 OptionalInt, OptionalLong, OptionalDouble
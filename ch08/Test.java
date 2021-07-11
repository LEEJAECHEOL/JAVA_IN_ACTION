package ch08;

import java.util.*;

public class Test {
  public static class Board {
    private String title;
    private String content;
    private boolean delYn;

    public Board(String title, String content, boolean delYn) {
      this.title = title;
      this.content = content;
      this.delYn = delYn;
    }

    public String getTitle() {
      return title;
    }

    public String getContent() {
      return content;
    }

    public boolean isDelYn() {
      return delYn;
    }

    @Override
    public String toString() {
      return "Board{" +
        "title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", delYn=" + delYn +
        '}';
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public void setDelYn(boolean delYn) {
      this.delYn = delYn;
    }
  }

  public static void main(String[] args) {
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
    System.out.println(boards);

    boards.replaceAll(board -> {
      board.setContent(board.getContent() + " 입니다.");
      return board;
    });
    System.out.println(boards);

    Map<String, String> maps = new HashMap();
    maps.put("1", "프랑스");
    maps.put("2", "한국");
    maps.put("3", "미국");
    maps.put("4", "아랍에미리트");
    maps.put("5", "일본");

    maps.forEach((key, name)-> System.out.println("key : " + key + ", 나라 : " + name));

    maps.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .forEachOrdered(System.out::println);

//    System.out.println(
//      maps.getOrDefault("6", "없음")
//    );
//
//    maps.computeIfAbsent("6", (key) -> maps.put(key, "태국"));
//    System.out.println(maps);
//
//    maps.computeIfPresent("6", (key, value)-> value + " 중복");
//    System.out.println(maps);

//    maps.replaceAll((key, value) -> value + " replaceAll");
//    System.out.println(maps);

//    maps.replace("2", "대한민국");
//    System.out.println(maps);

    Map<String, Integer> map1 = new HashMap<>();
    map1.put("A", 100);
    map1.put("B", 200);
    map1.put("C", 300);

    Map<String, Integer> map2 = new HashMap<>();
    map2.put("C", 400);
    map2.put("D", 500);
    map2.put("E", 600);

//    map1.putAll(map2);
    map2.forEach((key, value) -> map1.merge(key, value, (v1, v2) -> v1 + v2));

    System.out.println(map1);



  }
}

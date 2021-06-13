package ch02;

public interface Predict<T> {
  boolean filter(T item);
}

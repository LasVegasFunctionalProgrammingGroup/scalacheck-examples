package com.ixee;

public class Pair<T, U> {
  T _1;
  U _2;

  public Pair(T t, U u) {
    _1 = t;
    _2 = u;
  }

  public T _1() { return _1; }
  public U _2() { return _2; }

  public String toString() {
    return "{1: " + _1 + ", 2: '" + _2 + "'}";
  }

  public void cloneAs(Pair<T, U> other) {
    _1 = other._1();
    _2 = other._2();
  }

  public boolean equals(Object other) {
    if (other instanceof Pair) {
      Pair<T, U> castedOther = (Pair<T, U>)other;
      return
        _1.equals(castedOther._1()) && _2.equals(castedOther._2());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return _1.hashCode() ^ _2.hashCode();
  }
}

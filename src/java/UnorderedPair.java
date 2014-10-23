package com.ixee;

public class UnorderedPair<U> extends Pair<U, U> {
  public UnorderedPair(U u1, U u2) {
    super(u1, u2);
  }

  public boolean equals(Object other) {
    if (other instanceof UnorderedPair) {
      UnorderedPair<U> castedOther = (UnorderedPair<U>)other;
      return
        (_1.equals(castedOther._1()) && _2.equals(castedOther._2())) ||
        (_2.equals(castedOther._1()) && _1.equals(castedOther._2()));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return _1.hashCode() ^ _2.hashCode();
  }
}

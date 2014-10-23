package test

import org.scalatest._
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.scalacheck.Gen
import org.scalacheck.Prop._

class ScalaTestExample extends FreeSpec
    with MustMatchers
    with Checkers
    with GeneratorDrivenPropertyChecks {

  def isEven(i: Int): Boolean = i % 2 == 0
  def isOdd(i: Int): Boolean = !isEven(i)

  "all integers are odd or even" in {
    check { (i: Int) =>
      isEven(i) || isOdd(i)
    }
  }

  "all integers are even (wait...)" in {
    check { (i: Int) =>
      isEven(i)
    }
  }

  "all integers times two are even" in {
    check { (i: Int) =>
      isEven(i * 2)
    }
  }

  "all integers times two plus one are odd" - {
    "the test" in {
      check { (i: Int) =>
        isOdd(i * 2 + 1)
      }
    }

    "but counterexample..." in {
      isOdd(Integer.MAX_VALUE * 2 + 1) must equal (true)
    }
  }

  "using a generator for evens" - {
    val evens = for {
      x <- Gen.choose(0, Integer.MAX_VALUE) if x % 2 == 0
    } yield x

    "when testing with an even, isEven == true" in {
      forAll { (i: Int) =>
        whenever (i % 2 == 0) { isEven(i) must equal (true) }
      }
    }

    "all values are odd" in {
      // !!!
      forAll(evens) { isOdd(_) must equal (true) }
    }

    "all values are even" in {
      forAll(evens) { (i: Int) =>
        //isOdd(i) must equal (true)
        isEven(i) must equal (true)
      }
    }
  }
}

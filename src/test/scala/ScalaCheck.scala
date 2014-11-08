package test

import org.scalacheck._
import org.scalacheck.Prop._

object ScalaCheckExample extends Properties("numbers") {
  def isEven(i: Int): Boolean = i % 2 == 0
  def isOdd(i: Int): Boolean = !isEven(i)

  val intsAreOddOrEven = forAll { (x: Int) =>
    isEven(x) || isOdd(x)
  }

  intsAreOddOrEven.check
  property("integers are odd or even") = intsAreOddOrEven

  // forAll goes up to 8-argument functions
  /*
    http://www.scalacheck.org/files/scalacheck_2.11-1.11.6-api/#org.scalacheck.Prop$
  */
  
  // Have to be careful that the domain being tested over is correct..
  property("integers times two are even (incorrect impl)") =
    forAll { (x: Int) => isEven(x) }

  property("integers times two are even (correct impl)") =
    forAll { (x: Int) => isEven(x * 2) }

  property("evens plus one are odd") = forAll { (x: Int) =>
    isOdd(x * 2 + 1)
  }

  // we can just filter the generated values for ones we want to test!
  property("evens are even!") = forAll { (x: Int) =>
    x % 2 == 0 ==> isEven(x)
  }

  // But it would be nicer to generate just even numbers, rather than
  // integers, then do the conversion ourself.
  val evens = for {
    x <- Gen.choose(0, 1023)
  } yield x * 2

  property("evens plus one are still odd") = forAll(evens) {
    (x: Int) => classify(x > 0, "positive") {
      isEven(x)
    }
  }

  property("evens are still even when just a function") =
    forAll(evens) { isEven }
}

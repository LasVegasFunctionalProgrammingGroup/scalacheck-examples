package test

import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop._

class Example extends Properties("numbers") {
  def isEven(i: Int) = i % 2 == 0
  def isOdd(i: Int) = !isEven(i)

  // But it would be nicer to generate just even numbers, rather than
  // integers, then do the conversion ourself.
  // so, construct one!
  val evens = for {
    x <- Gen.choose(-1023, 1023)
  } yield x * 2

  val odds = for {
    x <- Gen.choose(0, 1000)
  } yield x * 2 + 1

  val oddTimesEven = for {
    x <- evens
    y <- odds
  } yield x*y

  property("evens are still even when just a function") =
    forAll(oddTimesEven) { isEven }
}

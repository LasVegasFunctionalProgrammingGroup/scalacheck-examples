package test

import org.scalatest._
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.scalacheck.Gen
import org.scalacheck.Prop._

import com.ixee.Pair

class PairTest extends FreeSpec
    with MustMatchers
    with Checkers
    with GeneratorDrivenPropertyChecks {

  "Pair class" - {
    "is an ordered pair of two elements" in {
      forAll { (i: Int, s: String) =>
        val p = new Pair(i, s)
        p._1 must equal (i)
        p._2 must equal (s)
      }
    }

    "stringifies to show its elements" in {
      forAll { (i: Int, s: String) =>
        new Pair(i, s).toString must equal (s"{1: $i, 2: '$s'}")
      }
    }

    "preserves ordering when checking equality" - {
      "equal when the values are the same" in {
        forAll { (i: Int, j: Int) =>
          new Pair(i, j) must equal (new Pair(i, j))
        }
      }

      "not equal when the values are in reversed order" in {
        forAll { (i: Int, j: Int) =>
          new Pair(i, j) must not equal (new Pair(j, i))
        }
      }

      "not equal when different values are in reversed order" in {
        forAll { (i: Int, j: Int) =>
          //classify(i == j, "filtered check") {
            whenever (i != j) {
              new Pair(i, j) must not equal (new Pair(j, i))
            }
          //}
        }
      }
    }

    "hash code has an even distribution" in {
      val list = scala.collection.mutable.Buffer[Int]()

      val stringSize = 8
      val binaryChar = Gen.oneOf('0', '1')
      def binaryString = Gen.listOfN(stringSize, binaryChar) map { _.mkString("") }
      val rounds = 1000

      val bucketCount = Math.pow(2, stringSize)
      val likelihood = Math.ceil(rounds / bucketCount)

      forAll[String, String](binaryString, binaryString, minSuccessful(rounds), maxSize(stringSize)) {
        (a: String, b: String) => list += new Pair(a, b).hashCode()
      }

      val maxCount: Int = list.count(_ == list.max)
      val expectedCount: Int = likelihood.ceil.toInt * 2
      maxCount must be < expectedCount
    }
  }
}

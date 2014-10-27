package test

import org.scalatest._
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.scalatest.exceptions.GeneratorDrivenPropertyCheckFailedException
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
        // an example of a subtle failure if the test conditions
        // aren't well thought out: if i = j then Pair(i, j) == Pair(j, i)!
        // so, we should expect this to fail!
        a [GeneratorDrivenPropertyCheckFailedException] must be thrownBy {
          // this actually came out of me writing the test and
          // trying to figure out why it failed
          forAll { (i: Int, j: Int) =>
            new Pair(i, j) must not equal (new Pair(j, i))
          }
        }
      }

      "not equal when different values are in reversed order" in {
        forAll { (i: Int, j: Int) =>
          whenever (i != j) {
            new Pair(i, j) must not equal (new Pair(j, i))
          }
        }
      }
    }

    "hash code has an even distribution" in {
      /*
       This is actually kind of abusing the ScalaCheck
       machinery from generators and forAll to have
       a convenient set of randomized data to test
      */
      val list = scala.collection.mutable.Buffer[Int]()

      val stringSize = 8
      val binaryChar = Gen.oneOf('0', '1')
      def binaryString = Gen.listOfN(stringSize, binaryChar) map { _.mkString("") }
      val rounds = 1000

      // there are 2^n binary strings of length n
      // and for a pair of strings A and B, there's Pair(A, B) and Pair(B, A)
      // so total possible distinct hashes is 2 * 2^n
      val bucketCount = 2*Math.pow(2, stringSize)
      // the likelihood of any particular hash having been calculated is
      // numberOfHashes * 1 / possibleDistinctHashCount
      val likelihood = Math.ceil(rounds / bucketCount)

      forAll(binaryString, binaryString, minSuccessful(rounds)) {
        (a: String, b: String) => list += new Pair(a, b).hashCode()
      }

      // as long as the most common hash was not too common, we can
      // infer that the hash function has a reasonably good spread
      val maxCount: Int = list.count(_ == list.max)
      // fudgeFactor is used to compensate for the fact that the limited number
      // of runs doesn't model perfect mathematical theory; we'll often end up
      // with one or two above the theoretical expected number because of randomness
      val fudgeFactor = (rounds * 0.005).toInt
      val expectedCount: Int = likelihood.ceil.toInt + fudgeFactor
      maxCount must be < expectedCount
    }
  }
}

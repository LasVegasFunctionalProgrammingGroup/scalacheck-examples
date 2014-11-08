scalacheck-examples
===================

Examples of property-based testing with ScalaCheck, and using ScalaCheck with ScalaTest

To get started, clone the repo and do `sbt test`.

This compiles and executes the tests in `src/test/scala`, which are the bulk of the repository.

There is some Java code in `src/java` that is also tested using ScalaTest/ScalaCheck.

`src/test/scala/ScalaCheck.scala` shows some testing with *only* ScalaCheck. The property check `integers times two are even (incorrect impl)` intentionally fails; I slipped up when writing the test and noticed it's a subtle and kind of interesting way property-based testing can appear to be wrong.. but it was me who was wrong.

`src/test/scala/ScalaTest.scala` shows how testing might look in ScalaTest while using ScalaCheck.

`src/test/scala/PairTest.scala` shows testing some real-world code and properties one might care about in real usage. The code under test is `src/java/Pair.java`, a class that's very similar to Scala's `Tuple2`

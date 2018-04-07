package scalavators

import org.scalatest.{FlatSpec, Matchers}

class ScalavatorsTest extends FlatSpec with Matchers {
  it should "returns true status" in {
    new Scalavators().status shouldBe true
  }
}

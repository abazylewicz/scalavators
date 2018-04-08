package scalavators

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.SortedSet

class ElevatorTest extends FlatSpec with Matchers {

  val anyId = ElevatorId(1)

  "Adding destination" should "add destination when no previous destinations specified" in {
    val elevator = Elevator(anyId, Floor(0), SortedSet[Floor]())
    elevator.addDestination(Floor(2))
    elevator.destinations should contain only Floor(2)
  }

  it should "skip duplicate destinations" in {
    val elevator = Elevator(anyId, Floor(0), SortedSet[Floor](Floor(1)))
    elevator.addDestination(Floor(1))
    elevator.destinations should contain only Floor(1)
  }

  it should "ignore current floor" in {
    val elevator = Elevator(anyId, Floor(0), SortedSet[Floor]())
    elevator.addDestination(Floor(0))
    elevator.destinations shouldBe empty
  }

  it should "keep destinations sorted" in {
    val elevator = Elevator(anyId, Floor(0), SortedSet[Floor](Floor(1), Floor(3)))
    elevator.addDestination(Floor(2))
    elevator.destinations should contain inOrder (Floor(1), Floor(2), Floor(3))
  }

  "Updating elevator" should "move elevator toward upward destination" in {
    val elevator = Elevator(anyId, Floor(0), SortedSet[Floor](Floor(2)))
    elevator.update()
    elevator.floor shouldBe Floor(1)
  }

  it should "move elevator toward downward destination" in {
    val elevator = Elevator(anyId, Floor(3), SortedSet[Floor](Floor(0)))
    elevator.update()
    elevator.floor shouldBe Floor(2)
  }

  it should "keep the destination when it is not reached" in {
    val elevator = Elevator(anyId, Floor(3), SortedSet[Floor](Floor(0)))
    elevator.update()
    elevator.destinations should contain only Floor(0)
  }

  it should "remove the destination after it is reached" in {
    val elevator = Elevator(anyId, Floor(0), SortedSet[Floor](Floor(1), Floor(2)))
    elevator.update()
    elevator.destinations should contain only Floor(2)
  }

}

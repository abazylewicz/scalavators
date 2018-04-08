package scalavators

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.SortedSet

class ElevatorTest extends FlatSpec with Matchers {

  val anyId = ElevatorId(1)

  "Adding destination" should "add destination when no previous destinations specified" in {
    val elevator = Elevator(anyId, Floor(0))
    elevator.addDestination(Floor(2), Floor(3))
    elevator.maybeDestinations shouldBe defined
    elevator.maybeDestinations.get.floors should contain key Floor(2)
  }

  it should "ignore current floor and add target floor" in {
    val elevator = Elevator(anyId, Floor(0))
    elevator.addDestination(Floor(0), Floor(1))
    elevator.maybeDestinations.get.floors should contain (Floor(1) -> Set())
  }

  "Updating elevator" should "move elevator toward upward destination" in {
    val elevator = Elevator(anyId, Floor(0))
    elevator.addDestination(Floor(2), Floor(3))
    elevator.update()
    elevator.floor shouldBe Floor(1)
  }

  it should "move elevator toward downward destination" in {
    val elevator = Elevator(anyId, Floor(3))
    elevator.addDestination(Floor(1), Floor(3))
    elevator.update()
    elevator.floor shouldBe Floor(2)
  }

  it should "keep the destination when it is not reached" in {
    val elevator = Elevator(anyId, Floor(3))
    elevator.addDestination(Floor(1), Floor(3))
    elevator.update()
    elevator.maybeDestinations.get.floors should contain key Floor(1)
  }

  it should "remove the destination after it is reached and add its target" in {
    val elevator = Elevator(anyId, Floor(0))
    elevator.addDestination(Floor(1), Floor(3))
    elevator.update()
    elevator.maybeDestinations.get.floors should contain (Floor(3) -> Set())
  }

}

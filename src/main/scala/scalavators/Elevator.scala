package scalavators

import scala.collection.SortedSet

case class Elevator(id: ElevatorId,
                    var floor: Floor,
                    var destinations: SortedSet[Floor]) {

  def this(id: ElevatorId, floor: Floor) {
    this(id, floor, SortedSet[Floor]())
  }

  def changeFloor(floor: Floor): Unit = {
    this.floor = floor
  }

  def addDestination(destinationFloor: Floor): Unit = {
    if (destinationFloor != floor) {
      destinations = destinations + destinationFloor
    }
  }

  def update(): Unit = {
    if (destinations.nonEmpty) {
      if (floor.number < destinations.head.number) {
        floor = Floor(floor.number + 1)
      } else {
        floor = Floor(floor.number - 1)
      }
      destinations = destinations - floor
    }
  }
}

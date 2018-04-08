package scalavators.dispatchers

import scalavators.{Elevator, Request}

/**
  * Shortest path dispatching that dispatches request to elevator that will have closest distance to request floor.
  * If request floor is already on elevator target list it is treated as 0 distance pickup.
  * Otherwise distance is calculated as distance between current floor, next step (if defined), and request floor
  */
object ShortestPathDispatcher extends Dispatching {
  override def dispatch(request: Request, elevators: Iterable[Elevator]): Option[Elevator] = {
    val elevatorOpt = elevators.toSeq
      .filter(_.accepts(request))
      .sortBy(distance(_, request))
      .headOption
    elevatorOpt.foreach(_.addDestination(request.from, request.to))
    elevatorOpt
  }

  def distance(elevator: Elevator, request: Request): Int = {
    if (elevator.maybeDestinations.exists(_.floors.contains(request.from))) {
      0
    } else {
      val nextFloor = elevator.maybeDestinations.map(elevator.nextStop).getOrElse(elevator.floor)
      val nextFloorDistance = Math.abs(nextFloor.number - elevator.floor.number)
      val nextFloorAndPickupLocationDistance = Math.abs(nextFloor.number - request.from.number)
      nextFloorDistance + nextFloorAndPickupLocationDistance
    }
  }
}

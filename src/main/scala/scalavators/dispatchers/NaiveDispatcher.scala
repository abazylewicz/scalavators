package scalavators.dispatchers

import scalavators.{Elevator, Request}

/**
  * Naive dispatcher assigning request to elevator with shortest destination set
  */
object NaiveDispatcher extends Dispatching {
  override def dispatch(request: Request, elevators: Iterable[Elevator]): Option[Elevator] = {
    val elevatorOpt = elevators.toSeq
      .filter(_.accepts(request))
      .sortBy(_.maybeDestinations.map(_.floors.size).getOrElse(0))
      .headOption
    elevatorOpt.foreach(_.addDestination(request.from, request.to))
    elevatorOpt
  }
}

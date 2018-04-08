package scalavators.dispatchers

import scalavators.{Elevator, Request}

trait Dispatching {
  def dispatch(request: Request, elevators: Iterable[Elevator]): Option[Elevator]
}





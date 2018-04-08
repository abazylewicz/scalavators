package scalavators

case class Elevator(id: ElevatorId,
                    var floor: Floor) {

  var maybeDestinations: Option[Destinations] = None

  def changeFloor(floor: Floor): Unit = {
    this.floor = floor
  }

  def accepts(request: Request): Boolean = {
    maybeDestinations.forall(destinations => {
      request.direction == destinations.direction &&
        (floor == request.from || request.direction == Direction(floor, request.from))
    })
  }

  def addDestination(from: Floor, to: Floor): Unit = {
    val destination: (Floor, Option[Floor]) =
      if (floor != from) (from, Some(to))
      else (to, None)
    maybeDestinations = maybeDestinations.orElse(Some(new Destinations(Direction(from, to))))
    maybeDestinations.foreach(it => it.add(destination._1, destination._2))
  }


  def update(): Unit = {
    maybeDestinations.foreach(destinations => {
      val nextFloor = nextStop(destinations)
      floor =
        if (nextFloor.number > floor.number) Floor(floor.number + 1)
        else Floor(floor.number - 1)
      destinations.resolve(floor)
    })
    maybeDestinations = maybeDestinations.filter(_.floors.nonEmpty)
  }

  private def nextStop(destinations: Destinations) = {
    destinations.floors.keySet.maxBy(it => destinations.direction match {
      case Up => -it.number
      case Down => it.number
    })
  }
}

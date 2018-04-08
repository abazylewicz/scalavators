package object scalavators {

  case class ElevatorId(id: Int) extends AnyVal
  case class Floor(number: Int) extends AnyVal
  case class Request(floor: Floor, direction: Direction)
  sealed trait Direction
  final object Up extends Direction
  final object Down extends Direction

  implicit val floorOrdering: Ordering[Floor] = Ordering.by[Floor, Int](_.number)

}

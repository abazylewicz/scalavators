package object scalavators {

  case class ElevatorId(id: Int) extends AnyVal

  case class Floor(number: Int) extends AnyVal

  case class Request(from: Floor, to: Floor) {
    lazy val direction = Direction(from, to)
  }

  implicit val floorOrdering: Ordering[Floor] = Ordering.by[Floor, Int](_.number)

}

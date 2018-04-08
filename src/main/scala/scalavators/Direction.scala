package scalavators

sealed trait Direction

object Direction {
  def apply(from: Floor, to: Floor): Direction = {
    if (from == to) throw new IllegalArgumentException("From and to cant be the same")
    if (from.number < to.number) Up
    else Down
  }
}

case object Up extends Direction

case object Down extends Direction

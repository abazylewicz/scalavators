package scalavators

import scala.collection.mutable

class Destinations(val direction: Direction) {
  def this(from: Floor, to:Floor)  {
    this(Direction(from, to))
    add(from, to)
  }

  val floors: mutable.Map[Floor, Set[Floor]] = mutable.Map[Floor, Set[Floor]]()

  def add(from: Floor, maybeTo: Option[Floor]): Unit = {
    if(maybeTo.isDefined) {
      add(from, maybeTo.get)
    } else {
      add(from)
    }
  }

  def add(from: Floor, to: Floor): Unit = {
    floors.put(from, floors.getOrElse(from, Set()) + to)
  }

  def add(from: Floor): Unit = {
    floors.put(from, floors.getOrElse(from, Set()))
  }

  def resolve(floor: Floor): Unit = {
    floors.remove(floor).foreach(_.foreach(add))
  }
}

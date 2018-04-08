package scalavators

import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable


class ElevatorSystem(val elevators: Map[ElevatorId, Elevator])(val dispatching: Dispatching) extends ElevatorControls {

  val log: Logger = LoggerFactory.getLogger(classOf[ElevatorSystem])
  val requestQueue: mutable.Queue[Request] = mutable.Queue[Request]()
  var steps = 0

  override def status: Seq[(ElevatorId, Floor, Option[Floor])] = {
    elevators.map(it => (it._1, it._2.floor, it._2.maybeDestinations.flatMap(_.floors.headOption.map(_._1)))).toSeq
  }

  override def fullStatus: Seq[Elevator] = elevators.values.toList

  override def update(elevatorId: ElevatorId, currentFloor: Floor, goalFloor: Floor): Elevator = {
    val elevator = elevators(elevatorId)
    elevator.changeFloor(currentFloor)
    elevator.addDestination(currentFloor, goalFloor)
    elevator
  }

  override def pickup(floor: Floor, destination: Floor): Unit = {
    log.info(s"Got pickup request from: $floor, to: $destination")
    val request = Request(floor, destination)
    val maybeElevator = dispatching.dispatch(request, elevators.values)
    if(maybeElevator.isEmpty) {
      requestQueue.enqueue(request)
    }
    log.info(maybeElevator.map( e => s"Request dispatched to: ${e.id}")
      .getOrElse("Request not dispatched"))
  }

  override def step(): Unit = {
    elevators.values.foreach(_.update())
    requestQueue.dequeueAll(_ => true).foreach(req => pickup(req.from, req.to))
    log.info(s"Elevator system updated, status: $fullStatus")
    steps+=1
  }
}

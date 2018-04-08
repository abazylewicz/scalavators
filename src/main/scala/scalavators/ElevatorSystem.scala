package scalavators

import org.slf4j.{Logger, LoggerFactory}


class ElevatorSystem(val elevators: Map[ElevatorId, Elevator])(val dispatching: Dispatching) extends ElevatorControls {

  val log: Logger = LoggerFactory.getLogger(classOf[ElevatorSystem])

  var steps = 0

  override def status: Seq[(ElevatorId, Floor, Option[Floor])] = {
    elevators.map(it => (it._1, it._2.floor, it._2.destinations.headOption)).toSeq
  }

  override def update(elevatorId: ElevatorId, currentFloor: Floor, goalFloor: Floor): Elevator = {
    val elevator = elevators(elevatorId)
    elevator.changeFloor(currentFloor)
    elevator.addDestination(goalFloor)
    elevator
  }

  override def pickup(floor: Floor, direction: Direction): Unit = {
    log.info(s"Got pickup request to: $floor, with direction: $direction")
    val maybeElevator = dispatching.dispatch(Request(floor, direction), elevators.values)
    log.info(maybeElevator.map( e => s"Request dispatched to: ${e.id}")
      .getOrElse("Request not dispatched"))
  }

  override def step(): Unit = {
    elevators.values.foreach(_.update())
    log.info(s"Elevator system updated, status: $status")
    steps+=1
  }
}

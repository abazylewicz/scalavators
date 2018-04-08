package scalavators

trait ElevatorControls {

  def status: Seq[(ElevatorId, Floor, Option[Floor])]
  def update(elevatorId: ElevatorId, currentFloor: Floor, goalFloor: Floor) : Elevator
  def pickup(floor: Floor, destination: Floor)
  def step()

}

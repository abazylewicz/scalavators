package scalavators

trait ElevatorControls {

  def status: Seq[(ElevatorId, Floor, Option[Floor])]
  def fullStatus: Seq[Elevator]
  def update(elevatorId: ElevatorId, currentFloor: Floor, goalFloor: Floor) : Elevator
  def pickup(floor: Floor, destination: Floor)
  def step()

}

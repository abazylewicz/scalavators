package scalavators

import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.slf4j.{Logger, LoggerFactory}

class SimulationTest extends FlatSpec with GeneratorDrivenPropertyChecks {

  val log: Logger = LoggerFactory.getLogger(classOf[SimulationTest])

  override implicit val generatorDrivenConfig: PropertyCheckConfiguration = PropertyCheckConfiguration(minSuccessful = 2, sizeRange = 10)
  private val directionGen = Gen.choose(0, 1).map { i =>
    if (i % 2 == 0) Up
    else Down
  }

  private val requestsGen = Gen.listOf(
    Gen.listOf(
      Gen.choose(-2, 10)
        .flatMap(floor => directionGen.map(Request(Floor(floor), _)))
    ).flatMap(req => Gen.choose(0,3).map((req, _)))
  )


  it should "simulate requests" in forAll(requestsGen) { (requests: List[(List[Request], Int)]) =>
    log.info("Simulation starting")
    val elevator1 = new Elevator(ElevatorId(1), Floor(0))
    val elevator2 = new Elevator(ElevatorId(2), Floor(0))
    val elevatorSystem = new ElevatorSystem(
      Map(elevator1.id -> elevator1,
        elevator2.id -> elevator2))(NaiveDispatcher)
    requests.foreach { reqs =>
      reqs._1.foreach { req =>
        elevatorSystem.pickup(req.floor, req.direction)
      }
      (0 until reqs._2).foreach(_ => elevatorSystem.step())
    }
    while(elevatorSystem.status.exists(_._3.isDefined)){
      elevatorSystem.step()
    }
    log.info(s"Simulation finished, ${elevatorSystem.steps} steps used to complete ${requests.map(_._1.size).sum} requests")
  }
}

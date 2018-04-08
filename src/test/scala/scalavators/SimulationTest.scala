package scalavators

import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.slf4j.{Logger, LoggerFactory}
import scalavators.dispatchers.{NaiveDispatcher, ShortestPathDispatcher}

class SimulationTest extends FlatSpec with GeneratorDrivenPropertyChecks {

  val log: Logger = LoggerFactory.getLogger(classOf[SimulationTest])

  override implicit val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSuccessful = 2, minSize = 1, sizeRange = 10, maxDiscardedFactor = 100d)

  private val elevatorGen = Gen.choose(0, 1000).map(it => Elevator(ElevatorId(it), Floor(0)))
  private val elevatorsGen = for {
    n <- Gen.choose(2, 4)
    elevators <- Gen.listOfN(n, elevatorGen)
  } yield elevators

  private val requestGen = for {
    from <- Gen.choose(-2, 20)
    to <- Gen.choose(-2, 20)
  } yield Request(Floor(from), Floor(to))

  private val requestsGen = Gen.listOf(
    Gen.nonEmptyListOf(requestGen)
      .flatMap(req => Gen.choose(0, 10).map((req, _)))
  )


  it should "simulate naive dispatcher" in forAll(elevatorsGen, requestsGen) { (elevators: List[Elevator], requests: List[(List[Request], Int)]) =>
    val elevatorSystem = new ElevatorSystem(elevators.map(it => it.id -> it).toMap)(NaiveDispatcher)
    simulation(elevators, requests, elevatorSystem)
  }

  it should "simulate shortest distance dispatcher" in forAll(elevatorsGen, requestsGen) { (elevators: List[Elevator], requests: List[(List[Request], Int)]) =>
    val elevatorSystem = new ElevatorSystem(elevators.map(it => it.id -> it).toMap)(ShortestPathDispatcher)
    simulation(elevators, requests, elevatorSystem)
  }

  private def simulation(elevators: List[Elevator], requests: List[(List[Request], Int)], elevatorSystem: ElevatorSystem): Unit = {
    log.info(s"Simulation starting with ${elevatorSystem.dispatching}")
    requests.foreach { reqs =>
      reqs._1.foreach { req =>
        if (req.from != req.to) {
          elevatorSystem.pickup(req.from, req.to)
        }
      }
      (0 until reqs._2).foreach(_ => elevatorSystem.step())
    }
    while (elevatorSystem.status.exists(_._3.isDefined)) {
      elevatorSystem.step()
    }
    log.info(s"Simulation finished, ${elevatorSystem.steps} steps used to complete ${requests.map(_._1.size).sum} " +
      s"requests with ${elevators.size} elevators and ${elevatorSystem.dispatching}")
  }
}

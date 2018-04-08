package scalavators

import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.slf4j.{Logger, LoggerFactory}

class SimulationTest extends FlatSpec with GeneratorDrivenPropertyChecks {

  val log: Logger = LoggerFactory.getLogger(classOf[SimulationTest])

  override implicit val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSuccessful = 2, sizeRange = 10, maxDiscardedFactor = 100d)

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
    Gen.listOf(requestGen)
      .flatMap(req => Gen.choose(0, 3).map((req, _)))
  )


  it should "simulate requests" in forAll(elevatorsGen, requestsGen) { (elevators: List[Elevator], requests: List[(List[Request], Int)]) =>
    log.info("Simulation starting")

    val elevatorSystem = new ElevatorSystem(elevators.map(it => it.id -> it).toMap)(NaiveDispatcher)
    requests.foreach { reqs =>
      reqs._1.foreach { req =>
        if(req.from != req.to){
          elevatorSystem.pickup(req.from, req.to)
        }
      }
      (0 until reqs._2).foreach(_ => elevatorSystem.step())
    }
    while (elevatorSystem.status.exists(_._3.isDefined)) {
      elevatorSystem.step()
    }
    log.info(s"Simulation finished, ${elevatorSystem.steps} steps used to complete ${requests.map(_._1.size).sum} " +
      s"requests with ${elevators.size} elevators")
  }
}

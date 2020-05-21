package simulations

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import src.test.scala.UserService.getAccessModules

class GetCitiesSimulation extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8093")
    .header("Accept", "application/json")
      .proxy(Proxy("localhost", 8888))

  val scn = scenario("Get all cities - Ramp up users scenario")
    .exec(getAccessModules)

  /**
   * Injection Design:
   * 1) Start with wait - simulate loading or pause
   * 2) Inject 10 users at once
   * 3) start to ramp up users to 250 for 2 minutes
   * 4) Do nothing for the next 40 seconds and inject 5 new users at once
   */
  setUp(
    scn.inject(
      nothingFor(3 seconds),
      atOnceUsers(5),
      constantUsersPerSec(5) during(20 seconds),
      ).protocols(httpConf.inferHtmlResources())
    ).maxDuration(4 minutes)

}

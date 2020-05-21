package simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import src.test.scala.UserService.loginUser

import scala.concurrent.duration._

class LoginUsersSimulation extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8093")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8888))

  val scn = scenario("Login users scenario")
    .exec(loginUser)

  setUp(
    scn.inject(
      nothingFor(3 seconds),
      rampUsers(80) during (2 minutes),
      ).protocols(httpConf.inferHtmlResources())
    )

}

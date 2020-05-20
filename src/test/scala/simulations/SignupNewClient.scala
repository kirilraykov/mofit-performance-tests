package simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import src.test.scala.UserService.{getAccessModules, signupNewUser, getCities, createClient, getClient}
import src.test.scala.SportService.getSports

class SignupNewClient extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8093")
    .header("Accept", "application/json")
//    .proxy(Proxy("localhost", 8888))

  val scn = scenario("Signup new Client Scenario")
    .exec(getAccessModules)
    .exec(signupNewUser)
    .exec(getCities)
    .exec(getSports)
    .exec(createClient)
    .exec(getClient)

  setUp(
  scn.inject(atOnceUsers(1))
  ).protocols(httpConf.inferHtmlResources())

}

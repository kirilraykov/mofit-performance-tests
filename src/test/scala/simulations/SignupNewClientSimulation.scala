package simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import scala.concurrent.duration._
import src.test.scala.UserService.{getAccessModules, signupNewUser, getCities, createClient, getClient}
import src.test.scala.SportService.getSports

class SignupNewClientSimulation extends Simulation {

  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def userCount: Int = getProperty("rampUsers", "200").toInt
  def rampDuration: Int = getProperty("rampDuration", "2").toInt
  def testDuration: Int = getProperty("totalDuration", "4").toInt

  before {
    println(s"Ramping up users to total: $userCount users")
    println(s"Ramping users over: $rampDuration minutes")
    println(s"Total test duration: $testDuration minutes")
  }

  val httpConf = http.baseUrl("http://localhost:8093")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8888))

  val scn = scenario("Signup new Client Scenario")
    .exec(getAccessModules)
    .exec(signupNewUser)
    .exec(getCities)
    .exec(getSports)
    .exec(createClient)
    .exec(getClient)

  setUp(
    scn.inject(
      nothingFor(3 seconds),
      atOnceUsers(10),
      rampUsers(userCount) during (rampDuration minutes),
      nothingFor(40 seconds),
      atOnceUsers(5),
      ).protocols(httpConf.inferHtmlResources())
    ).maxDuration(testDuration minutes)

}

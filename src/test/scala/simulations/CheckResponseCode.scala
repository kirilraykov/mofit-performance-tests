package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class CheckResponseCode extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8071/")
    .header("Accept", "application/json")

  val scn = scenario("Video Game DB - 3 calls")

    .exec(http("Get all video games - 1st call")
      .get("access/modules")
        .check(status.is(400)))
    .pause(5)

    .exec(http("Get specific game")
      .get("access/modules")
        .check(status.in(200 to 210)))
    .pause(1, 20)

    .exec(http("Get all Video games - 2nd call")
      .get("access/modules")
        .check(status.not(404), status.not(500)))
    .pause(3000.milliseconds)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
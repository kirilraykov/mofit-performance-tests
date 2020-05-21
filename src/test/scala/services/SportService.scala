package src.test.scala

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

package object SportService {

  // Request names
  val REQ_NAME_GET_SPORTS = "Get Sports"

  // Endpoint paths
  val GET_SPORTS_URL = "/sports"

  val getSports: ChainBuilder = {
    exec(http(REQ_NAME_GET_SPORTS)
           .get(GET_SPORTS_URL)
           .header("Authorization", "Bearer ${jwtToken}")
           .queryParam("getActiveOnly", true)
           .check(status.is(200))
           .check(jsonPath("$..sportId").findRandom.saveAs("sportId"))
           .check(bodyString.saveAs("body")))
//      .exec { session => println("Random sportId: " + session("sportId").as[String]); session}
  }

}

package simulations

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class CustomFeeder extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8093/users/signup")
    .header("Accept", "application/json")

  var idNumbers = (1 to 3).iterator
  val random = new Random()
  val now = LocalDate.now()
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def randomString(length: Int) = {
    random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getRandomDate(startDate: LocalDate, random: Random): String = {
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name" -> ("Game-" + randomString(5)),
    "releaseDate" -> getRandomDate(now, random),
    "reviewScore" -> random.nextInt(100),
    "category" -> ("Category-" + randomString(6)),
    "rating" -> ("Rating-" + randomString(4))
    ))

  def postNewGame() = {
    repeat(5) {
      feed(customFeeder)
        .exec(http("Post New Game")
          .post("videogames/")
            .body(ElFileBody("bodies/NewGameTemplate.json")).asJson
          .check(status.is(200)))
        .pause(1)
    }
  }

  val scn = scenario("Post new games")
      .exec(postNewGame())


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}

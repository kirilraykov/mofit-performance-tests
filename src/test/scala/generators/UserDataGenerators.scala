package src.test.scala.generators

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.core.Predef.csv
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.util.Random

object UserDataGenerators {

  // Test data variables for custom feeders
  val permissions = List("READ", "WRITE", "UPDATE", "DELETE")
  val genders = List("MALE", "FEMALE")
  val experienceLevels = List("BEGINNER", "INTERMEDIATE", "ADVANCED", "PROFESSIONAL")
  val random = new Random()
  val now: LocalDate = LocalDate.now()
  val datePattern: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  // Util methods
  def getRandomEmail(): String = {
    "randomEmail" + getRandomString(10) + "@gmail.com"
  }

  def getRandomString(length: Int): String = {
    random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getRandomDate(startDate: LocalDate, random: Random): String = {
    startDate.minusYears(random.nextInt(40))
      .plusWeeks(random.nextInt(20))
      .format(datePattern)
  }

  // Feeders - skip dynamic variables, which will be populated when executing the scenario
  val userSignupFeeder: Iterator[Map[String, Any]] = Iterator.continually(Map(
    "email" -> getRandomEmail(),
    "password" -> "password",
    "reviewScore" -> random.nextInt(100),
    "permission" -> permissions(random.nextInt(permissions.size))
    ))

  val createClientFeeder: Iterator[Map[String, Any]] = Iterator.continually(Map(
    "birthDate" -> getRandomDate(now, random),
    "clientInfo" -> ("Info for client random " + getRandomString(random.nextInt(400))),
    "firstName" -> ("Name" + getRandomString(random.nextInt(7))),
    "lastName" -> ("Name" + getRandomString(random.nextInt(7))),
    "gender" -> genders(random.nextInt(genders.size)),
    "experienceLevel" -> experienceLevels(random.nextInt(experienceLevels.size)),
    "experienceYears" -> random.nextInt(6),
    "notified" -> true
    ))

  val userLoginFeeder = csv("feeders/dev/users.csv").circular
}

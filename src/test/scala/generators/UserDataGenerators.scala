package src.test.scala.generators

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.util.Random

object UserDataGenerators {

  // Test data variables for custom feeders
  val permissions: Iterator[String] = List("READ", "WRITE", "UPDATE", "DELETE").iterator
  val genders: Iterator[String] = List("MALE", "FEMALE").iterator
  val experienceLevels: Iterator[String] = List("BEGINNER", "INTERMEDIATE", "ADVANCED", "PROFESSIONAL").iterator
  var moduleId: Iterator[Int] = (1 to 3).iterator
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
    "moduleId" -> moduleId.next(),
    "reviewScore" -> random.nextInt(100),
    "permission" -> permissions.next()
    ))

  val createClientFeeder: Iterator[Map[String, Any]] = Iterator.continually(Map(
    "birthDate" -> getRandomDate(now, random),
    "clientInfo" -> ("Info for client random " + getRandomString(random.nextInt(400))),
    "firstName" -> ("Name" + getRandomString(random.nextInt(7))),
    "lastName" -> ("Name" + getRandomString(random.nextInt(7))),
    "gender" -> genders.next(),
    "experienceLevel" -> experienceLevels.next(),
    "experienceYears" -> random.nextInt(6),
    "notified" -> true
    ))
}

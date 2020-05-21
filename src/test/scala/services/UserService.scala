package src.test.scala

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import src.test.scala.generators.UserDataGenerators.{createClientFeeder, userSignupFeeder, userLoginFeeder}

package object UserService {

  // Request names
  val REQ_NAME_SIGNUP_NEW_USER = "Signup new User"
  val REQ_NAME_LOGIN_USER = "Login User"
  val REQ_NAME_SIGNUP_NEW_CLIENT = "Create client"
  val REQ_NAME_GET_ACCESS_MODULES = "Get Access modules"
  val REQ_NAME_GET_CITIES = "Get Cities"
  val REQ_NAME_GET_CLIENT = "Get Client"

  // Endpoint paths
  val CREATE_USER_URL = "/users/signup"
  val LOGIN_USER_URL = "/users/login"
  val GET_ACCESS_MODULES_URL = "/access/modules"
  val GET_CITIES_URL = "/cities"
  val CREATE_CLIENT_URL = "/clients/"

  val signupNewUser: ChainBuilder = {
    feed(userSignupFeeder)
      .exec(http(REQ_NAME_SIGNUP_NEW_USER)
              .post(CREATE_USER_URL)
              .body(ElFileBody("bodies/user/SignupUserPayload.json")).asJson
              .check(status.is(201))
              .check(jsonPath("$.token").find.saveAs("jwtToken"))
              .check(jsonPath("$.userId").find.saveAs("userId")))
      .exec { session => println("Jwt token: " + session("jwtToken").as[String])
                         println("UserId: " + session("userId").as[Int]); session}
      .pause(2)
  }

  val getAccessModules: ChainBuilder = {
    exec(http(REQ_NAME_GET_ACCESS_MODULES)
            .get(GET_ACCESS_MODULES_URL)
            .check(status.is(200))
           .check(jsonPath("$..moduleId").findRandom.saveAs("moduleId")))
      .exec { session => println("Random moduleId: " + session("moduleId").as[String]); session}
      .pause(1)
  }

  val getCities: ChainBuilder = {
    exec(http(REQ_NAME_GET_CITIES)
           .get(GET_CITIES_URL)
           .header("Authorization", "Bearer ${jwtToken}")
           .check(status.is(200))
           .check(jsonPath("$..cityId").findRandom.saveAs("cityId")))
//      .exec { session => println("Random cityId: " + session("cityId").as[String]); session}
      .pause(1)
  }

  val createClient: ChainBuilder = {
    feed(createClientFeeder)
      .exec(session => session
        .set("userId", session("userId").as[Int])
        .set("sportId", session("sportId").as[Int])
        .set("cityId", session("cityId").as[Int]))
      .exec(http(REQ_NAME_SIGNUP_NEW_CLIENT)
              .post(CREATE_CLIENT_URL)
              .header("Authorization", "Bearer ${jwtToken}")
              .body(ElFileBody("bodies/user/CreateClientPayload.json")).asJson
              .check(status.is(201))
              .check(bodyString.saveAs("clientId")))
//      .exec { session => println("ClientId: " + session("clientId").as[String]); session}
      .pause(2)
  }

  val getClient: ChainBuilder = {
    exec(http(REQ_NAME_GET_CLIENT)
           .get(CREATE_CLIENT_URL + "${clientId}")
           .header("Authorization", "Bearer ${jwtToken}")
           .check(status.is(200))
           .check(jsonPath("$..userId").is("${userId}"))
           .check(jsonPath("$..cityId").is("${cityId}"))
           .check(jsonPath("$..sportId").is("${sportId}"))
           .check(jsonPath("$..clientId").is("${clientId}")))
      .pause(1)
  }

  val loginUser: ChainBuilder = {
    feed(userLoginFeeder)
      .exec(http(REQ_NAME_LOGIN_USER)
              .post(LOGIN_USER_URL)
              .body(ElFileBody("bodies/user/LoginUserPayload.json")).asJson
              .check(status.is(200))
              .check(jsonPath("$.token").find.saveAs("jwtToken"))
              .check(jsonPath("$.userId").find.saveAs("userId")))
            .exec { session => println("Jwt token: " + session("jwtToken").as[String])
                               println("UserId: " + session("userId").as[Int]); session}
      .pause(2)
  }
}

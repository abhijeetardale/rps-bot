package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import model.rbsinfo
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneAppPerTest}
import play.api.cache.CacheApi
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.CachingService

import scala.concurrent.{ExecutionContext, Future}

class StartControllerSpec extends PlaySpec with GuiceOneAppPerSuite {

  val request = FakeRequest().withHeaders("Host" -> "localhost")

  val cacheApi = app.injector.instanceOf(classOf[CacheApi])
  val rbsCacheObj = new CachingService(cacheApi)
  val controller = new StartController(rbsCacheObj)
  implicit lazy val actorSystem = ActorSystem() ;
  implicit lazy val materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global


  "Start controller" must {

    "return 200 for a POST and cache the info" in {
      val json =
        """{
          | "opponentName": "Tiger",
          | "pointsToWin": 1000,
          | "maxRounds": 2000,
          | "dynamiteCount": 100
          | }""".stripMargin
      val request = FakeRequest().withHeaders("Host" -> "localhost").withJsonBody(Json.parse(json))
      val result = controller.start().apply(request).map{ res =>
          status(Future(res)) mustBe OK
          rbsCacheObj.getOpponentInfo() mustBe Some(rbsinfo("Tiger", 1000, 2000, 100))
      }
    }

    "return 400 for a POST if json is not correct and info should not cached" in {
      cacheApi.remove("opponentInfo")
      val json =
        """{
          | "opponentLastMove": "Tiger",
          | "pointsToWin": 1000,
          | "maxRounds": 2000,
          | "dynamiteCount": 100
          | }""".stripMargin
      val result = controller.start().apply(request).map{ res =>
        status(Future(res)) mustBe BAD_REQUEST
        rbsCacheObj.getOpponentInfo() mustBe None
      }
    }
  }
}

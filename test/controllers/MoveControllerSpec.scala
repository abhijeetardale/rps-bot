package controllers

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import model.lastMove
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneAppPerTest}
import play.api.cache.CacheApi
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{CachingService, rbsCache}
import scala.concurrent.duration._

import scala.concurrent.{ExecutionContext, Future, Await}

class MoveControllerSpec extends PlaySpec with GuiceOneAppPerSuite {

  val request = FakeRequest().withHeaders("Host" -> "localhost")

  val cacheApi = app.injector.instanceOf(classOf[CacheApi])
  val rbsCacheObj = new CachingService(cacheApi)
  val controller = new MoveController(rbsCacheObj)
  implicit lazy val actorSystem = ActorSystem() ;
  implicit lazy val materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  "Move controller calling get move" must {

    "return 200 for a GET" in {

      val result = controller.move()(request)

      status(result) mustBe OK
    }

    """return "ROCK" as JSON for a GET""" in {

      val result = controller.move()(request)

      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"ROCK""""
    }

  }

  "Move controller calling post move" must {

    "return 400 for a POST for Bad Request and object should not be cached" in {
      val json = """{"lastMove": "PAPER"}"""
      val requestPost = FakeRequest().withJsonBody(Json.parse(json))
      val result = controller.lastOpponentMove().apply(requestPost).map{ res =>
        status(Future(res)) mustBe BAD_REQUEST
        rbsCacheObj.getOpponentLastMove() mustBe None
      }
    }

    "return 200 for a POST and update the session and object should be cached" in {
      val json = """{"lastOpponentMove": "PAPER"}"""
      val requestPost = request.withJsonBody(Json.parse(json))
      val result = controller.lastOpponentMove().apply(requestPost).map{ res =>
        status(Future(res)) mustBe OK
        rbsCacheObj.getOpponentLastMove() mustBe Some(List(lastMove("PAPER")))
      }
    }
  }
}

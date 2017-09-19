package controllers

import javax.inject.Inject

import model.rbsinfo
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, Controller}
import services.{CachingService, rbsCache}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class StartController@Inject()(cache: CachingService) extends Controller {
  def start(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    Try(request.body.as[rbsinfo]) match {
      case Success(payload) =>
        cache.resetCache()
        cache.saveOpponentInfo(payload)
        Future.successful((Ok))
      case Failure(e) => Future.successful(BadRequest(s"Invalid payload: $e"))
    }
  }
}

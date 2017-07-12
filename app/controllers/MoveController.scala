package controllers

import javax.inject.Inject

import model.{rbsinfo, lastMove}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, Controller}
import services.{rbsCache, CachingService}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class MoveController@Inject()(cache: CachingService) extends Controller {

  def move() = Action {
    Ok(Json.toJson("ROCK"))
  }

  def lastOpponentMove() : Action[JsValue] = Action.async(parse.json) { implicit request =>
    Try(request.body.as[lastMove]) match {
      case Success(payload) =>
        cache.upateLastMove(payload)
        Future.successful((Ok))
      case Failure(e) => Future.successful(BadRequest(s"Invalid payload: $e"))
    }
  }
}

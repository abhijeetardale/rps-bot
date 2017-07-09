package controllers

import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class MoveController extends Controller {

  def move() = Action {
    Ok(Json.toJson("ROCK"))
  }

  def lastOpponentMove() : Action[JsValue] = Action.async(parse.json) { implicit request =>
    Try(request.body.as[rbsinfo]) match {
      case Success(payload) => Future.successful((Ok))
      case Failure(e) => Future.successful(BadRequest(s"Invalid payload: $e"))
    }
  }
}

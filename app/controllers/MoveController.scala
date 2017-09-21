package controllers

import javax.inject.Inject

import helper.MoveHelper
import model.{rbsinfo, lastMove}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, Controller}
import services.{rbsCache, CachingService}
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class MoveController @Inject()(cache: CachingService) extends Controller {
  def moveHelper: MoveHelper = new MoveHelper()

  def move() = Action {
    val opponentInfo = cache.getOpponentInfo()
    val opponentLastMoves = cache.getOpponentLastMove()
    val myLastMoves = cache.getMyLastMove()
    val move = if (opponentLastMoves.isDefined && myLastMoves.isDefined && opponentInfo.isDefined) {
      moveHelper.getMove(opponentLastMoves.get, myLastMoves.get, opponentInfo.get)
    } else {
      moveHelper.getDefaultMove()
    }
    cache.updateMyLastMove(move)
    Ok(Json.toJson(move))
  }

  def lastOpponentMove(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    Try(request.body.as[lastMove]) match {
      case Success(payload) =>
        cache.updateOpponentLastMove(payload.opponentLastMove)
        Future.successful((Ok))
      case Failure(e) => Future.successful(BadRequest(s"Invalid payload: $e"))
    }
  }
}

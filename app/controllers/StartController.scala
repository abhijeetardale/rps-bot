package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

case class rbsinfo(opponentName:String,
                   pointsToWin:Int,
                   maxRounds:Int,
                   dynamiteCount:Int)
object rbsinfo{
  implicit val formats=Json.format[rbsinfo]
}


class StartController extends Controller {
  def start() = Action {
    Ok
  }
}

package model

import play.api.libs.json.Json

case class rbsinfo(opponentName:String,
                   pointsToWin:Int,
                   maxRounds:Int,
                   dynamiteCount:Int)

object rbsinfo{
  implicit val formats=Json.format[rbsinfo]
}

case class lastMove(opponentLastMove:String)

object lastMove{
  implicit val formats=Json.format[lastMove]
}


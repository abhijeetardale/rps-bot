package helper

import model.lastMove

import scala.util.Random


class MoveHelper {

 def getMove(lastMoves :  Option[List[lastMove]]): String = {

   if(lastMoves.isDefined && lastMoves.get.nonEmpty){
     ""
   } else {
     Random.shuffle(List("ROCK", "PAPER", "SCISSORS", "DYNAMITE", "WATERBOMB")).head
   }
 }

  def oppositionDynamiteCount(lastMoves :  Option[List[lastMove]]) : Int = {

    if(lastMoves.isDefined && lastMoves.nonEmpty)
      lastMoves.get.filter(move=>move.lastOpponentMove=="DYNAMITE").length
    else 0

  }

}

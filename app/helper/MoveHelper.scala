package helper

import model.rbsinfo

import scala.util.Random

trait Constants {
  val ROCK = "ROCK"
  val PAPER = "PAPER"
  val SCISSORS = "SCISSORS"
  val DYNAMITE = "DYNAMITE"
  val WATERBOMB = "WATERBOMB"
}

class MoveHelper extends Constants{

 def getMove(opponentLastMoves :  List[String], myLastMoves :  List[String], opponentInfo: rbsinfo): String = {

   val opponentDynamite = oppositionDynamiteCount(opponentLastMoves)
   val myDynamite = oppositionDynamiteCount(myLastMoves)

   val randomNumber = if(myDynamite<opponentInfo.dynamiteCount && opponentLastMoves.size > (opponentInfo.maxRounds*0.75)) {1 + Random.nextInt(2)} else {2 + Random.nextInt(3)};

   val move  =  opponentLastMoves.nonEmpty && myLastMoves.nonEmpty match {
       //case true if(allCondition(opponentLastMoves, myLastMoves,randomNumber) && opponentDynamite>=(opponentInfo.dynamiteCount * 0.5) && myDynamite<opponentInfo.dynamiteCount) => DYNAMITE
       case true if(allCondition(opponentLastMoves, myLastMoves,3)) => getLastThreeSameMove(opponentLastMoves)
       case true if(allCondition(opponentLastMoves, myLastMoves,2)) => getLastTwoSameMove(opponentLastMoves)
       case true if(allCondition(opponentLastMoves, myLastMoves,1)) => getLastSameMove(opponentLastMoves)
       case _ => lasNtLost(opponentLastMoves, myLastMoves,opponentInfo, myDynamite: Int)
     }
   move
 }
  
  def getDefaultMove(): String ={
    Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
  }

  private def lasNtLost(opponentLastMoves :  List[String], myLastMoves :  List[String], opponentInfo: rbsinfo, myDynamite: Int): String ={
    opponentLastMoves match {
      //case _  if(myDynamite<opponentInfo.dynamiteCount && opponentLastMoves.size > (opponentInfo.maxRounds*0.85) && Random.nextBoolean()) => DYNAMITE
      /*case _::ROCK::ROCK::ROCK::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,SCISSORS,SCISSORS)) => Random.shuffle(List(PAPER, ROCK)).head
      case _::ROCK::ROCK::PAPER::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,SCISSORS,ROCK)) => Random.shuffle(List(SCISSORS, ROCK)).head
      case _::ROCK::ROCK::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,SCISSORS,PAPER)) => Random.shuffle(List(ROCK, PAPER)).head

      case _::ROCK::PAPER::ROCK::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,ROCK,SCISSORS)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::ROCK::PAPER::PAPER::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,ROCK, ROCK)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::ROCK::PAPER::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,ROCK,PAPER)) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head

      case _::ROCK::SCISSORS::ROCK::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,PAPER,SCISSORS)) => Random.shuffle(List(ROCK, SCISSORS)).head
      case _::ROCK::SCISSORS::PAPER::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,PAPER,ROCK)) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case _::ROCK::SCISSORS::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(SCISSORS,PAPER,PAPER)) => Random.shuffle(List(ROCK, SCISSORS)).head

      case _::PAPER::ROCK::ROCK::Nil  if(myLastMoves.takeRight(3) == List(ROCK,SCISSORS,SCISSORS)) => Random.shuffle(List(ROCK, PAPER)).head
      case _::PAPER::ROCK::PAPER::Nil  if(myLastMoves.takeRight(3) == List(ROCK,SCISSORS,ROCK)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::PAPER::ROCK::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(ROCK,SCISSORS,PAPER)) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head

      case _::PAPER::PAPER::ROCK::Nil  if(myLastMoves.takeRight(3) == List(ROCK,ROCK,SCISSORS)) => Random.shuffle(List(ROCK, PAPER)).head
      case _::PAPER::PAPER::PAPER::Nil  if(myLastMoves.takeRight(3) == List(ROCK,ROCK,ROCK)) => Random.shuffle(List(SCISSORS, PAPER)).head
      case _::PAPER::PAPER::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(ROCK,ROCK,PAPER)) => Random.shuffle(List(SCISSORS, ROCK)).head

      case _::PAPER::SCISSORS::ROCK::Nil  if(myLastMoves.takeRight(3) == List(ROCK,PAPER,SCISSORS)) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case _::PAPER::SCISSORS::PAPER::Nil  if(myLastMoves.takeRight(3) == List(ROCK,PAPER,ROCK)) => Random.shuffle(List(SCISSORS, PAPER)).head
      case _::PAPER::SCISSORS::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(ROCK,PAPER,PAPER)) => Random.shuffle(List(ROCK, PAPER)).head

      case _::SCISSORS::ROCK::ROCK::Nil  if(myLastMoves.takeRight(3) == List(PAPER,SCISSORS,SCISSORS)) => Random.shuffle(List(ROCK, SCISSORS)).head
      case _::SCISSORS::ROCK::PAPER::Nil  if(myLastMoves.takeRight(3) == List(PAPER,SCISSORS,ROCK)) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case _::SCISSORS::ROCK::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(PAPER,SCISSORS,PAPER)) => Random.shuffle(List(ROCK, PAPER)).head

      case _::SCISSORS::PAPER::ROCK::Nil  if(myLastMoves.takeRight(3) == List(PAPER,ROCK,SCISSORS)) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case _::SCISSORS::PAPER::PAPER::Nil  if(myLastMoves.takeRight(3) == List(PAPER,ROCK,ROCK)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::SCISSORS::PAPER::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(PAPER,ROCK,PAPER)) => Random.shuffle(List(SCISSORS, ROCK)).head

      case _::SCISSORS::SCISSORS::ROCK::Nil  if(myLastMoves.takeRight(3) == List(PAPER,PAPER,SCISSORS)) => Random.shuffle(List(ROCK, SCISSORS)).head
      case _::SCISSORS::SCISSORS::PAPER::Nil  if(myLastMoves.takeRight(3) == List(PAPER,PAPER,ROCK)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::SCISSORS::SCISSORS::SCISSORS::Nil  if(myLastMoves.takeRight(3) == List(PAPER,PAPER,PAPER)) => Random.shuffle(List(ROCK, PAPER)).head*/

      case _::ROCK::ROCK::Nil  if(myLastMoves.takeRight(2) == List(SCISSORS,SCISSORS)) => Random.shuffle(List(PAPER, ROCK)).head
      case _::ROCK::PAPER::Nil  if(myLastMoves.takeRight(2) == List(SCISSORS,ROCK)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::ROCK::SCISSORS::Nil  if(myLastMoves.takeRight(2) == List(SCISSORS,PAPER)) => Random.shuffle(List(ROCK, PAPER)).head

      case _::PAPER::ROCK::Nil  if(myLastMoves.takeRight(2) == List(ROCK,SCISSORS)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::PAPER::PAPER::Nil  if(myLastMoves.takeRight(2) == List(ROCK,ROCK)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::PAPER::SCISSORS::Nil  if(myLastMoves.takeRight(2) == List(ROCK,PAPER)) => Random.shuffle(List(SCISSORS, ROCK)).head

      case _::SCISSORS::ROCK::Nil  if(myLastMoves.takeRight(2) == List(PAPER,SCISSORS)) => Random.shuffle(List(PAPER, SCISSORS)).head
      case _::SCISSORS::PAPER::Nil  if(myLastMoves.takeRight(2) == List(PAPER,ROCK)) => Random.shuffle(List(SCISSORS, ROCK)).head
      case _::SCISSORS::SCISSORS::Nil  if(myLastMoves.takeRight(2) == List(PAPER,PAPER)) => Random.shuffle(List(ROCK, SCISSORS)).head

      case _::ROCK::Nil if(myLastMoves.takeRight(1) == List(SCISSORS)) => Random.shuffle(List(ROCK, PAPER)).head
      case _::PAPER::Nil  if(myLastMoves.takeRight(1) == List(ROCK))=> Random.shuffle(List(PAPER, SCISSORS)).head
      case _::SCISSORS::Nil if(myLastMoves.takeRight(1) == List(PAPER))=> Random.shuffle(List(SCISSORS, ROCK)).head

      case _ => getDefaultMove()
    }

  }

  private def allCondition(opponentLastMoves :  List[String], myLastMoves :  List[String], randomNumber:Int): Boolean ={
    sizeExists(opponentLastMoves, myLastMoves,randomNumber) && lastNItemSame(opponentLastMoves, myLastMoves,randomNumber)
  }
  
  private def getLastSameMove(opponentLastMoves :  List[String]): String = {

    opponentLastMoves.last match {
      case `ROCK` => Random.shuffle(List(ROCK, PAPER)).head
      case `PAPER` => Random.shuffle(List(PAPER, SCISSORS)).head
      case `SCISSORS` => Random.shuffle(List(SCISSORS, ROCK)).head
      case _ => getDefaultMove()
    }
  }

  private def getLastTwoSameMove(opponentLastMoves :  List[String]): String = {

    opponentLastMoves.takeRight(2) match {
      case List(ROCK,ROCK) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(ROCK,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(ROCK,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head
      case List(PAPER,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(PAPER,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(PAPER,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head
      case List(SCISSORS,ROCK) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(SCISSORS,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(SCISSORS,SCISSORS) => Random.shuffle(List(ROCK, PAPER)).head
      case _ => getDefaultMove()
    }
  }

  private def getLastThreeSameMove(opponentLastMoves :  List[String]): String = {

    opponentLastMoves.takeRight(3) match {
      case List(ROCK,ROCK,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(ROCK,ROCK,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(ROCK,ROCK,SCISSORS) => Random.shuffle(List(SCISSORS, PAPER)).head
      case List(ROCK,PAPER,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(ROCK,PAPER,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(ROCK,PAPER,SCISSORS) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case List(ROCK,SCISSORS,ROCK) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case List(ROCK,SCISSORS,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(ROCK,SCISSORS,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head

      case List(PAPER,ROCK,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(PAPER,ROCK,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(PAPER,ROCK,SCISSORS) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case List(PAPER,PAPER,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(PAPER,PAPER,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(PAPER,PAPER,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head
      case List(PAPER,SCISSORS,ROCK) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case List(PAPER,SCISSORS,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(PAPER,SCISSORS,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head

      case List(SCISSORS,ROCK,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(SCISSORS,ROCK,PAPER) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case List(SCISSORS,ROCK,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head
      case List(SCISSORS,PAPER,ROCK) => Random.shuffle(List(ROCK, PAPER, SCISSORS)).head
      case List(SCISSORS,PAPER,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(SCISSORS,PAPER,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head
      case List(SCISSORS,SCISSORS,ROCK) => Random.shuffle(List(ROCK, PAPER)).head
      case List(SCISSORS,SCISSORS,PAPER) => Random.shuffle(List(PAPER, SCISSORS)).head
      case List(SCISSORS,SCISSORS,SCISSORS) => Random.shuffle(List(SCISSORS, ROCK)).head
      case _ => getDefaultMove()
    }
  }

  private def sizeExists(opponentLastMoves :  List[String], myLastMoves :  List[String], size:Int): Boolean ={
    opponentLastMoves.size>=size && myLastMoves.size>=size
  }

  private def lastNItemSame(opponentLastMoves :  List[String], myLastMoves :  List[String], size:Int): Boolean ={
    opponentLastMoves.takeRight(size) == myLastMoves.takeRight(size)
  }

  private def oppositionDynamiteCount(lastMoves :  List[String]) : Int = {

    if(lastMoves.nonEmpty)
      lastMoves.filter(_==DYNAMITE).size
    else 0

  }

}

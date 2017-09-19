package services

import javax.inject.Inject

import model.{rbsinfo, lastMove}
import play.api.cache.CacheApi
import scala.concurrent.duration._


trait rbsCache {
  def saveOpponentInfo(info : rbsinfo)
  def getOpponentInfo() : Option[rbsinfo]
  def updateOpponentLastMove(move : String)
  def getOpponentLastMove() : Option[List[String]]
  def updateMyLastMove(move : String)
  def getMyLastMove() : Option[List[String]]
  def resetCache()
}


class CachingService@Inject()(cache: CacheApi) extends rbsCache {

  def saveOpponentInfo(info : rbsinfo) = {

    val cacheInfo = cache.get[rbsinfo]("opponentInfo")
    if(cacheInfo.isDefined) {
      cache.set("opponentInfo", cacheInfo.get.copy(info.opponentName, info.pointsToWin, info.maxRounds, info.dynamiteCount), 5.minutes)
    }  else {
      cache.set("opponentInfo", info, 5.minutes)
    }
  }

  override def getOpponentInfo(): Option[rbsinfo] = {
    cache.get[rbsinfo]("opponentInfo")
  }

  override def getOpponentLastMove(): Option[List[String]] = {
    cache.get[List[String]]("opponentLastMove")
  }

  override def updateOpponentLastMove(move: String): Unit = {

    val lastMoves = cache.getOrElse("opponentLastMove")(List[String]())

    cache.set("opponentLastMove", lastMoves:::List(move), 5.minutes)

  }

  override def getMyLastMove(): Option[List[String]] = {
    cache.get[List[String]]("myLastMove")
  }

  override def updateMyLastMove(move: String): Unit = {

    val lastMoves = cache.getOrElse("myLastMove")(List[String]())

    cache.set("myLastMove", lastMoves:::List(move), 5.minutes)

  }

  override def resetCache(): Unit = {
    cache.set("opponentInfo", List(), 5.minutes)
    cache.set("opponentLastMove", List(), 5.minutes)
    cache.set("myLastMove", List(), 5.minutes)
  }
}
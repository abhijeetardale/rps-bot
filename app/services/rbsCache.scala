package services

import javax.inject.Inject

import model.{rbsinfo, lastMove}
import play.api.cache.CacheApi
import scala.concurrent.duration._


trait rbsCache {

  def upateLastMove(move : lastMove)
  def saveOpponentInfo(info : rbsinfo)
  def getOpponentInfo() : Option[rbsinfo]
  def getOpponentLastMove() : Option[List[lastMove]]
}


class CachingService@Inject()(cache: CacheApi) extends rbsCache {

  def upateLastMove(move : lastMove) = {

    val lastMoves = cache.getOrElse("lastMove")(List[lastMove]())

    cache.set("lastMove", lastMoves:::List(move), 5.minutes)

  }

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

  override def getOpponentLastMove(): Option[List[lastMove]] = {
    cache.get[List[lastMove]]("lastMove")
  }

}
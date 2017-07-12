package services

import model.{rbsinfo, lastMove}
import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneAppPerTest}
import play.api.Application
import play.api.cache.Cache
import play.api.cache.{Cache, CacheApi}
import play.cache.Cache

class rbsCacheSpec extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfter{

  val cacheApi = app.injector.instanceOf(classOf[CacheApi])
  val rbsCacheObj = new CachingService(cacheApi)


  "rbsCacheSpec calling upateLastMove" must {

    "update the list with 1st element" in {
      rbsCacheObj.upateLastMove(lastMove("ROCK"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK")))
    }

    "update the list with 2nd element" in {
      rbsCacheObj.upateLastMove(lastMove("PAPER"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK"),lastMove("PAPER")))
    }

    "update the list with 3rd element" in {
      rbsCacheObj.upateLastMove(lastMove("SCISSORS"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS")))
    }

    "update the list with 4th element" in {
      rbsCacheObj.upateLastMove(lastMove("DYNAMITE"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS"),lastMove("DYNAMITE")))
    }

    "update the list with 5th element" in {
      rbsCacheObj.upateLastMove(lastMove("WATERBOMB"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS"),lastMove("DYNAMITE"),lastMove("WATERBOMB")))
    }
  }

  "rbsCacheSpec calling saveOpponentInfo" must {

    "save the Opponent Info" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.saveOpponentInfo(info)
      cacheApi.get[rbsinfo]("opponentInfo") mustBe Some(info)
    }

    "update the Opponent Info if already present" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      val infoUpdated = rbsinfo("Tiger", 50, 100, 50)
      cacheApi.get[rbsinfo]("opponentInfo") mustBe Some(info)
      rbsCacheObj.saveOpponentInfo(infoUpdated)
      cacheApi.get[rbsinfo]("opponentInfo") mustBe Some(infoUpdated)
    }
  }

  "rbsCacheSpec calling getOpponentInfo" must {

    "get the None if info not present" in {
      cacheApi.remove("opponentInfo")
      rbsCacheObj.getOpponentInfo() mustBe None
    }
    
    "get the Opponent Info if present" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.saveOpponentInfo(info)
      rbsCacheObj.getOpponentInfo() mustBe Some(info)
    }
  }

  "rbsCacheSpec calling getOpponentLastMove" must {

    "get the None if info not present" in {
      cacheApi.remove("lastMove")
      rbsCacheObj.getOpponentLastMove() mustBe None
    }

    "get the Opponent Info if present" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.upateLastMove(lastMove("WATERBOMB"))
      rbsCacheObj.getOpponentLastMove() mustBe Some(List(lastMove("WATERBOMB")))
    }

    "get the Opponent Info if list present" in {
      cacheApi.remove("lastMove")
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.upateLastMove(lastMove("ROCK"))
      rbsCacheObj.upateLastMove(lastMove("PAPER"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK"),lastMove("PAPER")))
      rbsCacheObj.upateLastMove(lastMove("SCISSORS"))
      cacheApi.get("lastMove") mustBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS")))
      rbsCacheObj.getOpponentLastMove() mustBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS")))
    }
  }
}

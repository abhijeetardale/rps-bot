package services

import model.{rbsinfo, lastMove}
import org.scalatest.{WordSpec, Matchers, BeforeAndAfter}
import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneAppPerTest}
import play.api.Application
import play.api.cache.Cache
import play.api.cache.{Cache, CacheApi}
import play.cache.Cache

class rbsCacheSpec extends WordSpec with Matchers with GuiceOneAppPerSuite with BeforeAndAfter{

  val cacheApi = app.injector.instanceOf(classOf[CacheApi])
  val rbsCacheObj = new CachingService(cacheApi)


  "rbsCacheSpec calling upateLastMove" should {

    "update the list with 1st element" in {
      rbsCacheObj.updateOpponentLastMove(lastMove("ROCK"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK")))
    }

    "update the list with 2nd element" in {
      rbsCacheObj.updateOpponentLastMove(lastMove("PAPER"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER")))
    }

    "update the list with 3rd element" in {
      rbsCacheObj.updateOpponentLastMove(lastMove("SCISSORS"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS")))
    }

    "update the list with 4th element" in {
      rbsCacheObj.updateOpponentLastMove(lastMove("DYNAMITE"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS"),lastMove("DYNAMITE")))
    }

    "update the list with 5th element" in {
      rbsCacheObj.updateOpponentLastMove(lastMove("WATERBOMB"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS"),lastMove("DYNAMITE"),lastMove("WATERBOMB")))
    }
  }

  "rbsCacheSpec calling saveOpponentInfo" should {

    "save the Opponent Info" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.saveOpponentInfo(info)
      cacheApi.get[rbsinfo]("opponentInfo") shouldBe Some(info)
    }

    "update the Opponent Info if already present" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      val infoUpdated = rbsinfo("Tiger", 50, 100, 50)
      cacheApi.get[rbsinfo]("opponentInfo") shouldBe Some(info)
      rbsCacheObj.saveOpponentInfo(infoUpdated)
      cacheApi.get[rbsinfo]("opponentInfo") shouldBe Some(infoUpdated)
    }
  }

  "rbsCacheSpec calling getOpponentInfo" should {

    "get the None if info not present" in {
      cacheApi.remove("opponentInfo")
      rbsCacheObj.getOpponentInfo() shouldBe None
    }
    
    "get the Opponent Info if present" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.saveOpponentInfo(info)
      rbsCacheObj.getOpponentInfo() shouldBe Some(info)
    }
  }

  "rbsCacheSpec calling getOpponentLastMove" should {

    "get the None if info not present" in {
      cacheApi.remove("lastMove")
      rbsCacheObj.getOpponentLastMove() shouldBe None
    }

    "get the Opponent Info if present" in {
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.updateOpponentLastMove(lastMove("WATERBOMB"))
      rbsCacheObj.getOpponentLastMove() shouldBe Some(List(lastMove("WATERBOMB")))
    }

    "get the Opponent Info if list present" in {
      cacheApi.remove("lastMove")
      val info = rbsinfo("Tiger", 100, 200, 100)
      rbsCacheObj.updateOpponentLastMove(lastMove("ROCK"))
      rbsCacheObj.updateOpponentLastMove(lastMove("PAPER"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER")))
      rbsCacheObj.updateOpponentLastMove(lastMove("SCISSORS"))
      cacheApi.get("lastMove") shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS")))
      rbsCacheObj.getOpponentLastMove() shouldBe Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS")))
    }
  }

  "rbsCacheSpec calling updateDynamiteCount" should {

    "set to 1 if no Dynamite used" in {
      cacheApi.remove("dynamiteCount")
      rbsCacheObj.updateDynamiteCount
      cacheApi.get("dynamiteCount") shouldBe Some(1)
    }

    "set to next number if Dynamite used again" in {
      cacheApi.remove("dynamiteCount")
      rbsCacheObj.updateDynamiteCount
      rbsCacheObj.updateDynamiteCount
      cacheApi.get("dynamiteCount") shouldBe Some(2)
    }
  }

  "rbsCacheSpec calling updateDynamiteCount" should {

    "get 0 if no Dynamite used" in {
      cacheApi.remove("dynamiteCount")
      rbsCacheObj.getDynamiteCount shouldBe 0
    }

    "get 2 if Dynamite used 2 times " in {
      cacheApi.remove("dynamiteCount")
      rbsCacheObj.updateDynamiteCount
      rbsCacheObj.updateDynamiteCount
      rbsCacheObj.getDynamiteCount shouldBe 2
    }
  }
}

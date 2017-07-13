package helper

import model.lastMove
import org.scalatest.{Entry, Matchers, WordSpec}

class MoveHelperSpec extends WordSpec with Matchers {

  val helper = new MoveHelper

  "calling MoveHelper for oppositionDynamiteCount" should {

    "return 0 if last moves are not defined" in {

      helper.oppositionDynamiteCount(None) shouldBe 0
    }

    "return 1 if last moves contains 1 DYNAMITE" in {

      helper.oppositionDynamiteCount(Some(List(lastMove("ROCK"),lastMove("PAPER"),lastMove("SCISSORS"),lastMove("DYNAMITE"),lastMove("WATERBOMB")))) shouldBe 1
    }

    "return 2 if last moves contains 1 DYNAMITE" in {

      helper.oppositionDynamiteCount(Some(List(lastMove("DYNAMITE"),lastMove("PAPER"),lastMove("SCISSORS"),lastMove("DYNAMITE"),lastMove("WATERBOMB")))) shouldBe 2
    }
  }


  "calling MoveHelper for getMove" should {

    "return first Move if last moves is not defined" in {

      val result = helper.getMove(None)
      Some(result) should contain oneOf ("ROCK","PAPER", "SCISSORS", "DYNAMITE","WATERBOMB")
    }

    "return first Move if last moves is not defined and result should not be same for multiple calls" in {

      val result1 = helper.getMove(None)
      val result2 = helper.getMove(None)

      Some(result1) should contain oneOf ("ROCK","PAPER", "SCISSORS", "DYNAMITE","WATERBOMB")
      Some(result2) should contain oneOf ("ROCK","PAPER", "SCISSORS", "DYNAMITE","WATERBOMB")

      result1 should not be result2

    }
  }
}

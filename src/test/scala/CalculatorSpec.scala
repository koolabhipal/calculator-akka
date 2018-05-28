import Calculator.{Add, Divide, Multiply, Subtract}
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class CalculatorSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Calculator" must {

    "add and update state" in {
      val testProbe = TestProbe()
      val calculator = TestActorRef[Calculator]
      calculator ! Add(5)
      calculator ! Add(5)
      calculator.underlyingActor.state.result shouldBe 10.0
    }

    "subtract and update state" in {
      val testProbe = TestProbe()
      val calculator = TestActorRef[Calculator]
      calculator ! Add(5)
      calculator ! Subtract(2)
      calculator.underlyingActor.state.result shouldBe 3.0
    }

    "multiply and update state" in {
      val testProbe = TestProbe()
      val calculator = TestActorRef[Calculator]
      calculator ! Add(5)
      calculator ! Multiply(9)
      calculator.underlyingActor.state.result shouldBe 45.0
    }

    "divide and update state" in {
      val testProbe = TestProbe()
      val calculator = TestActorRef[Calculator]
      calculator ! Add(10)
      calculator ! Divide(5)
      calculator.underlyingActor.state.result shouldBe 2.0
    }

    "complete series of expressions" in {
      val testProbe = TestProbe()
      val calculator = TestActorRef[Calculator]
      calculator ! Add(10)
      calculator ! Divide(5)
      calculator ! Multiply(3)
      calculator ! Add(55)
      calculator ! Subtract(9)
      calculator ! Multiply(12)
      calculator.underlyingActor.state.result shouldBe 624.0
    }


  }

}

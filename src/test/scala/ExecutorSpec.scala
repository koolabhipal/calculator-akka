import Calculator.{Add, Divide, Multiply, Subtract}
import Main.Calculate
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ExecutorSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  "Executor" must {

    val testProbe = TestProbe()
    val executor = TestActorRef(new Executor(system = system) {
      override def createCalculator: ActorRef = testProbe.ref
    })

    "parse and send expressions for calculation" in {

      executor ! Calculate("5+7*5/2-5=")
      testProbe.expectMsg(Add(5))
      testProbe.expectMsg(Add(7))
      testProbe.expectMsg(Multiply(5))
      testProbe.expectMsg(Divide(2))
      testProbe.expectMsg(Subtract(5))

    }
  }


}

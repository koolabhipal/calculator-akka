import Calculator._
import Main.Calculate
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class Executor(system: ActorSystem) extends Actor {

  val operators = List("+", "-", "*", "/", "=")

  def createCalculator: ActorRef = system.actorOf(Props[Calculator])

  val calculator: ActorRef = createCalculator

  override def receive: Receive = {
    case Calculate(input) =>

      if (input.contains('C') || input.contains('c')) {
        calculator ! Clear
      }
      evaluateFromInputString(input, calculator)
      if (!input.endsWith("=")) {
        print(input + "\n")
        calculator ! Clear
      }
  }

  private def evaluateFromInputString(input: String, calculator: ActorRef): Unit = {
    try {
      val firstOperatorIndex = getIndexOfFirstOperator(input.substring(0, input.length))
      input.charAt(0) match {
        case '+' =>
          val nextNumberLiteral = input.substring(1, firstOperatorIndex.getOrElse(input.length)).toDouble
          calculator ! Add(nextNumberLiteral)
        case '-' =>
          val nextNumberLiteral = input.substring(1, firstOperatorIndex.getOrElse(input.length)).toDouble
          calculator ! Subtract(nextNumberLiteral)
        case '*' =>
          val nextNumberLiteral = input.substring(1, firstOperatorIndex.getOrElse(input.length)).toDouble
          calculator ! Multiply(nextNumberLiteral)
        case '/' =>
          val nextNumberLiteral = input.substring(1, firstOperatorIndex.getOrElse(input.length)).toDouble
          calculator ! Divide(nextNumberLiteral)
        case '=' => calculator ! PrintResult; return
        case _ =>
          val nextNumberLiteral = input.substring(0, firstOperatorIndex.getOrElse(input.length)).toDouble
          calculator ! Add(nextNumberLiteral)
      }
      if (firstOperatorIndex.isDefined && firstOperatorIndex.get < input.length) {
        evaluateFromInputString(input.substring(firstOperatorIndex.get, input.length), calculator)
      }
    } catch {
      case _: Throwable => calculator ! PrintError("Incorrect input")
    }
  }

  def getIndexOfFirstOperator(input: String): Option[Int] = {
    val operatorIndexes = operators.flatMap(getAllTheIndexesOfOperator(_, input, 0)).filter(index => index != -1 && index != 0)
    if (operatorIndexes.isEmpty) {
      return None
    }
    Some(operatorIndexes.min)
  }

  def getAllTheIndexesOfOperator(operator: String, input: String, startPositionToScan: Int): List[Int] = {
    val index = input.indexOf(operator, startPositionToScan)
    if (index == -1) {
      return List.empty
    }
    index :: getAllTheIndexesOfOperator(operator, input, index + 1)
  }

}
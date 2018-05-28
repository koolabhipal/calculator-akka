import akka.actor._

object Calculator {

  sealed trait Command

  case object Clear extends Command

  case class Add(value: Double) extends Command

  case class Subtract(value: Double) extends Command

  case class Divide(value: Double) extends Command

  case class Multiply(value: Double) extends Command

  case class PrintError(message: String) extends Command

  case object PrintResult extends Command

  case class CalculationResult(result: Double = 0) {
    def reset: CalculationResult = copy(result = 0)

    def add(value: Double): CalculationResult = copy(result = this.result + value)

    def subtract(value: Double): CalculationResult = copy(result = this.result - value)

    def divide(value: Double): CalculationResult = copy(result = this.result / value)

    def multiply(value: Double): CalculationResult = copy(result = this.result * value)
  }

}

class Calculator extends Actor {

  import Calculator._

  var state = CalculationResult()

  val receive: Receive = {
    case Add(value) => state = state.add(value)
    case Subtract(value) => state = state.subtract(value)
    case Divide(value) => if (value != 0) state = state.divide(value) else sender ! PrintError("Divide by zero is obviously not possible")
    case Multiply(value) => state = state.multiply(value)
    case PrintResult => print(s"${state.result}\n"); state = state.reset
    case Clear => state = state.reset; print("\033[2J")
  }
}

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

object Main extends App {
  val system = ActorSystem("CalculatorSystem")

  val executor = system.actorOf(Props(new Executor(system)))

  while (true) {
    val input = StdIn.readLine()

    executor ! Calculate(input)

  }

  case class Calculate(input: String)

}
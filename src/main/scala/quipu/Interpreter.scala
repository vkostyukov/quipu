/*
 * Copyright 2013, by Vladimir Kostyukov and Contributors.
 *
 * This file is part of Quipu project (https://github.com/vkostyukov/quipu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor(s): -
 *
 */

package quipu

class InterpreterException(message: String) extends Exception(message)

trait Interpreter {
  def interpret()
}
abstract class AbstractInterpreter(threads: Map[Char, Thread], labels: List[Char] ) extends Interpreter

class CaseInterpreter(threads: Map[Char, Thread], labels: List[Char])
  extends AbstractInterpreter(threads, labels) {

  def interpret(){
    var pointer = 0
    var halted = labels.length > 0

    while (!halted) { // over threads
      val thread = threads(labels(pointer))

      var stack: List[Any] = if (thread.initialized) thread.main(0) match {
        case NumberKnot(n) => 1 :: Nil
        case StringKnot(s) => s :: Nil
      } else (() => resolveThreadValue(thread)) :: Nil // lazy initialization (have no idea how does it work)

      var knots: List[Knot] = thread.main.tail // skips the init knot
      var jumped = false
      var finished = knots.length > 0
      while (!halted && !finished && !jumped) { // over knots
        knots.head match {
          case VarKnot(c) => stack = resolveThreadValue(c) :: stack
          case NumberKnot(n) => stack = n :: stack
          case StringKnot(s) => stack = s :: stack
          case OperationKnot(fn) =>
            try {
              stack = fn(stack(1), stack(0)) :: stack
            } catch {
              case e: IndexOutOfBoundsException => throw new InterpreterException("a")
            }
          case JumpKnot(c, p) =>
            if (p(stack(0))) {
              jumped = true
              pointer = resolveJump(c)
            }
          case InKnot() =>
            val str = Console.readLine()
            try {
              stack = str.toInt :: stack
            } catch {
              case e: NumberFormatException => stack = str :: stack
            }
          case OutKnot() => Console.println(stack(0))
          case HaltKnot() => halted = true
        }
        knots = knots.tail
        finished = (knots == Nil)
      }

      if (!jumped && !halted) {
        if (pointer + 1 == labels.length) {
          halted = true
        } else {
          pointer = pointer + 1
        }
      }

      if (!halted) {
        thread.main = valueToKnot(stack(0)) :: thread.main.tail
      }
    }
  }

  private def initialize(thread: Thread) {
    if (!thread.initialized) {

      var stack: List[Any] = 0 :: Nil

      thread.init.tail foreach { knot => knot match {
        case VarKnot(c) => stack = resolveThreadValue(c) :: stack
        case NumberKnot(n) => stack = n :: stack
        case StringKnot(s) => stack = s :: stack
        case OperationKnot(fn) =>
          try {
            stack = fn(stack(1), stack(0)) :: stack
          } catch {
            case e: IndexOutOfBoundsException => throw new InterpreterException("a")
          }
        case InKnot() =>
          val str = Console.readLine()
          try {
            stack = str.toInt :: stack
          } catch {
            case e: NumberFormatException => stack = str :: stack
          }
        case OutKnot() => Console.println(stack(0))
        case _ => throw new IllegalArgumentException("Not supported in init thread")
      }}

      thread.main = valueToKnot(stack(0)) :: thread.main.tail

      thread.initialized = true
    }
  }

  private def resolveThreadValue(c: Char): Any = {
    if (labels.contains(c)) {
      return resolveThreadValue(threads(c))
    } else {
      throw new InterpreterException("No such thread '" + c + "'.")
    }
  }

  private def resolveThreadValue(t: Thread): Any = {
    initialize(t)
    return t.main(0) match {
      case NumberKnot(n) => n
      case StringKnot(s) => s
    }
  }

  private def resolveJump(c: Char): Int = {
    if (labels.contains(c)) return labels.indexOf(c)
    throw new InterpreterException("No such thread '" + c + "'.")
  }

  private def valueToKnot(v: Any): Knot = v match {
    case i: Int => new NumberKnot(i)
    case s: String => new StringKnot(s)
  }
}

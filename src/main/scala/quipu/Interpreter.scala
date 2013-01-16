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

object Interpreter {

  def apply(code: Array[Array[Knot]]) {

    def resolveJump(n: Any): Int = n match {
      case i: Int if (i < code.length) => i
      case _  => throw new InterpreterException("No such thread: \"" + n + "\".")
    }

    var pointer = 0
    var halted = code.length == 0

    while (!halted) { // over threads
      val thread = code(pointer)

      var stack: List[Any] = thread(0) match {
        case NumberKnot(n) => n :: Nil
        case StringKnot(s) => s :: Nil
      }

      var knots: List[Knot] = thread.toList.tail // skips the self knot
      var jumped = false
      var finished = knots.length == 0

      while (!halted && !finished && !jumped) { // over knots
        knots.head match {
          case ReferenceKnot =>
            val ref = stack(0) match {
              case i: Int if (i < code.length) => code(i)(0) match {
                case NumberKnot(n) => n
                case StringKnot(s) => s
              }
              case _  => throw new InterpreterException("No such thread: \"" + stack(0) + "\".")
            }
            stack = ref :: stack.tail
          case NumberKnot(n) => stack = n :: stack
          case StringKnot(s) => stack = s :: stack
          case SelfKnot => stack = stack.last :: stack
          case CopyKnot => stack = stack(0) :: stack
          case OperationKnot(fn) =>
            try {
              (stack(1), stack(0)) match {
                case (a: Int, b: Int) => stack = fn(a, b) :: stack
                case _ => throw new InterpreterException("Type missmatch.")
              }
            } catch {
              case e: IndexOutOfBoundsException =>
                throw new InterpreterException("Not enough arguments for operation.")
            }
          case ConditionalJumpKnot(p) =>
            val target = stack(0)
            stack = stack.tail
            stack(0) match {
              case i: Int =>
                if (p(i)) {
                  jumped = true
                  pointer = resolveJump(target)
                }
              case _ => throw new InterpreterException("!")
            }
          case JumpKnot =>
            val target = stack(0)
            stack = stack.tail
            jumped = true
            pointer = resolveJump(target)
          case InKnot =>
            val str = Console.readLine()
            try {
              stack = str.toInt :: stack
            } catch {
              case e: NumberFormatException => stack = str :: stack
            }
          case OutKnot => Console.print(stack(0))
          case HaltKnot => halted = true
        }

        knots = knots.tail
        finished = (knots == Nil)
      }

      if (!jumped && !halted) {
        if (pointer + 1 == code.length) {
          halted = true
        } else {
          pointer = pointer + 1
        }
      }

      if (!halted) {
        thread(0) = stack(0) match {
          case i: Int => new NumberKnot(i)
          case s: String => new StringKnot(s)
        }
      }
    }
  }
}

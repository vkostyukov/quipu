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

object Main extends App {

  val usage = "Use following command to run Quipu programm:\n" +
    "java -jar quipu-0.0.1.jar programm.qp\n"

  if (args.length > 0) {
    try {
      val (code, labels) = new BufferedParser(
        scala.io.Source.fromFile(args(0))
      ).parse()
      new CaseInterpreter(code, labels).interpret()
    } catch {
      case pe: ParserException => println("Parser error: " + pe.getMessage)
      case ie: InterpreterException => println("Interpreter error: " + ie.getMessage)
    }
  } else {
    print(usage)
  }
}

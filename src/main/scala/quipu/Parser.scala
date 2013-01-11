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

import io.Source

class ParserException(message: String) extends Exception(message)

trait Parser {
  def parse(): (Map[Char, Thread], List[Char])
}

abstract class AbstractParser(source: Source) extends Parser

class BufferedParser(source: Source) extends AbstractParser(source) {

  def parse(): (Map[Char, Thread], List[Char]) = {
    var threads: Map[Char, Thread] = (Nil -> Nil)
    var labels: List[Char] = Nil

    var knots: List[AnyRef] = Nil
    var starts: List[Int] = Nil

    val lines = source.getLines

    // perform the first line
    if (lines.hasNext) {
      val index = 0
      val first: Seq[Char] = lines.next()
      while (index < first.length) {

        while (first(index).isWhitespace) index++

        if (first(index).isLetter) {
          val label = first(index++)
          val t = if (threads.contains(label)) threads(label) else new Thread

          threads += (label -> t)
          if (first(index) == ':') {
            knots = knots ::: t.init
          } else if (first(index) == '.') {
            knots = knots ::: t.main
            labels = labels ::: label
          } else {
            throw new ParserException()
          }
          starts = starts ::: ++index
        }
      }
    }

    // perform the rest lines
    lines foreach { line =>
      var seq: Seq[Char] = line
//      starts foreach { i =>
//        seq(i) match {
//          case Seq('\'', c) =>
//          case Seq('\\', c) =>
//        }
      }
    }

    source.close()

    return (threads, labels)
    // threads += ('a' -> 10)
  }
}
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
import collection.mutable.ListBuffer

class ParserException(message: String) extends Exception(message)

trait Parser {
  def parse(): (Map[Char, Thread], List[Char])
}

abstract class AbstractParser(source: Source) extends Parser

class BufferedParser(source: Source) extends AbstractParser(source) {

  def parse(): (Map[Char, Thread], List[Char]) = {
    var threads: Map[Char, Thread] = Map()
    var labels: List[Char] = Nil

    var knots: Array[String] = new Array(52) // Sorry guys :(
    var idents: List[Int] = Nil

    val lines = source.getLines

    // perform the first line
    if (lines.hasNext) {
      var index = 0
      var cur = 0
      val first: Seq[Char] = lines.next()
      while (index < first.length) {

        while (first(index).isWhitespace) index += 1

        if (first(index).isLetter) {

          idents = idents :+ index

          val label: Char = first(index)
          index += 1
          val t = if (threads.contains(label)) threads(label) else new Thread

          threads += (label -> t)
          if (first(index) == ':') {
            knots.update(cur, label.toString + ":")
            //knots(cur) = t.init
          } else if (first(index) == '.') {
            knots.update(cur, label.toString + ".")
            labels = labels :+ label
          } else {
            throw new ParserException("A")
          }
          cur += 1
          index += 1
        } else {
          // TODO! no labels
        }
      }
    }

    // adds empty threads (only with init part)
    idents.indices foreach { i =>
      if (!labels.contains(knots(i).head)) {
        labels = labels :+ knots(i).head
      }
    }

    val strBuffer = Array.fill(idents.length) {""}
    val intBuffer = Array.fill(idents.length) {-1}

    // perform the rest lines

    lines foreach { lines =>
      var seq: Seq[Char] = lines
      idents.indices foreach { i =>
        val idnt = idents(i)
        val lbl = knots(i)
        var thr = threads(lbl.head)
        if (seq.length > idnt + 1) {
          seq(idnt) match {
            case '\'' => strBuffer(i) += seq(idnt + 1)
            case '\\' =>
              seq(idnt + 1) match {
                case 'n' => strBuffer(i) += '\n'
                case 't' => strBuffer(i) += '\t'
             }
            case '$' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new VarKnot(seq(idnt + 1))
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new VarKnot(seq(idnt + 1))
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case c: Char if c.isDigit =>
              seq(idnt + 1) match {
                case '&' =>
                  if (intBuffer(i) == -1) {
                    intBuffer(i) = 0
                  }
                  intBuffer(i) += c.toString.toInt
                case '@' =>
                  if (intBuffer(i) == -1) {
                    intBuffer(i) = 0
                  }
                  intBuffer(i) += c.toString.toInt * 10
                case '%' =>
                  if (intBuffer(i) == -1) {
                    intBuffer(i) = 0
                  }
                  intBuffer(i) += c.toString.toInt * 100
                case '#' =>
                  if (intBuffer(i) == -1) {
                    intBuffer(i) = 0
                  }
                  intBuffer(i) += c.toString.toInt * 1000
               }
            case '+' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x + y
                  case (x: String, y: String) => x + y
                  case (x: Int, y: String) => x + y
                  case (x: String, y: Int) => x + y
                })
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x + y
                  case (x: String, y: String) => x + y
                  case (x: Int, y: String) => x + y
                  case (x: String, y: Int) => x + y
                })
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case '-' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x - y
                  case (_, _) => throw new ParserException("Can't")
                })
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x - y
                  case (_, _) => throw new ParserException("Can't")
                })
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case '*' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x * y
                  case (_, _) => throw new ParserException("Can't")
                })
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x * y
                  case (_, _) => throw new ParserException("Can't")
                })
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case '/' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x / y
                  case (_, _) => throw new ParserException("Can't")
                })
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x / y
                  case (_, _) => throw new ParserException("Can't")
                })
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case '%' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x % y
                  case (_, _) => throw new ParserException("Can't")
                })
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new OperationKnot((x: Any, y: Any) => (x, y) match {
                  case (x: Int, y: Int) => x % y
                  case (_, _) => throw new ParserException("Can't")
                })
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case '<' =>
              seq(idnt + 1) match {
                case c: Char if c.isLetter =>
                  if (lbl.last == '.') {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                    thr.main += new JumpKnot(c, (x) => x match {
                      case x: Int => x < 0
                      case _ => throw new ParserException("Can't")
                    })
                  } else {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                    thr.init += new JumpKnot(c, (x) => x match {
                      case x: Int => x < 0
                      case _ => throw new ParserException("Can't")
                    })
                  }
                  intBuffer(i) = -1
                  strBuffer(i) = ""
                case _ =>
                  if (lbl.last == '.') {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                    thr.main += new OutKnot
                  } else {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                    thr.init += new OutKnot
                  }
                  intBuffer(i) = -1
                  strBuffer(i) = ""
              }
            case '?' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new JumpKnot(seq(idnt + 1), (x) => true)
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new JumpKnot(seq(idnt + 1), (x) => true)
              }
              intBuffer(i) = -1
              strBuffer(i) = ""
            case '>' =>
              seq(idnt + 1) match {
                case c: Char if c.isLetter =>
                  if (lbl.last == '.') {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                    thr.main += new JumpKnot(seq(idnt + 1), (x) => x match {
                      case x: Int => x > 0
                      case _ => throw new ParserException("Can't")
                    })
                  } else {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                    thr.init += new JumpKnot(seq(idnt + 1), (x) => x match {
                      case x: Int => x > 0
                      case _ => throw new ParserException("Can't")
                    })
                  }
                  intBuffer(i) = -1
                  strBuffer(i) = ""
                case _ =>
                  if (lbl.last == '.') {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                    thr.main += new InKnot
                  } else {
                    dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                    thr.init += new InKnot
                  }
                  intBuffer(i) = -1
                  strBuffer(i) = ""
              }
            case '=' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
                thr.main += new JumpKnot(seq(idnt + 1), (x) => x match {
                  case x: Int => x == 0
                  case _ => throw new ParserException("Can't")
                })
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
                thr.init += new JumpKnot(seq(idnt + 1), (x) => x match {
                  case x: Int => x == 0
                  case _ => throw new ParserException("Can't")
                })
              }
            case ':' =>
              if (lbl.last == '.') {
                thr.main += new HaltKnot
              } else {
                thr.init += new HaltKnot
              }
            case ' ' =>
              if (lbl.last == '.') {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
              } else {
                dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
              }
              strBuffer(i) = ""
              intBuffer(i) = -1
          }
        }
      }
    }

    // dump all buffers
    idents.indices foreach { i =>
      val lbl = knots(i)
      val thr = threads(lbl.head)

      if (lbl.last == '.') {
        dumpBuffers(intBuffer(i), strBuffer(i), thr.main)
      } else {
        dumpBuffers(intBuffer(i), strBuffer(i), thr.init)
      }
    }

    source.close()

    return (threads, labels)
  }

  private def dumpBuffers(i: Int, s: String, thr: ListBuffer[Knot]) {
    if (i != -1) {
      thr += new NumberKnot(i)
    }
    if (s != "") {
      thr += new StringKnot(s)
    }
  }
}
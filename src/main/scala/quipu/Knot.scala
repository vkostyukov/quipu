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

abstract class Knot

case class VarKnot(thread: Char) extends Knot                       // $a
case class NumberKnot(value: Int) extends Knot                      // 1%2&
case class StringKnot(value: String) extends Knot                   // 'a'b'c'\n

case class OperationKnot(fn: (Any, Any) => Any) extends Knot        // ++, --, **, //, %%

case class JumpKnot(thread: Char, p: (Any) => Boolean) extends Knot // >a, <a, =a, ?a

case class InKnot extends Knot                                      // >>
case class OutKnot extends Knot                                     // <<

case class HaltKnot extends Knot                                    // ::
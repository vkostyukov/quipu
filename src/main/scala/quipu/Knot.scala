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

case class ReferenceKnot extends Knot                               // []
case class NumberKnot(value: Int) extends Knot                      // 1%2&
case class StringKnot(value: String) extends Knot                   // 'a'b'c'\n

case class OperationKnot(fn: (Int, Int) => Int) extends Knot        // ++, --, **, //, %%

case class ConditionalJumpKnot(p: (Int) => Boolean) extends Knot    // >>, >=, <<, <=, ==
case class JumpKnot extends Knot                                    // ??

case class InKnot extends Knot                                      // \/
case class OutKnot extends Knot                                     // /\

case class HaltKnot extends Knot                                    // ::

case class SelfKnot extends Knot                                    // ^^
case class CopyKnot extends Knot                                    // ##



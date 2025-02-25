/*
 * Copyright (C) 2023 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.cash.redwood.tooling.schema

import DefaultPackage
import app.cash.redwood.tooling.schema.FqType.Variance.In
import app.cash.redwood.tooling.schema.FqType.Variance.Out
import assertk.assertions.hasMessage
import java.util.PrimitiveIterator
import kotlin.reflect.typeOf
import org.junit.Assert.assertEquals
import org.junit.Test

class FqTypeTest {
  @Test fun classes() {
    assertEquals(FqType(listOf("kotlin", "String")), String::class.toFqType())
    assertEquals(FqType(listOf("app.cash.redwood.tooling.schema", "FqTypeTest")), FqTypeTest::class.toFqType())
    assertEquals(FqType(listOf("java.util", "PrimitiveIterator", "OfInt")), PrimitiveIterator.OfInt::class.toFqType())
    assertEquals(FqType(listOf("", "DefaultPackage")), DefaultPackage::class.toFqType())
    assertEquals(FqType(listOf("", "DefaultPackage", "Nested")), DefaultPackage.Nested::class.toFqType())
  }

  @Test fun classSpecialCases() {
    assertEquals(FqType(listOf("kotlin", "Boolean", "Companion")), Boolean.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Byte", "Companion")), Byte.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Char", "Companion")), Char.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Double", "Companion")), Double.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Enum", "Companion")), Enum.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Float", "Companion")), Float.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Int", "Companion")), Int.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Long", "Companion")), Long.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "Short", "Companion")), Short.Companion::class.toFqType())
    assertEquals(FqType(listOf("kotlin", "String", "Companion")), String.Companion::class.toFqType())
  }

  @Test fun types() {
    assertEquals(
      FqType(listOf("kotlin", "String"), nullable = true),
      typeOf<String?>().toFqType(),
    )

    assertEquals(
      FqType(listOf("kotlin", "Array"), parameterTypes = listOf(FqType(listOf("kotlin", "String")))),
      typeOf<Array<String>>().toFqType(),
    )

    assertEquals(
      FqType(
        names = listOf("kotlin", "Array"),
        parameterTypes = listOf(FqType(listOf("kotlin", "String"), variance = Out)),
      ),
      typeOf<Array<out String>>().toFqType(),
    )

    assertEquals(
      FqType(
        names = listOf("kotlin", "Array"),
        parameterTypes = listOf(FqType(listOf("kotlin", "String"), variance = In)),
      ),
      typeOf<Array<in String>>().toFqType(),
    )

    assertEquals(
      FqType(listOf("kotlin", "Array"), parameterTypes = listOf(FqType.Star)),
      typeOf<Array<*>>().toFqType(),
    )
  }

  @Test fun classTooFewNamesThrows() {
    assertFailsWith<IllegalArgumentException> {
      FqType(listOf())
    }.hasMessage("At least two names are required: package and a simple name: []")

    assertFailsWith<IllegalArgumentException> {
      FqType(listOf("kotlin"))
    }.hasMessage("At least two names are required: package and a simple name: [kotlin]")
  }

  @Test fun star() {
    assertEquals(FqType(listOf("", "*")), FqType.Star)
  }

  @Test fun starOtherPropertiesThrows() {
    assertFailsWith<IllegalArgumentException> {
      FqType(listOf("kotlin", "*"))
    }.hasMessage("Star projection must use empty package name: kotlin")

    assertFailsWith<IllegalArgumentException> {
      FqType(listOf("", "*", "Nested"))
    }.hasMessage("Star projection cannot have nested types: [*, Nested]")

    assertFailsWith<IllegalArgumentException> {
      FqType(listOf("", "*"), parameterTypes = listOf(Int::class.toFqType()))
    }.hasMessage("Star projection must not have parameter types: [kotlin.Int]")

    assertFailsWith<IllegalArgumentException> {
      FqType(listOf("", "*"), variance = In)
    }.hasMessage("Star projection must be Invariant: In")

    assertFailsWith<IllegalArgumentException> {
      FqType(listOf("", "*"), nullable = true)
    }.hasMessage("Star projection must not be nullable")
  }

  @Test fun bestGuessValid() {
    assertEquals(
      FqType(listOf("", "Map")),
      FqType.bestGuess("Map"),
    )
    assertEquals(
      FqType(listOf("", "Map", "Entry")),
      FqType.bestGuess("Map.Entry"),
    )
    assertEquals(
      FqType(listOf("java", "Map", "Entry")),
      FqType.bestGuess("java.Map.Entry"),
    )
    assertEquals(
      FqType(listOf("java.util.concurrent", "Map", "Entry")),
      FqType.bestGuess("java.util.concurrent.Map.Entry"),
    )
  }

  @Test fun bestGuessInvalid() {
    assertFailsWith<IllegalArgumentException> {
      FqType.bestGuess("java")
    }.hasMessage("Couldn't guess: java")

    assertFailsWith<IllegalArgumentException> {
      FqType.bestGuess("java.util.concurrent")
    }.hasMessage("Couldn't guess: java.util.concurrent")

    assertFailsWith<IllegalArgumentException> {
      FqType.bestGuess("java..concurrent")
    }.hasMessage("Couldn't guess: java..concurrent")

    assertFailsWith<IllegalArgumentException> {
      FqType.bestGuess("java.util.concurrent.Map..Entry")
    }.hasMessage("Couldn't guess: java.util.concurrent.Map..Entry")

    assertFailsWith<IllegalArgumentException> {
      FqType.bestGuess("java.util.concurrent.Map.entry")
    }.hasMessage("Couldn't guess: java.util.concurrent.Map.entry")
  }
}

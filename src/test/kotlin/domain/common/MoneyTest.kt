package domain.common

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MoneyTest {

    @Test
    fun `Money(2000)의 amount는 2000이다`() {
        Money(2_000).amount shouldBe 2_000
    }

    @Test
    fun `Money의 Amount 가 음수라면, 예외를 던진다`() {
        shouldThrow<IllegalArgumentException> { Money(-100) }
    }

    @Test
    fun `Money 간 더하기 연산을 수행할 수 있다`() {
        Money(3_000) + Money(2_000) shouldBe Money(5_000)
    }

    @Test
    fun `Money간 빼기 연산을 수행할 수 있다`() {
        Money(3_000) - Money(2_000) shouldBe Money(1_000)
    }

    @Test
    fun `Money에 곱하기 연산을 수행할 수 있다`() {
        Money(2_000) * 2.0 shouldBe Money(4_000)
    }
}

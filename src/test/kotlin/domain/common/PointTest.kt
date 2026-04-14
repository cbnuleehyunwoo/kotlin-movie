package domain.common

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun `Point(1000)의 amount는 1000이다`() {
        Point(1_000).amount shouldBe 1_000
    }

    @Test
    fun `Point의 amount가 음수이면 예외를 던진다`() {
        shouldThrow<IllegalArgumentException> { Point(-100) }
    }

    @Test
    fun `Point(1000)은 Money로 변환할 수 있으며 Money(1000)과 같다`() {
        Point(1_000).toMoney() shouldBe Money(1_000)
    }
}

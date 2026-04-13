package domain.movie

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class RunningTimeTest {
    @Test
    fun `영화의 상영 길이가 0초과일 경우 RunningTime이 생성된다`() {
        val runningTime = RunningTime(duration = 1)
        assertThat(runningTime.duration).isEqualTo(1)
    }

    @Test
    fun `영화의 상영 길이가 0이하일 경우 예외를 던진다`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { RunningTime(duration = 0) }
    }
}

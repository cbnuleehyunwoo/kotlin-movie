package domain.screening

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ScreeningPeriodTest {
    @Test
    fun `시작일이 종료일보다 앞설 경우 ScreeningPeriod가 생성된다`() {
        val startDate = LocalDate.of(2026, 4, 7)
        val endDate = LocalDate.of(2026, 4, 8)

        val screeningPeriod = ScreeningPeriod(
            startDate = startDate,
            endDate = endDate,
        )

        assertThat(screeningPeriod.startDate).isEqualTo(startDate)
        assertThat(screeningPeriod.endDate).isEqualTo(endDate)
    }

    @Test
    fun `종료일이 시작일보다 앞 설 경우 예외를 던진다`() {
        val startDate = LocalDate.of(
            2026,
            4,
            8
        )
        val endDate = LocalDate.of(
            2026,
            4,
            7
        )
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningPeriod(
                startDate = startDate,
                endDate = endDate,
            )
        }
    }
}

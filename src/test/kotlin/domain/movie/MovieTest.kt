package domain.movie

import domain.screening.ScreeningPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MovieTest {
    @Test
    fun `영화는 제목, 상영 길이, 상영 기간을 가진다`() {
        val title = Title(title = "안녕하세요 커피입니다")
        val runningTime = RunningTime(duration = 178)
        val screeningPeriod = ScreeningPeriod(
            startDate = LocalDate.of(2026, 4, 7),
            endDate = LocalDate.of(2026, 4, 8),
        )

        val movie = Movie(
            title = title,
            runningTime = runningTime,
            screeningPeriod = screeningPeriod,
        )
        assertThat(movie.title).isEqualTo(title)
        assertThat(movie.runningTime).isEqualTo(runningTime)
        assertThat(movie.screeningPeriod).isEqualTo(screeningPeriod)
    }
}

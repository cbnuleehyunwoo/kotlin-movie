package domain.screening

import domain.DomainTestFixture.createMovie
import domain.DomainTestFixture.createScreening
import domain.DomainTestFixture.createScreeningRoom
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ScreeningScheduleTest {
    @Test
    fun `상영 정보 리스트를 가진다`() {

        val screenings = listOf(createScreening())

        val schedule = ScreeningSchedule(screenings)

        schedule.screenings shouldBe screenings
    }

    @Test
    fun `한 영화가 동시에 상영될 경우 예외를 던진다`() {

        // given
        val startTime = LocalDateTime.of(2026, 4, 8, 10, 0)
        val movie = createMovie(title = "허닛")

        // when
        val screening1 = createScreening(
            movie = movie,
            startTime = startTime,
            room = createScreeningRoom("커브볼 1관")
        )
        val screening2 = createScreening(
            movie = movie,
            startTime = startTime,
            room = createScreeningRoom("커브볼 2관")
        )

        // then
        shouldThrow<IllegalArgumentException> {
            ScreeningSchedule(listOf(screening1, screening2))
        }
    }
}

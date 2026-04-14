import controller.Controller
import domain.common.TimeRange
import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.Title
import domain.screening.Screening
import domain.screening.ScreeningPeriod
import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.screening.ScreeningSchedule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun main() {
    val schedule = InitData.createSchedule()
    Controller(schedule).run()
}

object InitData {
    fun createSchedule(): ScreeningSchedule {
        val movie1 = Movie(
            title = Title("허닛"),
            runningTime = RunningTime(167),
            screeningPeriod = ScreeningPeriod(LocalDate.of(2026, 4, 8), LocalDate.of(2026, 4, 9))
        )

        val movie2 = Movie(
            title = Title("커비"),
            runningTime = RunningTime(167),
            screeningPeriod = ScreeningPeriod(LocalDate.of(2026, 4, 8), LocalDate.of(2026, 4, 9))
        )

        val room = ScreeningRoom(
            name = ScreeningRoomName("커피"),
            operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0))
        )

        return ScreeningSchedule(
            listOf(
                Screening(movie = movie1, room = room, startTime = LocalDateTime.of(2026, 4, 8, 10, 0)),
                Screening(movie = movie1, room = room, startTime = LocalDateTime.of(2026, 4, 8, 14, 0)),
                Screening(movie = movie2, room = room, startTime = LocalDateTime.of(2026, 4, 8, 10, 0)),
                Screening(movie = movie2, room = room, startTime = LocalDateTime.of(2026, 4, 8, 14, 0)),
            )
        )
    }
}

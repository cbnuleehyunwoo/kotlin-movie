package domain.seat

import domain.DomainTestFixture.createScreening
import domain.DomainTestFixture.seatA1
import domain.DomainTestFixture.seatA2
import domain.ticket.Ticket
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SeatPositionsTest {
    @Test
    fun `SeatPositions 생성 시 위치에 중복이 있을 경우 예외를 던진다`() {
        // given
        val screening = createScreening()

        // when & then
        shouldThrow<IllegalArgumentException> {
            Ticket(screening, SeatPositions(listOf(seatA1(), seatA1())))
        }
    }

    @Test
    fun `SeatPositions 생성 시 위치에 중복이 없다면 정상적으로 생성된다`() {
        // given
        val positions = listOf(seatA1(), seatA2())

        // when
        val seatPositions = SeatPositions(positions)

        // then
        seatPositions.positions shouldBe positions
        seatPositions.positions.size shouldBe 2
    }

}

package domain.ticket

import domain.DomainTestFixture.createScreening
import domain.DomainTestFixture.createTicket
import domain.DomainTestFixture.seatA1
import domain.DomainTestFixture.seatA2
import domain.seat.SeatPositions
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TicketTest {
    @Test
    fun `티켓은 상영 정보와 예매 좌석 정보를 가진다`() {
        // given
        val screening = createScreening()
        val selectedPositions = SeatPositions(listOf(seatA1(), seatA2()))

        // when
        val ticket = Ticket(screening, selectedPositions)

        // then
        ticket.screening shouldBe screening
        ticket.seatPositions shouldBe selectedPositions
    }

    @Test
    fun `두 티켓의 상영 시간이 겹치는 티켓이라면 true를 반환한다`() {
        // given
        val startTime = LocalDateTime.of(2026, 4, 10, 10, 0)
        val ticket1 = createTicket(screening = createScreening(startTime = startTime))
        val ticket2 = createTicket(screening = createScreening(startTime = startTime.plusMinutes(30)))

        // when & then
        ticket1.isOverlapping(ticket2) shouldBe true
    }

    @Test
    fun `두 티켓이 동일한 상영 회차의 티켓이라면 true를 반환한다`() {
        // given
        val screening = createScreening()
        val ticket1 = createTicket(screening = screening)
        val ticket2 = createTicket(screening = screening)

        // when & then
        ticket1.isSameScreening(ticket2) shouldBe true
    }

    @Test
    fun `두 티켓 간에 동일한 좌석이 포함되어 있다면 true를 반환한다`() {
        // given
        val ticket1 = createTicket(seatPositions = SeatPositions(listOf(seatA1(), seatA2())))
        val ticket2 = createTicket(seatPositions = SeatPositions(listOf(seatA1())))

        // when & then
        ticket1.hasSameSeat(ticket2) shouldBe true
    }
}

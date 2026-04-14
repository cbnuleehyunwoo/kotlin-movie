package domain.ticket

import domain.DomainTestFixture.createScreening
import domain.DomainTestFixture.createTicket
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TicketBucketTest {
    @Test
    fun `TicketBucket은 Tickets를 가진다`() {

        // given
        val tickets = listOf(createTicket())

        // when
        val bucket = TicketBucket(tickets)

        // then
        bucket.tickets shouldBe tickets
    }

    @Test
    fun `티켓을 추가할 때, 상영시간이 겹치면 예외를 던진다`() {

        // given
        val startTime = LocalDateTime.of(2026, 4, 8, 10, 0)
        val existingTicket = createTicket(screening = createScreening(startTime = startTime))
        val bucket = TicketBucket(listOf(existingTicket))

        // when
        val overlappingTicket = createTicket(
            screening = createScreening(startTime = startTime.plusMinutes(30))
        )

        // then
        shouldThrow<IllegalArgumentException> {
            bucket.addTicket(overlappingTicket)
        }
    }

    @Test
    fun `상영 시간이 겹치지 않는 티켓을 추가하면 티켓이 추가된 장바구니를 반환한다`() {

        // given
        val existingTicket = createTicket(
            screening = createScreening(
                startTime = LocalDateTime.of(2026, 4, 8, 10, 0)
            )
        )
        val bucket = TicketBucket(listOf(existingTicket))

        // when
        val newTicket = createTicket(
            screening = createScreening(startTime = LocalDateTime.of(2026, 4, 8, 14, 30))
        )
        val updatedBucket = bucket.addTicket(newTicket)

        // then
        updatedBucket.tickets.size shouldBe 2
        updatedBucket.tickets shouldBe listOf(existingTicket, newTicket)
    }
}

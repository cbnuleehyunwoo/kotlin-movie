package domain.payment

import domain.common.Money
import domain.DomainTestFixture.createTicket
import domain.DomainTestFixture.ticketDiscountPolicy
import domain.DomainTestFixture.totalDiscountPolicy
import domain.ticket.TicketBucket
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PaymentSystemTest {

    private val paymentSystem = PaymentSystem(
        ticketDiscountStrategy = ticketDiscountPolicy,
        totalDiscountStrategy = totalDiscountPolicy
    )

    @Test
    fun `무비데이, 시간 할인, 포인트 차감, 결제 수단 할인이 올바른 순서로 적용된다`() {
        val result = paymentSystem.calculate(
            point = Point(5_000),
            payment = PaymentType.CREDIT_CARD,
            ticketBucket = TicketBucket(listOf(createTicket(), createTicket())),
        )
        assertThat(result).isEqualTo(Money(11_970))
    }

    @Test
    fun `할인 적용 후 금액이 0보다 작을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            paymentSystem.calculate(
                point = Point(10001),
                payment = PaymentType.CREDIT_CARD,
                ticketBucket = TicketBucket(listOf(createTicket())),
            )
        }
    }
}


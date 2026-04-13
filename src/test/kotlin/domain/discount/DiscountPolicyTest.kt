package domain.discount

import domain.common.Money
import domain.payment.PaymentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountPolicyTest {

    @Test
    fun `의도한 티켓 할인이 순서대로 적용된다`() {
        // given
        val ticketDiscountPolicy = TicketDiscountPolicy(
            strategies = listOf(
                MoviedayDiscount(),
                TimeDiscount(),
            )
        )
        val given = Money(10_000)
        // when
        val result = ticketDiscountPolicy.calculateDiscountResult(given, ticketDiscountContext)
        val firstDiscount = MoviedayDiscount().apply(given, ticketDiscountContext)
        val secondDiscount = TimeDiscount().apply(given , ticketDiscountContext)
        val expected = given - (firstDiscount + secondDiscount)
        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `의도한 결제 할인이 순서대로 적용된다`() {
        // given
        val totalDiscountPolicy = TotalDiscountPolicy(
            strategies = listOf(
                PaymentDiscount(),
            )
        )
        val given = Money(10_000)
        // when
        val result = totalDiscountPolicy.calculateDiscountResult(given, cardPaymentContext)
        val firstDiscount = PaymentDiscount().apply(given, cardPaymentContext)
        val expected = given - firstDiscount
        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `카드 결제 할인 시 5퍼센트의 할인 금액이 반환된다`() {
        // given
        val given = Money(10000)
        // when
        val result = PaymentDiscount().apply(given, cardPaymentContext)
        // then
        assertThat(result).isEqualTo(Money(500))
    }

    @Test
    fun `현금 결제 할인 시 2퍼센트의 할인 금액이 반환된다`() {
        // given
        val given = Money(10000)
        // when
        val result = PaymentDiscount().apply(given, PaymentDiscountContext(PaymentType.CASH))
        // then
        assertThat(result).isEqualTo(Money(200))
    }

    @Test
    fun `날짜가 무비데이 조건(10, 20, 30일)에 해당하는 경우 할인 금액(1000원)을 반환한다`() {
        // given
        val given = Money(10_000)
        // when
        val result = MoviedayDiscount().apply(given, ticketDiscountContext)
        // then
        assertThat(result).isEqualTo(Money(1000))
    }

    @Test
    fun `특정 시간대 조건(11시00분 이전, 20시00분 이후)에 해당하는 경우 할인 금액(2000원)을 반환한다`() {
        // given
        val given = Money(10_000)
        // when
        val result = TimeDiscount().apply(given, ticketDiscountContext)
        // then
        assertThat(result).isEqualTo(Money(2000))
    }

    private val ticketDiscountContext = TicketDiscountContext(
        dateTime = LocalDateTime.of(2026, 4, 10, 10, 0)
    )

    private val cardPaymentContext = PaymentDiscountContext(
        paymentType = PaymentType.CREDIT_CARD
    )

}

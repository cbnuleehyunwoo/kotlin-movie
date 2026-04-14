package domain.discount

import domain.DomainTestFixture.ticketDiscountPolicy
import domain.DomainTestFixture.totalDiscountPolicy
import domain.common.Money
import domain.payment.PaymentType
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountPolicyTest {

    @Test
    fun `의도한 티켓 할인(무비데이 1000원, 시간할인 2000원)이 적용된다`() {

        // given
        val given = Money(10_000)

        // when
        val result = ticketDiscountPolicy.calculateDiscountResult(given, ticketDiscountContext)

        // then
        result shouldBe Money(7_000)    }

    @Test
    fun `의도한 결제 할인(카드 5%)이 적용된다`() {

        // given
        val given = Money(10_000)

        // when
        val result = totalDiscountPolicy.calculateDiscountResult(given, cardPaymentContext)

        // then
        result shouldBe Money(9_500)
    }

    @Test
    fun `카드 결제 할인 시 5퍼센트의 할인 금액이 반환된다`() {

        // given
        val given = Money(10_000)

        // when
        val result = PaymentDiscount().apply(given, cardPaymentContext)

        // then
        result shouldBe Money(500)    }

    @Test
    fun `현금 결제 할인 시 2퍼센트의 할인 금액이 반환된다`() {

        // given
        val given = Money(10000)

        // when
        val result = PaymentDiscount().apply(given, PaymentDiscountContext(PaymentType.CASH))

        // then
        result shouldBe Money(200)    }

    @Test
    fun `날짜가 무비데이 조건(10, 20, 30일)에 해당하는 경우 할인 금액(1000원)을 반환한다`() {

        // given
        val given = Money(10_000)

        // when
        val result = MoviedayDiscount().apply(given, ticketDiscountContext)

        // then
        result shouldBe Money(1_000)
    }

    @Test
    fun `특정 시간대 조건(11시00분 이전, 20시00분 이후)에 해당하는 경우 할인 금액(2000원)을 반환한다`() {

        // given
        val given = Money(10_000)

        // when
        val result = TimeDiscount().apply(given, ticketDiscountContext)

        // then
        result shouldBe Money(2_000)
    }

    private val ticketDiscountContext = TicketDiscountContext(
        dateTime = LocalDateTime.of(2026, 4, 10, 10, 0)
    )

    private val cardPaymentContext = PaymentDiscountContext(
        paymentType = PaymentType.CREDIT_CARD
    )

}

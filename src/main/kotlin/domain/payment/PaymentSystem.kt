package domain.payment

import domain.common.Money
import domain.discount.PaymentDiscountContext
import domain.discount.TicketDiscountContext
import domain.discount.TicketDiscountPolicy
import domain.discount.TotalDiscountPolicy
import domain.ticket.TicketBucket

class PaymentSystem(
    private val ticketDiscountStrategy: TicketDiscountPolicy,
    private val totalDiscountStrategy: TotalDiscountPolicy
) {
    fun calculate(
        point: Point,
        payment: PaymentType,
        ticketBucket: TicketBucket,
    ): Money {
        var total = Money(0)

        ticketBucket.tickets.forEach { ticket ->
            total += ticketDiscountStrategy.calculateDiscountResult(
                money = ticket.totalPrice,
                context = TicketDiscountContext(ticket.screening.startTime)
            )
        }

        total = applyPoint(total, point)

        total = totalDiscountStrategy.calculateDiscountResult(
            money = total,
            context = PaymentDiscountContext(payment)
        )
        return total
    }

    private fun applyPoint(total: Money, point: Point): Money =
        total - point.toMoney()
}

package domain.discount

import domain.common.Money

class MoviedayDiscount : TicketDiscountStrategy {
    override fun apply(money: Money, context: TicketDiscountContext): Money {
        if (context.dateTime.toLocalDate().dayOfMonth % 10 == 0)
            return money * 0.1
        return Money(0)
    }
}

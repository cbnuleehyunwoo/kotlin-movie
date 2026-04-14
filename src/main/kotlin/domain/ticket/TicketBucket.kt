package domain.ticket

import domain.screening.Screening

data class TicketBucket(
    val tickets: List<Ticket> = emptyList(),
) {

    fun isSchedulable(newScreening: Screening): Boolean {
        return tickets.none { it.screening.isOverlapping(newScreening) && !it.screening.isSame(newScreening) }
    }

    fun validateSchedulable(newScreening: Screening) {
        if (!isSchedulable(newScreening)) {
            throw IllegalArgumentException("이미 장바구니에 담긴 티켓과 시간이 겹칩니다.")
        }
    }

    fun addTicket(newTicket: Ticket): TicketBucket {
        for (ticket in tickets) {
            if (!ticket.isOverlapping(newTicket)) continue

            require(ticket.isSameScreening(newTicket)) { "동일한 시간대의 영화는 예매할 수 없습니다." }
            require((ticket.hasSameSeat(newTicket)).not()) { "이미 선택하신 좌석입니다." }
        }
        return TicketBucket(tickets + newTicket)
    }
}

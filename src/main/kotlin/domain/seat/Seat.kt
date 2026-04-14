package domain.seat

import domain.common.Money

data class Seat(
    val position: SeatPosition,
    val state: ReserveState = ReserveState.AVAILABLE,
) {
    val grade: SeatGrade = SeatGrade.of(position)

    fun changeState(state: ReserveState): Seat = this.copy(state = state)
    fun isReservable(): Boolean = state == ReserveState.AVAILABLE
}

enum class ReserveState {
    RESERVED,
    AVAILABLE,
}

data class SeatPosition(
    val row: Row,
    val column: Column,

) {
    val price: Money = SeatGrade.of(this).price
    override fun toString(): String = "$row$column"
}

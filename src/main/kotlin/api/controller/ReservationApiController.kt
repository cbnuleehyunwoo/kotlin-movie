package api.controller

import api.dto.ReservationItemDto
import api.dto.ReservationRequest
import api.dto.ReservationResponse
import domain.common.Money
import domain.payment.PaymentType
import domain.reservation.Reservation
import domain.seat.Column
import domain.seat.Row
import domain.seat.SeatPosition
import domain.seat.SeatPositions
import domain.ticket.Ticket
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import repository.ReservationRepository
import repository.ScreeningRepository

@RestController
@RequestMapping("/api/reservations")
class ReservationApiController(
    private val screeningRepository: ScreeningRepository,
    private val reservationRepository: ReservationRepository
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun reserve(@RequestBody request: ReservationRequest): ReservationResponse {
        val tickets = request.reservations.map { item ->
            val screening = screeningRepository.findById(item.screeningId)
            val positions = item.seats.map { parseSeat(it) }
            val seatPositions = SeatPositions(positions)
            
            screening.isReservable(seatPositions)
            
            Ticket(screening, seatPositions)
        }

        val paymentType = PaymentType.valueOf(request.paymentMethod)
        val usedPoints = Money(request.usedPoints)
        
        var ticketsTotal = Money(0)
        tickets.forEach { ticketsTotal += it.totalPrice }
        
        val discountedPrice = ticketsTotal * (1 - paymentType.discountRate)
        val finalPrice = discountedPrice - usedPoints

        val reservation = Reservation(
            tickets = tickets,
            usedPoints = usedPoints,
            paymentMethod = paymentType,
            totalPrice = finalPrice
        )

        val savedId = reservationRepository.save(reservation)

        return ReservationResponse(
            reservationId = savedId,
            reservations = request.reservations,
            usedPoints = request.usedPoints,
            paymentMethod = request.paymentMethod,
            totalPrice = finalPrice.amount
        )
    }

    private fun parseSeat(seatStr: String): SeatPosition {
        val rowStr = seatStr.substring(0, 1)
        val colInt = seatStr.substring(1).toInt()
        return SeatPosition(Row(rowStr), Column(colInt))
    }
}

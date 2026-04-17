package domain.repository

import domain.DomainTestFixture
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.JdbcConnection
import repository.ReservationRepository
import repository.SimpleDataSource
import java.sql.Connection

class ReservationRepositoryTest {

    private lateinit var connection: Connection
    private lateinit var reservationRepository: ReservationRepository

    @BeforeEach
    fun setUp() {
        connection = JdbcConnection.getConnection()
        reservationRepository = ReservationRepository(SimpleDataSource())

        connection.createStatement().use { stmt ->
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS reservations (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                used_points INT NOT NULL,
                payment_method VARCHAR(50) NOT NULL,
                total_price INT NOT NULL
            )
        """.trimIndent())

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS reserved_seats (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                reservation_id BIGINT NOT NULL,
                screening_id BIGINT NOT NULL,
                seat_row VARCHAR(5) NOT NULL,
                seat_column INT NOT NULL,
                FOREIGN KEY (reservation_id) REFERENCES reservations(id)
            )
        """.trimIndent())

            stmt.execute("SET REFERENTIAL_INTEGRITY FALSE")
            stmt.execute("TRUNCATE TABLE reserved_seats RESTART IDENTITY")
            stmt.execute("TRUNCATE TABLE reservations RESTART IDENTITY")
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE")
        }
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { stmt ->
            stmt.execute("DROP TABLE IF EXISTS reserved_seats CASCADE")
            stmt.execute("DROP TABLE IF EXISTS reservations CASCADE")
        }
        connection.close()
    }

    @Test
    fun `save는 예약 정보와 좌석 정보를 저장한다`() {
        // given
        val reservation = DomainTestFixture.createReservation(
            tickets = listOf(
                DomainTestFixture.createTicketWithSeats(
                    DomainTestFixture.seatA1(),
                    DomainTestFixture.seatA2()
                )
            )
        )

        // when
        val savedId = reservationRepository.save(reservation)

        // then
        savedId shouldNotBe null

        connection.prepareStatement("SELECT count(*) FROM reservations").use { stmt ->
            val rs = stmt.executeQuery()
            rs.next()
            rs.getInt(1) shouldBe 1
        }

        connection.prepareStatement("SELECT count(*) FROM reserved_seats WHERE reservation_id = ?").use { stmt ->
            stmt.setLong(1, savedId)
            val rs = stmt.executeQuery()
            rs.next()
            rs.getInt(1) shouldBe 2
        }
    }
}

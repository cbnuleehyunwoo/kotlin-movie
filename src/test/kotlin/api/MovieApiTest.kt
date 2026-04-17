package api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieApiControllerTest(
    @LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `영화 목록 조회 요청 시 200 OK를 반환하고 영화와 상영 정보가 포함된다`() {
        client.get().uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()

            .jsonPath("$.movies").isArray()

            .jsonPath("$.movies[0].id").exists()
            .jsonPath("$.movies[0].title").exists()
            .jsonPath("$.movies[0].runningTimeMinutes").isNumber()

            .jsonPath("$.movies[0].screenings").isArray()
            .jsonPath("$.movies[0].screenings[0].startAt").exists()
            .jsonPath("$.movies[0].screenings[0].endAt").exists()
    }
}

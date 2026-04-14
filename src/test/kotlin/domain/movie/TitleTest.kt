package domain.movie

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class TitleTest {
    @Test
    fun `영화 제목이 공백이 아닌 경우 Title이 생성된다`() {
        val title = Title(title = "커비")
        assertThat(title.title).isEqualTo("커비")
    }

    @Test
    fun `영화 제목이 공백일 경우 예외를 던진다`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Title(title = "") }
    }
}

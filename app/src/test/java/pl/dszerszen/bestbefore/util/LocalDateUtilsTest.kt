package pl.dszerszen.bestbefore.util

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class LocalDateUtilsTest {

    @Test
    fun `should properly format date`()  {
        //Arrange
        val date = LocalDate.parse("2020-01-01")
        //Act
        val formattedDate = date.formatFullDate()
        //Assert
        formattedDate shouldBe "1 stycznia 2020"

    }
}
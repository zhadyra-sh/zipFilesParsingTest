package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Merchants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;

public class JsonFileParsingTest {
    private final ClassLoader cl = JsonFileParsingTest.class.getClassLoader();
    private static final ObjectMapper om = new ObjectMapper();

    @DisplayName("Тестирование содержимого JSON файла")
    @Tag("blocker")
    @Test
    public void jsonFileParsingTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("merchants.json")

        )) {
            Merchants merchants = om.readValue(reader, Merchants.class);
            Assertions.assertThat(merchants.getDataBaseVersion()).isEqualTo(1.1);

            Assertions.assertThat(merchants.getMerchants().get(0).getId()).isEqualTo(1);
            Assertions.assertThat(merchants.getMerchants().get(0).getName()).isEqualTo("Sulpak");
            Assertions.assertThat(merchants.getMerchants().get(0).getCity()).isEqualTo("Almaty");

            Assertions.assertThat(merchants.getMerchants().get(1).getId()).isEqualTo(2);
            Assertions.assertThat(merchants.getMerchants().get(1).getName()).isEqualTo("Mechta");
            Assertions.assertThat(merchants.getMerchants().get(1).getCity()).isEqualTo("Astana");

            Assertions.assertThat(merchants.getMerchants().get(2).getId()).isEqualTo(3);
            Assertions.assertThat(merchants.getMerchants().get(2).getName()).isEqualTo("Alser");
            Assertions.assertThat(merchants.getMerchants().get(2).getCity()).isEqualTo("Kostanay");
        }

    }
}

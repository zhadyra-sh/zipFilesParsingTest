package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class ZipFilesParsingTest {

    PDF pdf = null;
    XLS xlsx = null;
    InputStreamReader inputStreamReader = null;

    @DisplayName("Тестирование содержимого ZIP файла")
    @Tag("regress")
    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                getClass().getResourceAsStream("/parsing.zip")

        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }
    }

    @DisplayName("Тестирование содержимого PDF файла")
    @Tag("regression")
    @Test
    void pdfFileParsingTest() throws Exception {

        try (ZipInputStream zis = new ZipInputStream(
                getClass().getResourceAsStream("/parsing.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("BP.pdf")) {
                    pdf = new PDF(zis);
                    break;
                }
            }
        }
        assertThat(pdf.text).contains("TESTING");
        System.out.println(pdf.text);

    }

    @DisplayName("Тестирование содержимого Excel файла ")
    @Tag("regression")
    @Test
    void xlsxFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                getClass().getResourceAsStream("/parsing.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("merchant.xlsx")) {
                    xlsx = new XLS(zis);
                    break;
                }
            }
        }

        String actualValue = xlsx.excel.getSheetAt(0).getRow(19).getCell(1).getStringCellValue();
        Assertions.assertThat(actualValue).contains("Marathon-4");

    }

    @DisplayName("Тестирование содержимого CSV файла")
    @Tag("regression")
    @Test
    void csvFileParsingTest() throws Exception {

        try (InputStream zipInputStream = getClass().getResourceAsStream("/parsing.zip");
             ZipInputStream zis = new ZipInputStream(zipInputStream)) {

            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().equals("tableConvert.com_466259.csv")) {
                    inputStreamReader = new InputStreamReader(zis);
                    break;
                }
            }

            Assertions.assertThat(inputStreamReader) //проверка на поиск файла
                    .isNotNull()
                    .withFailMessage("CSV файл не найден в архиве или не удалось его открыть");

            CSVReader csvReader = new CSVReader(inputStreamReader);

            List<String[]> data = csvReader.readAll();

            data = data.stream()  // фильтрация на пустые строки
                    .map(row -> Arrays.stream(row).filter(value -> !value.isEmpty()).toArray(String[]::new))
                    .collect(Collectors.toList());

            Assertions.assertThat(data.get(0)).isEqualTo(new String[]{"name", "surname", "email"});
            Assertions.assertThat(data.get(1)).isEqualTo(new String[]{"Test", "Testing", "test@gmail.com"});

            for (String[] str : data) {
                for (String value : str) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }

        }
    }
}






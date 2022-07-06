package de.kremer.parser.service;

import de.kremer.parser.client.MedianClient;
import de.kremer.parser.client.response.RequestMedian;
import de.kremer.parser.dto.NumericalRecordDto;
import de.kremer.parser.dto.Record;
import de.kremer.parser.dto.RowDto;
import de.kremer.parser.dto.TextRecordDto;
import de.kremer.parser.exception.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class ParserServiceTest {

    @Autowired
    private ParserService parserService;

    @MockBean
    private MedianClient medianClient;

    @Test
    public void validCSV() throws ParserException {
        List<RowDto> medians = new ArrayList<>();
        medians.add(createRow());
        Mockito.when(medianClient.createPost(Mockito.any())).thenReturn(medians);

        List<String> testFilePaths = new ArrayList<>();
        testFilePaths.add("./src/test/resources/multi_test.csv");
        parserService.readFiles(testFilePaths);

        Mockito.verify(medianClient, times(1)).createPost(any(RequestMedian.class));
    }

    @Test
    public void invalidCSV() {
        assertThatThrownBy(() -> parserService.readFiles(null))
                .isInstanceOf(ParserException.class)
                .hasMessage("No file paths referred for processing.");
    }

    @Test
    public void invalidCSV2() {
        List<String> testFilePaths = new ArrayList<>();
        testFilePaths.add("./src/test/resources/multi_test_invalid.csv");
        assertThatThrownBy(() -> parserService.readFiles(testFilePaths))
                .isInstanceOf(ParserException.class)
                .hasMessage("Different cell types in the same column.");
    }

    @Test
    public void invalidCSV3() {
        List<String> testFilePaths = new ArrayList<>();
        testFilePaths.add("./src/test/resources/multi_test_invalid2.csv");
        assertThatThrownBy(() -> parserService.readFiles(testFilePaths))
                .isInstanceOf(ParserException.class)
                .hasMessage("Inconsitency between number of cells per row and number of column names.");
    }

    private RowDto createRow() {
        List<String> headers = new ArrayList<>();
        headers.add("attr1");
        headers.add("attr2");
        headers.add("attr3");
        headers.add("attr4");
        headers.add("attr5");
        headers.add("label");

        List<String> types = new ArrayList<>();
        List<Record> records = new ArrayList<>();
        NumericalRecordDto numRec1 = new NumericalRecordDto();
        numRec1.setValue(0.1);
        records.add(numRec1);
        types.add(numRec1.getType());
        NumericalRecordDto numRec2 = new NumericalRecordDto();
        numRec2.setValue(0.1);
        records.add(numRec2);
        types.add(numRec2.getType());
        NumericalRecordDto numRec3 = new NumericalRecordDto();
        numRec3.setValue(0.1);
        records.add(numRec3);
        types.add(numRec3.getType());
        NumericalRecordDto numRec4 = new NumericalRecordDto();
        numRec4.setValue(0.1);
        records.add(numRec4);
        types.add(numRec4.getType());
        NumericalRecordDto numRec5 = new NumericalRecordDto();
        numRec5.setValue(0.1);
        records.add(numRec5);
        types.add(numRec5.getType());
        TextRecordDto textRec1 = new TextRecordDto();
        textRec1.setValue("one");
        records.add(textRec1);
        types.add(textRec1.getType());

        RowDto row = new RowDto(headers, records, types);
        return row;
    }
}

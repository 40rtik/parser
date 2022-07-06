package de.kremer.parser.client.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kremer.parser.dto.Record;
import de.kremer.parser.serialization.RowDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@JsonDeserialize(using = RowDeserializer.class)
@Data
@AllArgsConstructor
public class Row {

    /*
    Columnames
     */
    private List<String> header;

    /*
    Records for each cell in the row.
     */
    private List<Record> records;

    /*
    Cell types in the row.
     */
    private List<String> typelist;

}

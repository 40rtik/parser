package de.kremer.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapperclass for a row.
 *
 * @author kremer
 */
@Data
@AllArgsConstructor
public class RowDto {

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

    public String toString() {
        String representation = "";
        List<String> values = new ArrayList<>();
        if (this.records != null && this.records.size() > 0) {
            for (Record record : records) {
                values.add(record.getValue() + "");
            }
            return String.join(";", values);
        }
        return representation;
    }
}

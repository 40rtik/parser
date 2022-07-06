package de.kremer.parser.dto;

import lombok.Data;

/**
 * Class for the text representation of a cell.
 *
 * @author kremer
 */
@Data
public class TextRecordDto implements Record {
    private String value;

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public String getType() {
        return String.class.toString();
    }
}

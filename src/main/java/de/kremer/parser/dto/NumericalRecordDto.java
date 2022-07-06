package de.kremer.parser.dto;

import lombok.Data;

/**
 * Class for the numerical representation of a cell.
 *
 * @author kremer
 */
@Data
public class NumericalRecordDto implements Record {
    private double value;

    @Override
    public void setValue(Object value) {
        this.value = (double) value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public String getType() {
        return Double.class.toString();
    }

}

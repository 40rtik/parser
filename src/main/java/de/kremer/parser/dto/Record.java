package de.kremer.parser.dto;

/**
 * Interface for the different types of the cells in the csv file.
 *
 * @author kremer
 */
public interface Record {

    public void setValue(Object value);

    public Object getValue();

    public String getType();
}

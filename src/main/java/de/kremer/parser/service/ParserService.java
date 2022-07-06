package de.kremer.parser.service;

import de.kremer.parser.client.MedianClient;
import de.kremer.parser.client.response.RequestMedian;
import de.kremer.parser.client.response.Row;
import de.kremer.parser.dto.NumericalRecordDto;
import de.kremer.parser.dto.Record;
import de.kremer.parser.dto.RowDto;
import de.kremer.parser.dto.TextRecordDto;
import de.kremer.parser.exception.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Service
public class ParserService {

    private static final String SUFFIX = "_median.csv";

    private static final String DELIMITER = ";";

    private static final String NEWLINE = "\n";

    @Autowired
    private MedianClient medianClient;

    /**
     * Read a list of files, calculates the medians and writes the results in files with the suffix {@link #SUFFIX}.
     *
     * @param paths List of file paths.
     * @throws ParserException Represents validation errors, io errors and null errors.
     */
    public void readFiles(List<String> paths) throws ParserException {
        if (paths == null) {
            throw new ParserException("No file paths referred for processing.");
        }
        try {
            for (String filepath : paths) {
                File inputFile = new File(filepath);
                if (inputFile.exists()) {
                    List<RowDto> parsedRows = parseFile(inputFile);

                    List<Row> convertedRows = new ArrayList<>();
                    for (RowDto row : parsedRows) {
                        Row reqRow = new Row(row.getHeader(), row.getRecords(), row.getTypelist());
                        convertedRows.add(reqRow);
                    }

                    RequestMedian requestMedian = new RequestMedian(convertedRows);

                    List<RowDto> median = medianClient.createPost(requestMedian);
                    writeToCSV(filepath, median);
                }else{
                    throw new ParserException("Referenced file not found.");
                }
            }
        } catch (RestClientException exception) {
            throw new ParserException(String.format("Median Service could not calculate the medians. %s", exception.getMessage()));
        }
    }

    /**
     * Parse the content of a csv file.
     *
     * @param inputFile the path to the csv file
     * @return List<Row> is the structure created from the csv file.
     * @throws ParserException for validation errors or file reference errors
     */
    private List<RowDto> parseFile(File inputFile) throws ParserException {

        List<RowDto> rows = new ArrayList<>();

        // set locale to us, so the csv double values can be parsed
        Locale.setDefault(Locale.US);

        try (Scanner lineScanner = new Scanner(inputFile);) {
            int linecounter = 0;
            List<String> headline = new ArrayList<>();
            while (lineScanner.hasNextLine()) {
                String line = lineScanner.nextLine();
                Scanner cellScanner = new Scanner(line);
                cellScanner.useDelimiter(DELIMITER);
                RowDto row = new RowDto(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                while (cellScanner.hasNext()) {
                    if (cellScanner.hasNextDouble()) {
                        double value = cellScanner.nextDouble();
                        if (linecounter == 0) {
                            headline.add(String.valueOf(value));
                        } else {
                            Record doubleRecord = new NumericalRecordDto();
                            doubleRecord.setValue(value);
                            row.getRecords().add(doubleRecord);
                            row.getTypelist().add(Double.class.getName());
                        }

                    } else {
                        String value = cellScanner.next();
                        if (linecounter == 0) {
                            headline.add(value);
                        } else {
                            Record textRecord = new TextRecordDto();
                            textRecord.setValue(value);
                            row.getRecords().add(textRecord);
                            row.getTypelist().add(String.class.getName());
                        }
                    }
                }
                if (linecounter != 0) {
                    row.setHeader(headline);
                    rows.add(row);
                }
                linecounter += 1;
                cellScanner.close();
            }

            validate(rows);
            return rows;
        } catch (FileNotFoundException e) {
            throw new ParserException("File not found for parsing.");
        }
    }

    /**
     * Consistency validation over all rows.
     *
     * @param structuredData the parsed list of rows
     * @throws ParserException represents different validation errors
     */
    private void validate(List<RowDto> structuredData) throws ParserException {
        List<String> typeList = null;
        for (RowDto row : structuredData) {
            if(typeList == null && row.getTypelist() != null){
                typeList = row.getTypelist();
            }
            if (row.getHeader().size() != row.getRecords().size()) {
                throw new ParserException("Inconsitency between number of cells per row and number of column names.");
            } else if (typeList != null && row.getTypelist() != null && !typeList.equals(row.getTypelist())) {
                throw new ParserException("Different cell types in the same column.");
            }
            // can be here extended with other validations
        }
    }

    /**
     * Writes the calculated medians in the csv format to a new file near the origin file.
     *
     * @param filepath the path to the current file
     * @param medians  the calculated medians for the current file
     * @throws ParserException for null values, as well for IOExceltion
     */
    private void writeToCSV(String filepath, List<RowDto> medians) throws ParserException {
        if (filepath == null) {
            throw new ParserException("Referenced csv file not found.");
        } else if (medians == null) {
            throw new ParserException("Median reference error.");
        } else {

            try {
                StringBuffer sb = new StringBuffer();
                boolean header = true;
                for (RowDto row : medians) {
                    if (row.getRecords() != null) {
                        if (header && row.getHeader() != null) {
                            sb.append(String.join(DELIMITER, row.getHeader()) + NEWLINE);
                            sb.append(row.toString());
                            header = false;
                        } else {
                            sb.append(NEWLINE + row.toString());
                        }
                    }
                }
                String newFilePath = filepath.substring(0, filepath.lastIndexOf(".")) + SUFFIX;
                BufferedWriter writer = new BufferedWriter(new FileWriter(newFilePath));
                writer.write(sb.toString());
                writer.close();
            } catch (IOException e) {
                throw new ParserException("Error at file creation, to persist the calculated median.");
            }
        }
    }
}

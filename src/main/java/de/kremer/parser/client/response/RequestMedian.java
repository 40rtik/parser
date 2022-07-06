package de.kremer.parser.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestMedian {

    List<Row> rows;
}

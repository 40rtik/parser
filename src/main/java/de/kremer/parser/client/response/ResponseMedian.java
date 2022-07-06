package de.kremer.parser.client.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseMedian {

    private List<Row> median;

}

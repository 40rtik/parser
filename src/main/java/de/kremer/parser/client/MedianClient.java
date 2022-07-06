package de.kremer.parser.client;

import de.kremer.parser.apiconfig.ApiConfig;
import de.kremer.parser.client.response.RequestMedian;
import de.kremer.parser.client.response.Row;
import de.kremer.parser.client.response.ResponseMedian;
import de.kremer.parser.dto.RowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Median client
 *
 * @author kremer
 */
@Component
public class MedianClient {

    @Autowired
    private ApiConfig apiConfig;

    private final RestTemplate restTemplate;

    public MedianClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Sends the parsed rows to the median service for median calculation.
     *
     * @param requestMedian Wrapper for the parsed rows.
     * @return List of rows, where each row represents the medians per column for one label.
     * @throws RestClientException Thrown when the Service is not available or could not calculate the medians.
     */
    public List<RowDto> createPost(RequestMedian requestMedian) throws RestClientException {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<RequestMedian> entity = new HttpEntity<>(requestMedian, headers);

        ResponseMedian responseEntity = restTemplate.postForObject(apiConfig.getMedianUrl(), entity, ResponseMedian.class);

        List<RowDto> medians = new ArrayList<>();
        if (responseEntity != null) {
            for (Row row : responseEntity.getMedian()) {
                RowDto rowDto = new RowDto(row.getHeader(), row.getRecords(), row.getTypelist());
                medians.add(rowDto);
            }
        }
        return medians;
    }
}

package de.kremer.parser.apiconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Config
 *
 * @author kremer
 */
@Configuration
@Data
public class ApiConfig {

    @Value("${api.host.baseurl}")
    private String medianUrl;

}

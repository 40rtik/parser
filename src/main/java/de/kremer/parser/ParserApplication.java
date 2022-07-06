package de.kremer.parser;

import de.kremer.parser.exception.ParserException;
import de.kremer.parser.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the entrypoint to the standalone csv parser.
 *
 * @author kremer
 */

@SpringBootApplication
public class ParserApplication implements ApplicationRunner {

    @Autowired
    private ParserService parser;

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

    /**
     * @param args incoming application arguments
     */
    @Override
    public void run(ApplicationArguments args) throws ParserException {
        List<String> filePaths = new ArrayList<>();
        args.getOptionNames().forEach(optionName -> {
            if (optionName.equals("f")) {
                filePaths.addAll(args.getOptionValues(optionName));
            }
        });
        if (filePaths.size() > 0) {
            parser.readFiles(filePaths);
        }
    }
}

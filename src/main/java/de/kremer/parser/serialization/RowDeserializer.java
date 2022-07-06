package de.kremer.parser.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;
import de.kremer.parser.client.response.Row;
import de.kremer.parser.dto.NumericalRecordDto;
import de.kremer.parser.dto.Record;
import de.kremer.parser.dto.TextRecordDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RowDeserializer extends JsonDeserializer<Row> {
    @Override
    public Row deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        if(jsonParser == null){
            throw new IllegalStateException("Json parser error.");
        }
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        List<String> header = new ArrayList<>();
        if (node.get("header").size() > 0) {
            ArrayNode jsonHeader = (ArrayNode) node.get("header");

            for (int i = 0; i < jsonHeader.size(); i++) {
                header.add(jsonHeader.get(i).asText());
            }
        }
        List<Record> records = new ArrayList<>();
        if (node.get("records").size() > 0) {
            ArrayNode jsonRecords = (ArrayNode) node.get("records");
            for (int j = 0; j < jsonRecords.size(); j++) {
                JsonNode record = jsonRecords.get(j);
                if (record.get("value") instanceof DoubleNode) {
                    NumericalRecordDto numrec = new NumericalRecordDto();
                    numrec.setValue(record.get("value").asDouble());
                    records.add(numrec);
                } else if (record.get("value") instanceof TextNode) {
                    TextRecordDto textrec = new TextRecordDto();
                    textrec.setValue(record.get("value").asText());
                    records.add(textrec);
                }
            }
        }
        List<String> typelist = new ArrayList<>();
        if (node.get("typelist").size() > 0) {
            ArrayNode jsonTypelist = (ArrayNode) node.get("typelist");
            for (int i = 0; i < jsonTypelist.size(); i++) {
                typelist.add(jsonTypelist.get(i).asText());
            }
        }

        Row row = new Row(header, records, typelist);
        return row;
    }
}



package com.chartercommunication.assesment.RetailPointsApplication.runner;

import com.chartercommunication.assesment.RetailPointsApplication.dto.PurchaseRequest;
import com.chartercommunication.assesment.RetailPointsApplication.model.Customer;
import com.chartercommunication.assesment.RetailPointsApplication.repository.CustomerRepository;
import com.chartercommunication.assesment.RetailPointsApplication.service.CustomerService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPopulatingRunner implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        final SimpleModule localDateTimeSerialization = new SimpleModule();
        localDateTimeSerialization.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        localDateTimeSerialization.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        mapper.registerModule(localDateTimeSerialization);
        List<Customer> customers = mapper.readValue(
                new File("src/main/resources/data/customers.json"),
                new TypeReference<List<Customer>>() {
                });
        List<PurchaseRequest> purchaseRequests = mapper.readValue(
                new File("src/main/resources/data/purchase_details.json"),
                new TypeReference<List<PurchaseRequest>>() {
                });
        LOGGER.info("========DATASETS IMPORTED========");

        customerRepository.saveAll(customers);
        customerService.purchase(purchaseRequests);
        LOGGER.info("========RECORDS PERSISTED INTO DB========");
        System.out.println(customerService.getReport());
    }

    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        private final DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(format));
        }
    }

    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        private final DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), fmt);
        }
    }

}

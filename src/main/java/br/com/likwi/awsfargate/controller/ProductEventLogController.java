package br.com.likwi.awsfargate.controller;

import br.com.likwi.awsfargate.controller.model.ProductResponse;
import br.com.likwi.awsfargate.model.ProductEventLog;
import br.com.likwi.awsfargate.repository.ProductEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductEventLogController {

    private ProductEventRepository productEventRepository;
    private ObjectMapper objectMapper;

    public ProductEventLogController(ProductEventRepository productEventRepository,
                                     ObjectMapper objectMapper) {
        this.productEventRepository = productEventRepository;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @GetMapping("/events")
    public List<ProductResponse> getAllEvents() {

        final Iterable<ProductEventLog> all = this.productEventRepository.findAll();
        all.forEach(productEventLog -> {
            System.out.println("productEventLog.getProductId() = " + productEventLog.getProductId());
        });

        log.info(String.format("Toda tabela dynamo: %s", this.objectMapper.writeValueAsString(all)));

        return StreamSupport
                .stream(all.spliterator(), false)
                .map(ProductResponse::toReponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/events/{code}")
    public List<ProductResponse> findByCode(@PathVariable String code) {
        return this.productEventRepository.findAllByPk(code)
                .stream()
                .map(ProductResponse::toReponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/events/{code}/{event}")
    public List<ProductResponse> findByCodeAndEventType(@PathVariable String code, @PathVariable String event) {
        return this.productEventRepository.findAllByPkAndSkStartingWith(code, event)
                .stream()
                .map(ProductResponse::toReponse)
                .collect(Collectors.toList());
    }

}

package gr.edu.aegean.epta.kafka.reliable.controller;


import gr.edu.aegean.epta.kafka.reliable.producer.ReliableKafkaProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/kafka")
public class ProducerRestController {

    private final ReliableKafkaProducer producer;

    public ProducerRestController(ReliableKafkaProducer producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/producer/{number}")
    public String produceMessages(@PathVariable("number") String number) {
        log.info("Triggering producer to generate: {} messages", number);

        producer.sendMessage(Integer.parseInt(number));

        return "Messages has been generated successfully";
    }
}

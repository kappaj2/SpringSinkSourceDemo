package za.co.ajk.reactive.processor;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.function.Function;

@SpringBootApplication
public class ProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }

}

@EnableBinding(Processor.class)
class GateAgent {

    private final Random rnd = new Random();

    @Bean
    Function<Flux<Passenger>, Flux<FlyingPassenger>> recognize() {
        return flux -> flux.map(pax -> new FlyingPassenger(pax.getId(), pax.getName(), rnd.nextInt(5) == 0 ? FlyingPassenger.Status.PREMIUM : FlyingPassenger.Status.VALUED));
    }

    @Bean
    Function<Flux<FlyingPassenger>, Flux<FlyingPassenger>> fixMark() {
        return flux -> flux.map(pax -> new FlyingPassenger(pax.getId(),
                pax.getName(),
                pax.getName().equalsIgnoreCase("Mark") ? FlyingPassenger.Status.ENHANCED_SCREENING_SEAT_BY_TOILETS : pax.getStatus())
        );
    }
}


@Value
class FlyingPassenger {
    enum Status {
        VALUED,
        PREMIUM,
        ENHANCED_SCREENING_SEAT_BY_TOILETS
    }

    private final String id, name;
    private final Status status;
}

@Value
class Passenger {
    private final String id, name;
}

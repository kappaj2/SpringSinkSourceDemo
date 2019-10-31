package za.co.ajk.reactive.sink;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@SpringBootApplication
public class SinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SinkApplication.class, args);
	}

}

@EnableBinding(Sink.class)
class FlightAttendant{

	@Bean
	Consumer<Flux<FlyingPassenger>> logPassenger(){
		return flux -> flux.subscribe(System.out::println);
	}
}

@Value
class FlyingPassenger{

	enum Status{
		VALUED,
		PREMIUM,
		ENHANCED_SCREENING_SEAT_BY_TOILETS
	}

	private final String id, name;
	private Status status;
}
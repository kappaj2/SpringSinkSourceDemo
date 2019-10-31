package za.co.ajk.reactive.source;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

@SpringBootApplication
public class SourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SourceApplication.class, args);
	}

}


@EnableBinding(Source.class)
@AllArgsConstructor
class DeskAgent{
	private final Source source;
	private final PassengerGenerator passengerGenerator;

	@Bean
	Supplier<Flux<Passenger>> sendPassengerToGate(){
		return () -> Flux.interval(Duration.ofSeconds(1))
				.onBackpressureDrop()
				.map(l -> passengerGenerator.generate());
	}
}

@Component
class PassengerGenerator{

	private List<String> names = Arrays.asList("Aretem", "Haytham", "Elsa", "Shellee", "Gowran", "Mark");
	private Random rnd = new Random();

	Passenger generate(){
		return new Passenger(UUID.randomUUID().toString(), names.get(rnd.nextInt(names.size())));
	}
}

@Value
class Passenger{

	private final String id, name;
}

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

	public Flux<String> namesFlux() {

		return Flux.fromIterable(List.of("alex", "ben", "chloe")) // Comes from a DB or a service call
				.log(); // logs all the data transfer to the subscriber
	}

	public Mono<String> namesMono() {

		return Mono.just("xavi");
	}

	public Flux<String> namesFlux_map(int stringLength) {

		return Flux.fromIterable(List.of("alex", "ben", "chloe"))
				// .map(String::toUpperCase)
				.map(s -> s.toUpperCase()).filter(s -> s.length() > stringLength).map(s -> s.length() + "-" + s).log(); // logs
																														// all
																														// the
																														// data
																														// transfer
																														// to
																														// the
																														// subscriber
	}

	public Flux<String> namesFlux_immutability() {

		Flux<String> namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

		namesFlux.map(String::toUpperCase);

		return namesFlux;

	}

	public Flux<String> namesFlux_flatMap(int stringLength) {

		return Flux.fromIterable(List.of("alex", "ben", "chloe"))
				// .map(String::toUpperCase)
				.map(s -> s.toUpperCase()).filter(s -> s.length() > stringLength).flatMap(s -> splitString(s)).log();
	}

	public Flux<String> namesFlux_flatMap_async(int stringLength) {

		return Flux.fromIterable(List.of("alex", "ben", "chloe"))
				// .map(String::toUpperCase)
				.map(s -> s.toUpperCase()).filter(s -> s.length() > stringLength).flatMap(s -> splitString_withDelay(s))
				.log();
	}

	public Flux<String> namesFlux_concatMap_async(int stringLength) {
		// ONLY USE IF ORDERING MATTERS
		return Flux.fromIterable(List.of("alex", "ben", "chloe"))
				// .map(String::toUpperCase)
				.map(s -> s.toUpperCase()).filter(s -> s.length() > stringLength).concatMap(this::splitString_withDelay)
				.log();
	}

	public Mono<String> namesMono_map_filter(int stringLength) {
		return Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > stringLength);
	}

	public Mono<List<String>> namesMono_flatMap(int stringLength) {
		return Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > stringLength)
				.flatMap(this::splitStringMono);
	}

	public Flux<String> namesMono_flatMapMany(int stringLength) {
		return Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > stringLength)
				.flatMapMany(this::splitString);
	}

	public Flux<String> namesFlux_transform(int stringLength) {

		Function<Flux<String>, Flux<String>> filterMap = name -> name.map(s -> s.toUpperCase())
				.filter(s -> s.length() > stringLength);

		return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap).flatMap(s -> splitString(s))
				.defaultIfEmpty("default").log();
	}

	public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

		Function<Flux<String>, Flux<String>> filterMap = name -> name.map(s -> s.toUpperCase())
				.filter(s -> s.length() > stringLength).flatMap(s -> splitString(s));

		Flux<String> defaultFlux = Flux.just("default").transform(filterMap);

		return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap).switchIfEmpty(defaultFlux).log();
	}

	public Flux<String> explore_concat() {

		var abcFlux = Flux.just("A", "B", "C");
		var defFlux = Flux.just("D", "E", "F");

		return Flux.concat(abcFlux, defFlux).log();

	}

	public Flux<String> explore_concatWith() {

		var aMono = Mono.just("A");
		var bMono = Mono.just("B");

		return aMono.concatWith(bMono).log();

	}

	public Flux<String> explore_merge() {

		var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
		var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

		// return Flux.merge(abcFlux,defFlux).log();
		return abcFlux.mergeWith(defFlux).log();
	}

	public Flux<String> explore_mergeWith() {

		var aMono = Mono.just("A");
		var bMono = Mono.just("B");

		return aMono.mergeWith(bMono).log();
	}

	public Flux<String> explore_mergeSequential() {

		var abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(200));
		var defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(25));

		return Flux.mergeSequential(abcFlux,defFlux).log();
		
	}
	
	public Flux<String> explore_zip() {

		var abcFlux = Flux.just("A", "B", "C");
		var defFlux = Flux.just("D", "E", "F");

		return Flux.zip(abcFlux,defFlux, 
				(first,second) -> first+second).log(); //AD. BE. CF
		
	}

	public Flux<String> explore_zip_1() {

		var abcFlux = Flux.just("A", "B", "C");
		var defFlux = Flux.just("D", "E", "F");
		var _123Flux = Flux.just("1", "2", "3");
		var _456Flux = Flux.just("4", "5", "6");

		return Flux.zip(abcFlux,defFlux,_123Flux,_456Flux)
				.map(t4 -> t4.getT1()+t4.getT2()+t4.getT3()+t4.getT4())
				.log();
		
	}
	
	public Flux<String> explore_zipWith() {

		var abcFlux = Flux.just("A", "B", "C");
		var defFlux = Flux.just("D", "E", "F");


		return abcFlux.zipWith(defFlux,(abc,def) -> abc+def).log();
		
	}
	
	public Mono<String> explore_zipWith_mono() {

		var aMono = Mono.just("A");
		var bMono = Mono.just("B");

		return aMono.zipWith(bMono)
				.map(tuple -> tuple.getT1()+tuple.getT2())
				.log();
		
	}
	
	
	////////////////////

	private Mono<List<String>> splitStringMono(String s) {
		String[] charArray = s.split("");
		return Mono.just(List.of(charArray));
	}

	private Flux<String> splitString(String name) {
		String[] charArray = name.split("");
		return Flux.fromArray(charArray);
	}

	private Flux<String> splitString_withDelay(String name) {
		String[] charArray = name.split("");
		int delay = new Random().nextInt(1000);
		return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
	}

	public static void main(String[] args) {

		FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

		fluxAndMonoGeneratorService.namesFlux().subscribe(name -> {
			System.out.println("Flux is " + name);
		});

		fluxAndMonoGeneratorService.namesMono().subscribe(name -> {
			System.out.println("Mono is " + name);
		});

	}
}

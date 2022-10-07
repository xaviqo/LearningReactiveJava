import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

	FluxAndMonoGeneratorService fluxAndMonoGeneratorService =
			new FluxAndMonoGeneratorService();

	
	@Disabled 
	@Test
	void namesFlux() {

		var namesFlux = fluxAndMonoGeneratorService.namesFlux();

		StepVerifier.create(namesFlux)
		//.expectNext("alex","ben","chloe")
		//.expectNextCount(3)
		.expectNext("alex")
		.expectNextCount(2)
		.verifyComplete();
	}

	@Disabled
	@Test
	void namesFlux_map() {
		int strLength = 3;
		var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(strLength);

		StepVerifier.create(namesFlux)
		.expectNext("4-ALEX","5-CHLOE")
		.verifyComplete();
	}

	@Disabled
	@Test
	void namesFlux_flatMap(){
		int strLength = 3;
		var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatMap_async(strLength);

		StepVerifier.create(namesFlux)
		//.expectNext("A","L","E","X","C","H","L","O","E")
		.expectNextCount(9)
		.verifyComplete();
	}

	@Disabled
	@Test
	void names_concatMap() {
		int strLength = 3;
		var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap_async(strLength);

		StepVerifier.create(namesFlux)
		.expectNext("A","L","E","X","C","H","L","O","E")
		//.expectNextCount(9)
		.verifyComplete();
	}

	@Disabled
	@Test
	void namesFlux_flatMap_async(){
		int strLength = 3;
		var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatMap(strLength);

		StepVerifier.create(namesFlux)
		.expectNext("A","L","E","X","C","H","L","O","E")
		.verifyComplete();
	}

	@Disabled
	@Test
	void namesMono_flatMap() {
		int strLength = 3;

		Mono<List<String>> value = fluxAndMonoGeneratorService.namesMono_flatMap(strLength);

		StepVerifier.create(value)
		.expectNext(List.of("A","L","E","X"))
		.verifyComplete();
	}

	@Disabled
	@Test
	void namesMono_flatMapMany() {
		int strLength = 3;

		Flux<String> value = fluxAndMonoGeneratorService.namesMono_flatMapMany(strLength);

		StepVerifier.create(value)
		.expectNext("A","L","E","X")
		.verifyComplete();
	}

	@Disabled
	@Test
	void namesFlux_transform() {
		int strLength = 3;

		Flux<String> value = fluxAndMonoGeneratorService.namesFlux_transform(strLength);

		StepVerifier.create(value)
		.expectNext("A","L","E","X","C","H","L","O","E")
		.verifyComplete();
	}
	
	@Disabled
	@Test
	void namesFlux_transform_1() {
		int strLength = 6;

		Flux<String> value = fluxAndMonoGeneratorService.namesFlux_transform(strLength);

		StepVerifier.create(value)
		//.expectNext("A","L","E","X","C","H","L","O","E")
		.expectNext("default")
		.verifyComplete();
	}
	
	@Disabled
	@Test
	void namesFlux_transform_switchIfEmpty() {
		int strLength = 6;

		Flux<String> value = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(strLength);

		StepVerifier.create(value)
		.expectNext("D","E","F","A","U","L","T")
		.verifyComplete();
	}
	
	@Disabled
	@Test
	void explore_concat() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_concat();
		
		StepVerifier.create(value)
		.expectNext("A","B","C","D","E","F")
		.verifyComplete();
	}
	
	@Disabled
	@Test
	void explore_concatWith() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_concatWith();
		
		StepVerifier.create(value)
		.expectNext("A","B")
		.verifyComplete();
	}
	
	@Test
	void explore_merge() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_merge();
		
		StepVerifier.create(value)
		.expectNext("A","D","B","E","C","F")
		.verifyComplete();
	}
	
	@Disabled
	@Test
	void explore_mergeWith() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_mergeWith();
		
		StepVerifier.create(value)
		.expectNext("A","B")
		.verifyComplete();
	}
	
	@Disabled
	@Test
	void explore_mergeSequential() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_mergeSequential();
		
		StepVerifier.create(value)
		.expectNext("A","B","C","D","E","F")
		.verifyComplete();
	}
	
	@Test
	void explore_zip() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_zip();
		
		StepVerifier.create(value)
		.expectNext("AD","BE","CF")
		.verifyComplete();
	}
	
	@Test
	void explore_zip_1() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_zip_1();
		
		StepVerifier.create(value)
		.expectNext("AD14","BE25","CF36")
		.verifyComplete();
	}
	
	@Test
	void explore_zipWith() {
		
		Flux<String> value = fluxAndMonoGeneratorService.explore_zipWith();
		
		StepVerifier.create(value)
		.expectNext("AD","BE","CF")
		.verifyComplete();
	}
	
	@Test
	void explore_zipWith_mono() {
		
		Mono<String> value = fluxAndMonoGeneratorService.explore_zipWith_mono();
		
		StepVerifier.create(value)
		.expectNext("AB")
		.verifyComplete();
	}
	
	// FAIL

	@Disabled
	@Test
	void namesFlux_immutability() {
		var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

		StepVerifier.create(namesFlux)
		.expectNext("ALEX","BEN","CHLOE")
		.verifyComplete();

	}

}

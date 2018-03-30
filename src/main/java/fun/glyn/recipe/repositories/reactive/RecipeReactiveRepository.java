package fun.glyn.recipe.repositories.reactive;

import fun.glyn.recipe.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
    Mono<Recipe> findByDescription(String description);
}

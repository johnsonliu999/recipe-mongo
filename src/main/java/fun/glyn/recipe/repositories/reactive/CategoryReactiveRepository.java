package fun.glyn.recipe.repositories.reactive;

import fun.glyn.recipe.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {
}

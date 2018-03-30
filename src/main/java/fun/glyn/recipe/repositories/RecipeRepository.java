package fun.glyn.recipe.repositories;

import fun.glyn.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}

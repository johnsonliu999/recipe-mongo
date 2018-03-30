package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String l);

    Mono<Void> deleteById(String id);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);

    Mono<RecipeCommand> findCommandById(String l);
}

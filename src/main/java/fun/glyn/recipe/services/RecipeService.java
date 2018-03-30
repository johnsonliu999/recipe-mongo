package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.domain.Recipe;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface RecipeService {
    Mono<Set<Recipe>> getRecipes();

    Mono<Recipe> findById(String l);

    Mono<Void> deleteById(String id);

    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);

    Mono<RecipeCommand> findCommandById(String l);
}

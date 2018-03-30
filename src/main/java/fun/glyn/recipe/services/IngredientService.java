package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findByRecipeIdAndId(String recipeId, String id);

    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);

    Mono<Void> deleteByRecipeIdAndId(String recipeId, String id);
}

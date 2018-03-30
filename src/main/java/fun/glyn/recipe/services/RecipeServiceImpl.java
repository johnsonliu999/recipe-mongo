package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.converters.RecipeCommandToRecipe;
import fun.glyn.recipe.converters.RecipeToRecipeCommand;
import fun.glyn.recipe.domain.Recipe;
import fun.glyn.recipe.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;

    private final RecipeCommandToRecipe recipeCommandToRecipe;

    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("getting recipes...");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String l) {
        return recipeReactiveRepository.findById(l);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {

        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(recipeCommand))
                .map(recipeToRecipeCommand::convert);
//        log.debug("Saved RecipeId : " + savedRecipe.getId());
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {

        return findById(id).map(recipe -> {
            RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
            recipeCommand.getIngredients().forEach(ic -> ic.setRecipeId(id));
            return recipeCommand;
        });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeReactiveRepository.deleteById(id);
    }
}

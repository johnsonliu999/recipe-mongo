package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.converters.RecipeCommandToRecipe;
import fun.glyn.recipe.converters.RecipeToRecipeCommand;
import fun.glyn.recipe.domain.Recipe;
import fun.glyn.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeCommandToRecipe recipeCommandToRecipe;

    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Mono<Set<Recipe>> getRecipes() {
        log.debug("getting recipes...");
        Set<Recipe> recipes = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return Mono.just(recipes);
    }

    @Override
    public Mono<Recipe> findById(String l) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(l);

        if (!recipeOptional.isPresent()) throw new RuntimeException("Recipe not found");
        return Mono.just(recipeOptional.get());
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);

        log.debug("Saved RecipeId : " + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public Mono<RecipeCommand> findCommandById(String l) {
        return Mono.just(recipeToRecipeCommand.convert(findById(l).block()));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        recipeRepository.deleteById(id);
        return null;
    }
}

package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.IngredientCommand;
import fun.glyn.recipe.converters.IngredientCommandToIngredient;
import fun.glyn.recipe.converters.IngredientToIngredientCommand;
import fun.glyn.recipe.domain.Ingredient;
import fun.glyn.recipe.domain.Recipe;
import fun.glyn.recipe.repositories.reactive.RecipeReactiveRepository;
import fun.glyn.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository uomReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository uomReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.uomReactiveRepository = uomReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndId(String recipeId, String id) {

        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(id))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();

        Optional<Ingredient> ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (!ingredientOptional.isPresent()) {
            // new ingredient
            Ingredient ingredient = ingredientCommandToIngredient.convert(command);
            // ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        } else {
            Ingredient ingredient = ingredientOptional.get();
            ingredient.setDescription(command.getDescription());
            ingredient.setAmount(command.getAmount());
            ingredient.setUom(uomReactiveRepository.findById(command.getUom().getId()).block());
        }

        Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (!ingredientOptional.isPresent())
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                    .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                    .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();

        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        ingredientCommand.setRecipeId(savedRecipe.getId());

        return Mono.just(ingredientCommand);
    }

    @Override
    public Mono<Void> deleteByRecipeIdAndId(String recipeId, String id) {

        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {

            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(id))
                    .findFirst();

            if (!ingredientOptional.isPresent()) {
                log.debug("ingredient not exist");
                return Mono.empty();
            }

            Ingredient ingredient = ingredientOptional.get();

            // ingredient.setRecipe(null);
            recipe.getIngredients().remove(ingredient);

            recipeReactiveRepository.save(recipe).block();
        }

        return Mono.empty();
    }
}

package fun.glyn.recipe.converters;

import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final NotesToNotesCommand notesConverter;
    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;


    public RecipeToRecipeCommand(NotesToNotesCommand notesConverter, CategoryToCategoryCommand categoryConverter, IngredientToIngredientCommand ingredientConverter) {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe recipe) {
        if (recipe == null) return null;

        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipe.getId());
        recipeCommand.setPrepTime(recipe.getPrepTime());
        recipeCommand.setCookTime(recipe.getCookTime());
        recipeCommand.setServings(recipe.getServings());
        recipeCommand.setDifficulty(recipe.getDifficulty());
        recipeCommand.setDirections(recipe.getDirections());
        recipeCommand.setDescription(recipe.getDescription());
        recipeCommand.setSource(recipe.getSource());
        recipeCommand.setUrl(recipe.getUrl());
        recipeCommand.setImage(recipe.getImage());

        recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));
        if (recipe.getCategories() != null && recipe.getCategories().size() > 0)
            recipe.getCategories().forEach(category -> recipeCommand.getCategories().add(
                    categoryConverter.convert(category)
            ));
        if (recipe.getIngredients() != null && recipe.getIngredients().size() > 0)
            recipe.getIngredients().forEach(ingredient -> recipeCommand.getIngredients().add(
                    ingredientConverter.convert(ingredient)
            ));

        return recipeCommand;
    }
}

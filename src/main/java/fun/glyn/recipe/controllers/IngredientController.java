package fun.glyn.recipe.controllers;

import fun.glyn.recipe.commands.IngredientCommand;
import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.commands.UnitOfMeasureCommand;
import fun.glyn.recipe.services.IngredientService;
import fun.glyn.recipe.services.RecipeService;
import fun.glyn.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findById(recipeId));
        return "recipe/ingredients/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId,
                                       @PathVariable String id, Model model) {
        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndId(recipeId, id).block());
        return "recipe/ingredients/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/new")
    public String newRecipeIngredient(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();
        // todo: null

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());

        return "recipe/ingredients/form";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/{id}/update")
    public String udpateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model) {
        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndId(recipeId, id));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());

        return "recipe/ingredients/form";
    }

    @PostMapping("/recipe/{recipeId}/ingredients")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();
        return "redirect:/recipe/" + command.getRecipeId() + "/ingredients/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id) {

        log.debug("remove recipe id: " + recipeId + ", id : " + id);
        ingredientService.deleteByRecipeIdAndId(recipeId, id);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}

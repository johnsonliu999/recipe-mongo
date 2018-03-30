package fun.glyn.recipe.controllers;

import fun.glyn.recipe.commands.RecipeCommand;
import fun.glyn.recipe.services.ImageService;
import fun.glyn.recipe.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{id}/upload-image")
    public String showUploadForm(@PathVariable String id, Model model) {

        model.addAttribute("recipe", recipeService.findById(id).block());
        return "/recipe/imageUploadForm";
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id,
                                  @RequestParam("imageFile") MultipartFile file) {
        imageService.saveImageFile(id, file);

        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping("recipe/{id}/image")
    public void renderImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(id).block();

        if (recipeCommand.getImage() != null) {
            byte[] bytes = new byte[recipeCommand.getImage().length];

            int i = 0;
            for (byte b : recipeCommand.getImage())
                bytes[i++] = b;

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(bytes);
            IOUtils.copy(is, response.getOutputStream());
        }

    }
}

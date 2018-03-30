package fun.glyn.recipe.services;

import fun.glyn.recipe.domain.Recipe;
import fun.glyn.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(String id, MultipartFile file) {

        try {
            Recipe recipe = recipeRepository.findById(id).get();

            Byte[] bytes = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()) bytes[i++] = b;

            recipe.setImage(bytes);

            recipeRepository.save(recipe);
        } catch (IOException e) {
            log.error("File error", e);
            e.printStackTrace();
        }
    }
}

package fun.glyn.recipe.services;

import fun.glyn.recipe.domain.Recipe;
import fun.glyn.recipe.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String id, MultipartFile file) {

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    try {
                        Byte[] bytes = new Byte[file.getBytes().length];
                        int i = 0;

                        for (byte b : file.getBytes()) bytes[i++] = b;
                        recipe.setImage(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return recipe;
                 });

        recipeReactiveRepository.save(recipeMono.block()).block();
        return Mono.empty();
//        try {
//            Recipe recipe = recipeReactiveRepository.findById(id).block();
//
//            if (recipe == null) return Mono.empty();
//
//            Byte[] bytes = new Byte[file.getBytes().length];
//
//            int i = 0;
//
//            for (byte b : file.getBytes()) bytes[i++] = b;
//
//            recipe.setImage(bytes);
//
//            recipeReactiveRepository.save(recipe).block();
//        } catch (IOException e) {
//            log.error("File error", e);
//            e.printStackTrace();
//        }
    }
}

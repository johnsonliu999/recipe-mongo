package fun.glyn.recipe.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Document
public class Category {

    @Id
    private String id;
    private String description;
    private Set<Recipe> recipes;

}

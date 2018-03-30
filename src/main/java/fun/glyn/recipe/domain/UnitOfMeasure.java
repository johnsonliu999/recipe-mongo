package fun.glyn.recipe.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@Document
public class UnitOfMeasure {

    @Id
    private String id;

    private String description;
}

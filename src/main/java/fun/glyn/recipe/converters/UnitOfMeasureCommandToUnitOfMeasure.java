package fun.glyn.recipe.converters;

import fun.glyn.recipe.commands.UnitOfMeasureCommand;
import fun.glyn.recipe.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand source) {
        if (source == null) return null;

        final UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(source.getDescription());
        uom.setId(source.getId());

        return uom;
    }
}

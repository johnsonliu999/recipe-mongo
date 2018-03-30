package fun.glyn.recipe.services;

import fun.glyn.recipe.commands.UnitOfMeasureCommand;
import fun.glyn.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import fun.glyn.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository uomReactiveRespository;
    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository uomReactiveRespository, UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.uomReactiveRespository = uomReactiveRespository;
        this.uomConverter = uomConverter;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {

        return uomReactiveRespository.findAll()
                .map(uomConverter::convert);

//        return StreamSupport.stream(uomReactiveRespository.findAll().spliterator(), false)
//                .map(uomConverter::convert)
//                .collect(Collectors.toSet());

    }
}

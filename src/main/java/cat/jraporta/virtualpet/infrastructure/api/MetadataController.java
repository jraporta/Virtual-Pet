package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.dto.response.PetDto;
import cat.jraporta.virtualpet.application.dto.response.ValidPetTypesRequest;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Tag(name = "Metadata Management", description = "Endpoints providing different kinds of lookup data.")
@AllArgsConstructor
@Slf4j
@RestController
public class MetadataController {

    @Operation(
            summary ="Get valid Pet types",
            description = "Returns a list with the valid pet types.",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PetDto.class)))
            }
    )
    @GetMapping("api/metadata/pet-types")
    public Mono<ResponseEntity<ValidPetTypesRequest>> getValidPetTypes(){
        return Mono.just(new ValidPetTypesRequest(
                Arrays.stream(Type.values())
                        .map(Enum::name)
                        .toList()))
                .map(ResponseEntity::ok);
    }

}

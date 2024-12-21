package com.zahid.socks_api.controllers;

import com.zahid.socks_api.SocksApiApplication;
import com.zahid.socks_api.dto.SocksDto;
import com.zahid.socks_api.entity.SocksBatch;
import com.zahid.socks_api.services.SocksService;
import com.zahid.socks_api.util.SocksDataValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(
        name = "Socks controller",
        description = "Контроллер для обработки данных по учёту носков на складе"
)
@RestController
@RequestMapping("/api/socks")
public class SocksController {
    private final SocksService socksService;
    private final ModelMapper mapper;
    private final SocksDataValidator validator;
    @Autowired
    public SocksController(SocksService socksService, ModelMapper mapper, SocksDataValidator validator) {
        this.socksService = socksService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Operation(
            summary = "Регистрация прихода носков",
            parameters = @Parameter(
                    name = "SocksDto",
                    description = "Параметры: цвет носков, процентное содержание хлопка, количество." +
                            "Увеличивает количество носков на складе.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "ошибка в запросе"
                    )
            }
    )
    @PostMapping("/income")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid SocksDto socksDto) {
        SocksApiApplication.logger.info("'/api/socks/income' request received to fix income");
        validator.checkInputData(socksDto, true);
        socksService.add(convertToEntity(socksDto));
        SocksApiApplication.logger.info("Income has been fixed");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Регистрация отпуска носков",
            parameters = @Parameter(
                    name = "SocksDto",
                    description = "Параметры: цвет носков, процентное содержание хлопка, количество." +
                            "Уменьшает количество носков на складе, если их хватает.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "ошибка в запросе"
                    ),
                    @ApiResponse(
                            responseCode = "405", description = "нехватка носков на складе"
                    )
            }
    )
    @PostMapping("/outcome")
    public ResponseEntity<HttpStatus> delete(@RequestBody @Valid SocksDto socksDto) {
        SocksApiApplication.logger.info("'/api/socks/outcome' request received to fix outcome");
        validator.checkInputData(socksDto, true);
        socksService.delete(convertToEntity(socksDto));
        SocksApiApplication.logger.info("Outcome has been fixed");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Получение общего количества носков с фильтрацией",
            description = "Получить число пар носков с возможностью фильтрации по точному значению содержания " +
                    "хлопка, либо по верхней и/или нижней границе диапазона, а также по цвету носков. " +
                    "Использование одновременно фильтрации и по точному значению, и по диапазону " +
                    "исключено за отсутствием смысла в таком запросе",
            parameters = {
                    @Parameter(name = "color", description = "цвет"),
                    @Parameter(name = "equal", description = "точное значение"),
                    @Parameter(name = "lessThan", description = "верхняя граница"),
                    @Parameter(name = "moreThan", description = "нижняя граница")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "ошибка в запросе"
                    )
            }
    )
    @GetMapping
    public int count(@RequestParam(value = "color", required = false) String color,
                     @RequestParam(value = "lessThan", required = false) Integer max,
                     @RequestParam(value = "equal", required = false) Integer number,
                     @RequestParam(value = "moreThan", required = false) Integer min) {
        SocksApiApplication.logger.info("'/api/socks' request received to get pairs of socks quantity");
        validator.checkCountingParameters(min, max, number);
        return socksService.count(color, min, number, max);
    }

    @Operation(
            summary = "Обновление данных носков",
            parameters = @Parameter(
                    name = "SocksDto",
                    description = "Позволяет изменить параметры определённой по ID партии " +
                            "носков (цвет, процент хлопка, количество)" +
                            "В случае, если пользователь изменяет разновидность носков на уже " +
                            "имеющееся в базе значение, повторяющаяся запись удаляется, а размер " +
                            "удаленной партии прибавляется к обновлённой партии. Если " +
                            "пользователь не хочет изменять одну из характеристик, достаточно " +
                            "проигнорировать её в JSON, тогда значение этой характеристики не изменится.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "ошибка в запросе"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> change(@RequestBody @Valid SocksDto socksDto,
                                                  @PathVariable("id") int id) {
        SocksApiApplication.logger.info("'/api/socks/*' request received to change batch of socks with ID {}", id);
        validator.checkInputData(socksDto, false);
        socksService.change(convertToEntity(socksDto), id);
        SocksApiApplication.logger.info("Changes have been committed");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Загрузка партий носков из .xslx файла",
            parameters = @Parameter(
                    name = "XLSX-файл",
                    description = "Принимает XLSX-файл с партиями носков, содержащими цвет, " +
                            "процентное содержание хлопка и количество.",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "ошибка в запросе или файле"
                    )
            }
    )
    @PostMapping("/batch")
    public ResponseEntity<HttpStatus> uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        SocksApiApplication.logger.info("'/api/socks/batch' request received to write income info from file {}", file.getName());
        validator.checkFile(file);
        socksService.parseExcel(file);
        SocksApiApplication.logger.info("Information has been written");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(
            summary = "Просмотр находящихся на складе партий носков с возможностью фильтрации и/или сортировки",
            description = "Получить список находящихся на складе партий. Можно отсортировать по цвету" +
                    " или по содержанию хлопка, для этого параметру \"sort\" указать значения" +
                    " \"color\" либо \"cotton\" соответственно. Также можно произвести фильтрацию " +
                    "по процентному содержанию хлопка, задав диапазон либо одну из его границ",
            parameters = {
                    @Parameter(name = "sort", description = "критерий сортировки: по цвету - " +
                            "\"color\", или по содержанию хлопка - \"cotton\""),
                    @Parameter(name = "max", description = "верхняя граница"),
                    @Parameter(name = "min", description = "нижняя граница")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "запрос выполнен успешно",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SocksBatch.class))
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "ошибка в запросе"
                    )
            }
    )
    @GetMapping("/search")
    public List<SocksBatch> search(@RequestParam(value = "sort", required = false) String sortCriteria,
                                   @RequestParam(value = "max", required = false) Integer max,
                                   @RequestParam(value = "min", required = false) Integer min) {
        SocksApiApplication.logger.info("'/api/socks/search' request received to find products");
        validator.checkFilterParameters(min, max);
        return socksService.searchWithFilter(sortCriteria, min, max);
    }
    protected SocksBatch convertToEntity(SocksDto dto) {
        return mapper.map(dto, SocksBatch.class);
    }
}

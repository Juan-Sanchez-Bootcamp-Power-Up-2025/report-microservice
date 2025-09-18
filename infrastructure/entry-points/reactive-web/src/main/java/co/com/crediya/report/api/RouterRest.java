package co.com.crediya.report.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperation(
            path = "/api/v1/reports",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = Handler.class,
            beanMethod = "listenGETReport",
            operation = @Operation(
                    operationId = "listenGETReport",
                    summary = "Get approved loans report",
                    description = "Gets the report with the total count and amount of approved loans.",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200", description = "Report of approved loans",
                                    content = @Content(
                                            examples = {
                                                    @ExampleObject(name = "Report",
                                                    value = """
                                                            {
                                                                "metricKey": "total_approved_loans",
                                                                "approvedLoansCount": 4,
                                                                "approvedLoansAmount": 5468
                                                            }
                                                            """
                                                    )
                                            }
                                    )
                            ),
                            @ApiResponse(responseCode = "500", description = "Internal Error")
                    }

            )
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/reports"), handler::listenGETReport);
    }

}

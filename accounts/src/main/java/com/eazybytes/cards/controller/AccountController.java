package com.eazybytes.cards.controller;

import com.eazybytes.cards.constants.AccountsConstants;
import com.eazybytes.cards.dto.AccountsContactInfoDto;
import com.eazybytes.cards.dto.CustomerDto;
import com.eazybytes.cards.dto.ErrorResponseDto;
import com.eazybytes.cards.dto.ResponseDto;
import com.eazybytes.cards.service.IAccountsService;
import com.eazybytes.cards.service.impl.AccountsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Tag(name = "CRUD REST APIs for Accounts in EazyBank", description = "CRUD REST APIs in EazyBank to CRETE, UPDATE, FETCH and DELETE account details")
public class AccountController {
    private final IAccountsService iAccountsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment env;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    public AccountController(IAccountsService iAccountsService) {
        this.iAccountsService = iAccountsService;
    }

    @Operation(summary = "Create Account REST API", description = "REST API to create new Customer and Account inside EazyBank")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP status CREATED"), @ApiResponse(responseCode = "500", description = "HTTP status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(summary = "Fetch Account details REST API", description = "REST API to fetch Customer and Account details based on a mobile number")
    @ApiResponse(responseCode = "200", description = "HTTP status OK")
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(summary = "Update Account details REST API", description = "REST API to update Customer and Account details based on a account number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP status OK"), @ApiResponse(responseCode = "417", description = "Expectation Failed"), @ApiResponse(responseCode = "500", description = "HTTP status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(summary = "Delete Account and Customer details REST API", description = "REST API to delete Customer and Account details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP status OK"), @ApiResponse(responseCode = "417", description = "Expectation Failed"), @ApiResponse(responseCode = "500", description = "HTTP status Internal Server Error")})
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(summary = "Get build information", description = "Get build information that is deployed into accounts microservice")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP status OK"), @ApiResponse(responseCode = "500", description = "HTTP status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @Operation(summary = "Get Java version", description = "Get Java version details that is installed into accounts microservice")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP status OK"), @ApiResponse(responseCode = "500", description = "HTTP status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK).body(env.getProperty("JAVA_HOME"));
    }

    @Operation(summary = "Get Contact info", description = "Get Contact info details that can be reached out in case of any issues")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP status OK"), @ApiResponse(responseCode = "500", description = "HTTP status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDto);
    }
}
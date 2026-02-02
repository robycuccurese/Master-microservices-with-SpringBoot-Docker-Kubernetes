package com.eazybytes.accounts.dto;

import com.eazybytes.accounts.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(
        name="Accounts",
        description = "Schema to hold Account information"
)
public class AccountsDto {
    @Schema(
            description = "Account number of Eazy Bank account", example="3454433243"
    )
    @NotEmpty(message = "Account number can not be null or empty")
    @Pattern(regexp="(^$|[0-9]{10})", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account type of Eazy Bank account", example="Savings"
    )
    @NotEmpty(message = "Account type can not be null or empty")
    private String accountType;

    @Schema(
            description = "Eazy Bank branch address", example="123, New York"
    )
    @NotEmpty(message = "Branch address can not be null or empty")
    private String branchAddress;
}
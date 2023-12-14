package master.ao.accountancy.api.responses;


import lombok.Data;

import java.util.UUID;

@Data
public class SubAccountDescriptionResponse {
    private UUID subAccountId;
    private String description;
}

package master.ao.accountancy.api.responses;


import lombok.Data;

import java.util.UUID;

@Data
public class NatureResponse {
    private UUID natureId;
    private String description;
    private CategoryResponse category;
}

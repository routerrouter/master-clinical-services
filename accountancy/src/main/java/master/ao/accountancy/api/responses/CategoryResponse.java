package master.ao.accountancy.api.responses;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponse {
    private UUID categoryId;
    private String description;
}

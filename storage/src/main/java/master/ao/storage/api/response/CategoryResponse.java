package master.ao.storage.api.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryResponse {
    private UUID categoryId;
    private String name;
}
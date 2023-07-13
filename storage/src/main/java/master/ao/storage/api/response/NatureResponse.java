package master.ao.storage.api.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NatureResponse {
    private UUID natureId;
    private String name;
}
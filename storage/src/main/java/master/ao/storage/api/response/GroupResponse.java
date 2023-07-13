package master.ao.storage.api.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponse {
    private UUID groupId;
    private String name;
}
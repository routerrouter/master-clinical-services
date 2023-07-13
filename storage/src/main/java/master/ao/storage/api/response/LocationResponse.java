package master.ao.storage.api.response;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {

    public interface LocationView {
        public static interface principalList {}
        public static interface otherList {}
    }

    @JsonView({LocationView.principalList.class,LocationView.otherList.class})
    private UUID locationId;
    @JsonView({LocationView.principalList.class})
    private LocalDateTime registeredAt;
    @JsonView({LocationView.principalList.class})
    private String shelf;
    @JsonView({LocationView.principalList.class})
    private String partition;
    @JsonView({LocationView.principalList.class,LocationView.otherList.class})
    private String description;
    @JsonView({LocationView.principalList.class})
    private boolean enabeld;
}

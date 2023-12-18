package master.ao.accountancy.api.requests.filters;

import lombok.Data;
import master.ao.accountancy.domain.enums.DailyType;

import java.util.UUID;

@Data
public class DailyFilter {
    private String initialDate;
    private String finalDate;
    private UUID natureId;
    private UUID accountId;
    private DailyType type;
}

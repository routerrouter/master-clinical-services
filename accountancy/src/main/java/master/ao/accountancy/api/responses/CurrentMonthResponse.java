package master.ao.accountancy.api.responses;


import lombok.Data;

import java.util.UUID;

@Data
public class CurrentMonthResponse {
    private UUID currentYearId;
    private String year;
    private String month;
}

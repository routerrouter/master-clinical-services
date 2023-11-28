package master.ao.accountancy.api.requests;


import lombok.Data;

@Data
public class CurrentMonthRequest {
    private String year;
    private String month;
}

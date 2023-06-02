package master.ao.storage.api.request;

import lombok.Data;

@Data
public class CompanyRequest {
    private String name;
    private String phoneNumber;
    private String address;
    private String nif;
    private String responsible;
}

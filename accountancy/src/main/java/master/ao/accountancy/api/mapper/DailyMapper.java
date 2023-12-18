package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.DailyRequest;
import master.ao.accountancy.api.responses.DailyResponse;
import master.ao.accountancy.domain.models.Daily;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DailyMapper {

    private final ModelMapper mapper;

    public Daily toDaily(DailyRequest request) {
        return mapper.map(request, Daily.class);
    }

    public DailyResponse toDailyResponse(Daily daily) {
        return mapper.map(daily, DailyResponse.class);
    }

    public List<DailyResponse> toDailyResponseList(List<Daily> natureList) {
        return natureList.stream()
                .map(this::toDailyResponse)
                .collect(Collectors.toList());
    }
}

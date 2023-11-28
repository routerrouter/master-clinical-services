package master.ao.accountancy.api.mapper;

import lombok.AllArgsConstructor;
import master.ao.accountancy.api.requests.CurrentMonthRequest;
import master.ao.accountancy.api.responses.CurrentMonthResponse;
import master.ao.accountancy.domain.models.CurrentMonth;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CurrentMonthMapper {

    private final ModelMapper mapper;

    public CurrentMonth toCurrentMonth(CurrentMonthRequest request) {
        return mapper.map(request, CurrentMonth.class);
    }

    public CurrentMonthResponse toCurrentMonthResponse(CurrentMonth currentMonth) {
        return mapper.map(currentMonth, CurrentMonthResponse.class);
    }

    public List<CurrentMonthResponse> toCurrentMonthResponseList(List<CurrentMonth> currentMonthList) {
        return currentMonthList.stream()
                .map(this::toCurrentMonthResponse)
                .collect(Collectors.toList());
    }
}

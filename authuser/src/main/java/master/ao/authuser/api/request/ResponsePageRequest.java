package master.ao.authuser.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ResponsePageRequest<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageRequest(@JsonProperty("content") List<T> content,
                               @JsonProperty("number") int number,
                               @JsonProperty("size") int size,
                               @JsonProperty("totalElements") Long totalElements,
                               @JsonProperty("pageable") JsonNode pageable,
                               @JsonProperty("last") boolean last,
                               @JsonProperty("totalPages") int totalPages,
                               @JsonProperty("sort") JsonNode sort,
                               @JsonProperty("first") boolean first,
                               @JsonProperty("empty") boolean empty) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public ResponsePageRequest(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ResponsePageRequest(List<T> content) {
        super(content);
    }
}
package ae.anyorder.bigorder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Frank on 4/25/2018.
 */
public class PaginationDto {

    private Integer numberOfRows;
    private List<?> data;

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
package ae.anyorder.bigorder.dto;

import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.exception.MyException;
import lombok.Data;

import java.util.Map;

/**
 * Created by Frank on 4/25/2018.
 */
@Data
public class Page {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortOrder;
    private Integer rowNumber;
    private Integer totalRows;
    private String searchFor;
    private Map<String, String> searchFields;
    private Status status;

    public Integer getValidRowNumber() {
        if (pageNumber <= 0 || pageSize == 0) {
            throw new MyException("ERR001");
        }
        this.rowNumber = (pageNumber - 1) * pageSize;
        if (rowNumber >= totalRows) {
            throw new MyException("ERR001");
        }
        return rowNumber;
    }
}

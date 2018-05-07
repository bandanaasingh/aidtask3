package ae.anyorder.bigorder.apiModel;

import ae.anyorder.bigorder.util.JsonTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by Frank on 4/15/2018.
 */
@Data
public class ComboItem {
    private String name;
    private String imageUrl;
    private String description;
    private String overview;
    private BigDecimal unitPrice;
    private Integer minPerson;
    @Temporal(TemporalType.TIME)
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    private Date availableStartTime;
    @Temporal(TemporalType.TIME)
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    private Date availableEndTime;
    private Long categoryId;
    private List<Items> items;

}

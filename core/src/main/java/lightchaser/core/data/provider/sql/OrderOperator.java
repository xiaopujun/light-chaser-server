package lightchaser.core.data.provider.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderOperator extends ColumnOperator {

    private AggregateOperator.SqlOperator aggOperator;

    private SqlOperator operator;

    public enum SqlOperator {
        ASC,
        DESC
    }

    @Override
    public String toString() {
        return "OrderOperator{" +
                "column='" + getColumnKey() + '\'' +
                ", operator=" + operator +
                '}';
    }
}

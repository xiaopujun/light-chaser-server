package lightchaser.core.data.provider.sql;

import lightchaser.core.data.provider.StdSqlOperator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionOperator implements Operator {

    private StdSqlOperator function;

    private List<FunArg> args;

}

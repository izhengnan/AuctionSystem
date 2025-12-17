package qkl.zn.AuctionSystem.model.bo;

import java.lang.Object;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionSystemUserOrdersInputBO {
  private BigInteger arg0;

  private BigInteger arg1;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(arg0);
    args.add(arg1);
    return args;
  }
}

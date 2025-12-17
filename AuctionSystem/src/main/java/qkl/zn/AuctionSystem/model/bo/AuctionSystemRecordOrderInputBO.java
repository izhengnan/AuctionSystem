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
public class AuctionSystemRecordOrderInputBO {
  private BigInteger _itemId;

  private BigInteger _buyerId;

  private BigInteger _dealPrice;

  private BigInteger _updateTime;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_itemId);
    args.add(_buyerId);
    args.add(_dealPrice);
    args.add(_updateTime);
    return args;
  }
}

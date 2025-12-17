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
public class AuctionSystemRecordBidInputBO {
  private BigInteger _itemId;

  private BigInteger _bidderId;

  private BigInteger _bidPrice;

  private BigInteger _bidTime;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_itemId);
    args.add(_bidderId);
    args.add(_bidPrice);
    args.add(_bidTime);
    return args;
  }
}

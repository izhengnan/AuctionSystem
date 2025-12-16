package qkl.zn.AuctionSystem.service;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemBidRecordsInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemGetBidRecordsByItemInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemGetOrdersByUserInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemItemBidRecordsInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemOrdersInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemRecordBidInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemRecordOrderInputBO;
import qkl.zn.AuctionSystem.model.bo.AuctionSystemUserOrdersInputBO;

@Service
@NoArgsConstructor
@Data
public class AuctionSystemService {
  public static final String ABI = qkl.zn.AuctionSystem.utils.IOUtil.readResourceAsString("abi/AuctionSystem.abi");

  public static final String BINARY = qkl.zn.AuctionSystem.utils.IOUtil.readResourceAsString("bin/ecc/AuctionSystem.bin");

  public static final String SM_BINARY = qkl.zn.AuctionSystem.utils.IOUtil.readResourceAsString("bin/sm/AuctionSystem.bin");

  @Value("${system.contract.auctionSystemAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse recordOrder(AuctionSystemRecordOrderInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "recordOrder", input.toArgs());
  }

  public CallResponse orders(AuctionSystemOrdersInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "orders", input.toArgs());
  }

  public CallResponse bidRecords(AuctionSystemBidRecordsInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "bidRecords", input.toArgs());
  }

  public CallResponse nextOrderId() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "nextOrderId", Arrays.asList());
  }

  public CallResponse getOrdersByUser(AuctionSystemGetOrdersByUserInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getOrdersByUser", input.toArgs());
  }

  public TransactionResponse recordBid(AuctionSystemRecordBidInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "recordBid", input.toArgs());
  }

  public CallResponse nextBidRecordId() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "nextBidRecordId", Arrays.asList());
  }

  public CallResponse userOrders(AuctionSystemUserOrdersInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "userOrders", input.toArgs());
  }

  public CallResponse itemBidRecords(AuctionSystemItemBidRecordsInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "itemBidRecords", input.toArgs());
  }

  public CallResponse getBidRecordsByItem(AuctionSystemGetBidRecordsByItemInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getBidRecordsByItem", input.toArgs());
  }
}

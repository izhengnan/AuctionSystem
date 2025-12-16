import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Bool;
import org.fisco.bcos.sdk.abi.datatypes.DynamicArray;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class AuctionSystem extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50600160008190555060018081905550610fea8061002f6000396000f3006080604052600436106100a4576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306703c65146100a95780630dc8c9b3146100f4578063142d8a3e146101565780632a58b330146101a157806379ba5664146101cc57806379fe1130146102175780639bab0586146103b9578063a13b019614610421578063a85c38ef1461060b578063e652ba7614610680575b600080fd5b3480156100b557600080fd5b506100de60048036038101908080359060200190929190803590602001909291905050506106ab565b6040518082815260200191505060405180910390f35b34801561010057600080fd5b5061015460048036038101908080359060200190929190803590602001909291908035906020019092919080359060200190929190803560ff169060200190929190803590602001909291905050506106db565b005b34801561016257600080fd5b5061018b6004803603810190808035906020019092919080359060200190929190505050610846565b6040518082815260200191505060405180910390f35b3480156101ad57600080fd5b506101b6610876565b6040518082815260200191505060405180910390f35b3480156101d857600080fd5b506102156004803603810190808035906020019092919080359060200190929190803590602001909291908035906020019092919050505061087c565b005b34801561022357600080fd5b50610242600480360381019080803590602001909291905050506109d3565b60405180806020018060200180602001806020018060200186810386528b818151815260200191508051906020019060200280838360005b8381101561029557808201518184015260208101905061027a565b5050505090500186810385528a818151815260200191508051906020019060200280838360005b838110156102d75780820151818401526020810190506102bc565b50505050905001868103845289818151815260200191508051906020019060200280838360005b838110156103195780820151818401526020810190506102fe565b50505050905001868103835288818151815260200191508051906020019060200280838360005b8381101561035b578082015181840152602081019050610340565b50505050905001868103825287818151815260200191508051906020019060200280838360005b8381101561039d578082015181840152602081019050610382565b505050509050019a505050505050505050505060405180910390f35b3480156103c557600080fd5b506103e460048036038101908080359060200190929190505050610bff565b6040518087815260200186815260200185815260200184815260200183815260200182151515158152602001965050505050505060405180910390f35b34801561042d57600080fd5b5061044c60048036038101908080359060200190929190505050610c48565b6040518080602001806020018060200180602001806020018060200187810387528d818151815260200191508051906020019060200280838360005b838110156104a3578082015181840152602081019050610488565b5050505090500187810386528c818151815260200191508051906020019060200280838360005b838110156104e55780820151818401526020810190506104ca565b5050505090500187810385528b818151815260200191508051906020019060200280838360005b8381101561052757808201518184015260208101905061050c565b5050505090500187810384528a818151815260200191508051906020019060200280838360005b8381101561056957808201518184015260208101905061054e565b50505050905001878103835289818151815260200191508051906020019060200280838360005b838110156105ab578082015181840152602081019050610590565b50505050905001878103825288818151815260200191508051906020019060200280838360005b838110156105ed5780820151818401526020810190506105d2565b505050509050019c5050505050505050505050505060405180910390f35b34801561061757600080fd5b5061063660048036038101908080359060200190929190505050610ee0565b604051808881526020018781526020018681526020018581526020018460ff1660ff1681526020018381526020018215151515815260200197505050505050505060405180910390f35b34801561068c57600080fd5b50610695610f3c565b6040518082815260200191505060405180910390f35b6005602052816000526040600020818154811015156106c657fe5b90600052602060002001600091509150505481565b6106e3610f42565b60e0604051908101604052808881526020018781526020018681526020018581526020018460ff16815260200183815260200160011515815250905080600360008981526020019081526020016000206000820151816000015560208201518160010155604082015181600201556060820151816003015560808201518160040160006101000a81548160ff021916908360ff16021790555060a0820151816005015560c08201518160060160006101000a81548160ff021916908315150217905550905050600560008681526020019081526020016000208790806001815401808255809150509060018203906000526020600020016000909192909190915055507f2521a6ea0202bdb001dba7fb1eef0d8a83632c7974c4a84839456147ee95e8698787878786604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390a150505050505050565b60046020528160005260406000208181548110151561086157fe5b90600052602060002001600091509150505481565b60015481565b610884610f85565b60c0604051908101604052806000548152602001868152602001858152602001848152602001838152602001600115158152509050806002600080548152602001908152602001600020600082015181600001556020820151816001015560408201518160020155606082015181600301556080820151816004015560a08201518160050160006101000a81548160ff0219169083151502179055509050506004600086815260200190815260200160002060005490806001815401808255809150509060018203906000526020600020016000909192909190915055507f58d410dc1370a220fa6d06b24c35ae795427433dcaed19cfc32cc56b77dfba4260005486868686604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390a160008081548092919060010191905055505050505050565b6060806060806060600080600080600460008b815260200190815260200160002093508380549050925082604051908082528060200260200182016040528015610a2c5781602001602082028038833980820191505090505b50985082604051908082528060200260200182016040528015610a5e5781602001602082028038833980820191505090505b50975082604051908082528060200260200182016040528015610a905781602001602082028038833980820191505090505b50965082604051908082528060200260200182016040528015610ac25781602001602082028038833980820191505090505b50955082604051908082528060200260200182016040528015610af45781602001602082028038833980820191505090505b509450600091505b82821015610be357600260008584815481101515610b1657fe5b90600052602060002001548152602001908152602001600020905080600001548983815181101515610b4457fe5b906020019060200201818152505080600101548883815181101515610b6557fe5b906020019060200201818152505080600201548783815181101515610b8657fe5b906020019060200201818152505080600301548683815181101515610ba757fe5b906020019060200201818152505080600401548583815181101515610bc857fe5b90602001906020020181815250508180600101925050610afc565b8888888888985098509850985098505050505091939590929450565b60026020528060005260406000206000915090508060000154908060010154908060020154908060030154908060040154908060050160009054906101000a900460ff16905086565b606080606080606080600080600080600560008c815260200190815260200160002093508380549050925082604051908082528060200260200182016040528015610ca25781602001602082028038833980820191505090505b50995082604051908082528060200260200182016040528015610cd45781602001602082028038833980820191505090505b50985082604051908082528060200260200182016040528015610d065781602001602082028038833980820191505090505b50975082604051908082528060200260200182016040528015610d385781602001602082028038833980820191505090505b50965082604051908082528060200260200182016040528015610d6a5781602001602082028038833980820191505090505b50955082604051908082528060200260200182016040528015610d9c5781602001602082028038833980820191505090505b509450600091505b82821015610ec157600360008584815481101515610dbe57fe5b90600052602060002001548152602001908152602001600020905080600001548a83815181101515610dec57fe5b906020019060200201818152505080600101548983815181101515610e0d57fe5b906020019060200201818152505080600201548883815181101515610e2e57fe5b906020019060200201818152505080600301548783815181101515610e4f57fe5b90602001906020020181815250508060040160009054906101000a900460ff168683815181101515610e7d57fe5b9060200190602002019060ff16908160ff168152505080600501548583815181101515610ea657fe5b90602001906020020181815250508180600101925050610da4565b8989898989899950995099509950995099505050505091939550919395565b60036020528060005260406000206000915090508060000154908060010154908060020154908060030154908060040160009054906101000a900460ff16908060050154908060060160009054906101000a900460ff16905087565b60005481565b60e06040519081016040528060008152602001600081526020016000815260200160008152602001600060ff168152602001600081526020016000151581525090565b60c060405190810160405280600081526020016000815260200160008152602001600081526020016000815260200160001515815250905600a165627a7a72305820268eee6fc552c9639e","0301b78915e4236c4349482a4bfcc6c328efb21eee9ee20029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"userOrders\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_orderId\",\"type\":\"uint256\"},{\"name\":\"_itemId\",\"type\":\"uint256\"},{\"name\":\"_buyerId\",\"type\":\"uint256\"},{\"name\":\"_dealPrice\",\"type\":\"uint256\"},{\"name\":\"_status\",\"type\":\"uint8\"},{\"name\":\"_updateTime\",\"type\":\"uint256\"}],\"name\":\"recordOrder\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"itemBidRecords\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"nextOrderId\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_itemId\",\"type\":\"uint256\"},{\"name\":\"_bidderId\",\"type\":\"uint256\"},{\"name\":\"_bidPrice\",\"type\":\"uint256\"},{\"name\":\"_bidTime\",\"type\":\"uint256\"}],\"name\":\"recordBid\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_itemId\",\"type\":\"uint256\"}],\"name\":\"getBidRecordsByItem\",\"outputs\":[{\"name\":\"ids\",\"type\":\"uint256[]\"},{\"name\":\"itemIds\",\"type\":\"uint256[]\"},{\"name\":\"bidderIds\",\"type\":\"uint256[]\"},{\"name\":\"bidPrices\",\"type\":\"uint256[]\"},{\"name\":\"bidTimes\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"bidRecords\",\"outputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"itemId\",\"type\":\"uint256\"},{\"name\":\"bidderId\",\"type\":\"uint256\"},{\"name\":\"bidPrice\",\"type\":\"uint256\"},{\"name\":\"bidTime\",\"type\":\"uint256\"},{\"name\":\"isExist\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_userId\",\"type\":\"uint256\"}],\"name\":\"getOrdersByUser\",\"outputs\":[{\"name\":\"ids\",\"type\":\"uint256[]\"},{\"name\":\"itemIds\",\"type\":\"uint256[]\"},{\"name\":\"buyerIds\",\"type\":\"uint256[]\"},{\"name\":\"dealPrices\",\"type\":\"uint256[]\"},{\"name\":\"statuses\",\"type\":\"uint8[]\"},{\"name\":\"updateTimes\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"orders\",\"outputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"itemId\",\"type\":\"uint256\"},{\"name\":\"buyerId\",\"type\":\"uint256\"},{\"name\":\"dealPrice\",\"type\":\"uint256\"},{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"updateTime\",\"type\":\"uint256\"},{\"name\":\"isExist\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"nextBidRecordId\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"recordId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"itemId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"bidderId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"price\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"bidTime\",\"type\":\"uint256\"}],\"name\":\"BidPlaced\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"orderId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"itemId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"buyerId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"dealPrice\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"updateTime\",\"type\":\"uint256\"}],\"name\":\"OrderCreated\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_USERORDERS = "userOrders";

    public static final String FUNC_RECORDORDER = "recordOrder";

    public static final String FUNC_ITEMBIDRECORDS = "itemBidRecords";

    public static final String FUNC_NEXTORDERID = "nextOrderId";

    public static final String FUNC_RECORDBID = "recordBid";

    public static final String FUNC_GETBIDRECORDSBYITEM = "getBidRecordsByItem";

    public static final String FUNC_BIDRECORDS = "bidRecords";

    public static final String FUNC_GETORDERSBYUSER = "getOrdersByUser";

    public static final String FUNC_ORDERS = "orders";

    public static final String FUNC_NEXTBIDRECORDID = "nextBidRecordId";

    public static final Event BIDPLACED_EVENT = new Event("BidPlaced", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ORDERCREATED_EVENT = new Event("OrderCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected AuctionSystem(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public BigInteger userOrders(BigInteger param0, BigInteger param1) throws ContractException {
        final Function function = new Function(FUNC_USERORDERS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt recordOrder(BigInteger _orderId, BigInteger _itemId, BigInteger _buyerId, BigInteger _dealPrice, BigInteger _status, BigInteger _updateTime) {
        final Function function = new Function(
                FUNC_RECORDORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_orderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_buyerId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dealPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(_status), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void recordOrder(BigInteger _orderId, BigInteger _itemId, BigInteger _buyerId, BigInteger _dealPrice, BigInteger _status, BigInteger _updateTime, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RECORDORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_orderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_buyerId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dealPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(_status), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRecordOrder(BigInteger _orderId, BigInteger _itemId, BigInteger _buyerId, BigInteger _dealPrice, BigInteger _status, BigInteger _updateTime) {
        final Function function = new Function(
                FUNC_RECORDORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_orderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_buyerId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dealPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint8(_status), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> getRecordOrderInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RECORDORDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue(), 
                (BigInteger) results.get(5).getValue()
                );
    }

    public BigInteger itemBidRecords(BigInteger param0, BigInteger param1) throws ContractException {
        final Function function = new Function(FUNC_ITEMBIDRECORDS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(param0), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public BigInteger nextOrderId() throws ContractException {
        final Function function = new Function(FUNC_NEXTORDERID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public TransactionReceipt recordBid(BigInteger _itemId, BigInteger _bidderId, BigInteger _bidPrice, BigInteger _bidTime) {
        final Function function = new Function(
                FUNC_RECORDBID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void recordBid(BigInteger _itemId, BigInteger _bidderId, BigInteger _bidPrice, BigInteger _bidTime, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RECORDBID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidTime)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRecordBid(BigInteger _itemId, BigInteger _bidderId, BigInteger _bidPrice, BigInteger _bidTime) {
        final Function function = new Function(
                FUNC_RECORDBID, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_bidTime)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> getRecordBidInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RECORDBID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<BigInteger, BigInteger, BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue()
                );
    }

    public Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>> getBidRecordsByItem(BigInteger _itemId) throws ContractException {
        final Function function = new Function(FUNC_GETBIDRECORDSBYITEM, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>>(
                convertToNative((List<Uint256>) results.get(0).getValue()), 
                convertToNative((List<Uint256>) results.get(1).getValue()), 
                convertToNative((List<Uint256>) results.get(2).getValue()), 
                convertToNative((List<Uint256>) results.get(3).getValue()), 
                convertToNative((List<Uint256>) results.get(4).getValue()));
    }

    public Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> bidRecords(BigInteger param0) throws ContractException {
        final Function function = new Function(FUNC_BIDRECORDS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>(
                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue(), 
                (Boolean) results.get(5).getValue());
    }

    public Tuple6<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>> getOrdersByUser(BigInteger _userId) throws ContractException {
        final Function function = new Function(FUNC_GETORDERSBYUSER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_userId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint8>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple6<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>, List<BigInteger>>(
                convertToNative((List<Uint256>) results.get(0).getValue()), 
                convertToNative((List<Uint256>) results.get(1).getValue()), 
                convertToNative((List<Uint256>) results.get(2).getValue()), 
                convertToNative((List<Uint256>) results.get(3).getValue()), 
                convertToNative((List<Uint8>) results.get(4).getValue()), 
                convertToNative((List<Uint256>) results.get(5).getValue()));
    }

    public Tuple7<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> orders(BigInteger param0) throws ContractException {
        final Function function = new Function(FUNC_ORDERS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple7<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>(
                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue(), 
                (BigInteger) results.get(5).getValue(), 
                (Boolean) results.get(6).getValue());
    }

    public BigInteger nextBidRecordId() throws ContractException {
        final Function function = new Function(FUNC_NEXTBIDRECORDID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public List<BidPlacedEventResponse> getBidPlacedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BIDPLACED_EVENT, transactionReceipt);
        ArrayList<BidPlacedEventResponse> responses = new ArrayList<BidPlacedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BidPlacedEventResponse typedResponse = new BidPlacedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recordId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.itemId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.bidderId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.bidTime = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeBidPlacedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(BIDPLACED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeBidPlacedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(BIDPLACED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<OrderCreatedEventResponse> getOrderCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ORDERCREATED_EVENT, transactionReceipt);
        ArrayList<OrderCreatedEventResponse> responses = new ArrayList<OrderCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrderCreatedEventResponse typedResponse = new OrderCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.orderId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.itemId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.buyerId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.dealPrice = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.updateTime = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeOrderCreatedEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ORDERCREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeOrderCreatedEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ORDERCREATED_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static AuctionSystem load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AuctionSystem(contractAddress, client, credential);
    }

    public static AuctionSystem deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(AuctionSystem.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class BidPlacedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger recordId;

        public BigInteger itemId;

        public BigInteger bidderId;

        public BigInteger price;

        public BigInteger bidTime;
    }

    public static class OrderCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger orderId;

        public BigInteger itemId;

        public BigInteger buyerId;

        public BigInteger dealPrice;

        public BigInteger updateTime;
    }
}

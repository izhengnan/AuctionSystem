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
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5060016000819055506001808190555061123b8061002f6000396000f3006080604052600436106100af576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306703c65146100b4578063142d8a3e146100ff5780632a58b3301461014a5780633a201f6d1461017557806379ba5664146101d457806379fe11301461021f5780639bab0586146103c1578063a13b019614610429578063a85c38ef14610613578063d2fbd0ed14610688578063e652ba76146106bf575b600080fd5b3480156100c057600080fd5b506100e960048036038101908080359060200190929190803590602001909291905050506106ea565b6040518082815260200191505060405180910390f35b34801561010b57600080fd5b50610134600480360381019080803590602001909291908035906020019092919050505061071a565b6040518082815260200191505060405180910390f35b34801561015657600080fd5b5061015f61074a565b6040518082815260200191505060405180910390f35b34801561018157600080fd5b506101be60048036038101908080359060200190929190803590602001909291908035906020019092919080359060200190929190505050610750565b6040518082815260200191505060405180910390f35b3480156101e057600080fd5b5061021d600480360381019080803590602001909291908035906020019092919080359060200190929190803590602001909291905050506108e3565b005b34801561022b57600080fd5b5061024a60048036038101908080359060200190929190505050610a3a565b60405180806020018060200180602001806020018060200186810386528b818151815260200191508051906020019060200280838360005b8381101561029d578082015181840152602081019050610282565b5050505090500186810385528a818151815260200191508051906020019060200280838360005b838110156102df5780820151818401526020810190506102c4565b50505050905001868103845289818151815260200191508051906020019060200280838360005b83811015610321578082015181840152602081019050610306565b50505050905001868103835288818151815260200191508051906020019060200280838360005b83811015610363578082015181840152602081019050610348565b50505050905001868103825287818151815260200191508051906020019060200280838360005b838110156103a557808201518184015260208101905061038a565b505050509050019a505050505050505050505060405180910390f35b3480156103cd57600080fd5b506103ec60048036038101908080359060200190929190505050610c66565b6040518087815260200186815260200185815260200184815260200183815260200182151515158152602001965050505050505060405180910390f35b34801561043557600080fd5b5061045460048036038101908080359060200190929190505050610caf565b6040518080602001806020018060200180602001806020018060200187810387528d818151815260200191508051906020019060200280838360005b838110156104ab578082015181840152602081019050610490565b5050505090500187810386528c818151815260200191508051906020019060200280838360005b838110156104ed5780820151818401526020810190506104d2565b5050505090500187810385528b818151815260200191508051906020019060200280838360005b8381101561052f578082015181840152602081019050610514565b5050505090500187810384528a818151815260200191508051906020019060200280838360005b83811015610571578082015181840152602081019050610556565b50505050905001878103835289818151815260200191508051906020019060200280838360005b838110156105b3578082015181840152602081019050610598565b50505050905001878103825288818151815260200191508051906020019060200280838360005b838110156105f55780820151818401526020810190506105da565b505050509050019c5050505050505050505050505060405180910390f35b34801561061f57600080fd5b5061063e60048036038101908080359060200190929190505050610f47565b604051808881526020018781526020018681526020018581526020018460ff1660ff1681526020018381526020018215151515815260200197505050505050505060405180910390f35b34801561069457600080fd5b506106bd6004803603810190808035906020019092919080359060200190929190505050610fa3565b005b3480156106cb57600080fd5b506106d461118d565b6040518082815260200191505060405180910390f35b60056020528160005260406000208181548110151561070557fe5b90600052602060002001600091509150505481565b60046020528160005260406000208181548110151561073557fe5b90600052602060002001600091509150505481565b60015481565b600061075a611193565b600060e0604051908101604052806001548152602001888152602001878152602001868152602001600060ff168152602001858152602001600115158152509150816003600060015481526020019081526020016000206000820151816000015560208201518160010155604082015181600201556060820151816003015560808201518160040160006101000a81548160ff021916908360ff16021790555060a0820151816005015560c08201518160060160006101000a81548160ff0219169083151502179055509050506005600087815260200190815260200160002060015490806001815401808255809150509060018203906000526020600020016000909192909190915055507f2521a6ea0202bdb001dba7fb1eef0d8a83632c7974c4a84839456147ee95e86960015488888888604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390a160015490506001600081548092919060010191905055508092505050949350505050565b6108eb6111d6565b60c0604051908101604052806000548152602001868152602001858152602001848152602001838152602001600115158152509050806002600080548152602001908152602001600020600082015181600001556020820151816001015560408201518160020155606082015181600301556080820151816004015560a08201518160050160006101000a81548160ff0219169083151502179055509050506004600086815260200190815260200160002060005490806001815401808255809150509060018203906000526020600020016000909192909190915055507f58d410dc1370a220fa6d06b24c35ae795427433dcaed19cfc32cc56b77dfba4260005486868686604051808681526020018581526020018481526020018381526020018281526020019550505050505060405180910390a160008081548092919060010191905055505050505050565b6060806060806060600080600080600460008b815260200190815260200160002093508380549050925082604051908082528060200260200182016040528015610a935781602001602082028038833980820191505090505b50985082604051908082528060200260200182016040528015610ac55781602001602082028038833980820191505090505b50975082604051908082528060200260200182016040528015610af75781602001602082028038833980820191505090505b50965082604051908082528060200260200182016040528015610b295781602001602082028038833980820191505090505b50955082604051908082528060200260200182016040528015610b5b5781602001602082028038833980820191505090505b509450600091505b82821015610c4a57600260008584815481101515610b7d57fe5b90600052602060002001548152602001908152602001600020905080600001548983815181101515610bab57fe5b906020019060200201818152505080600101548883815181101515610bcc57fe5b906020019060200201818152505080600201548783815181101515610bed57fe5b906020019060200201818152505080600301548683815181101515610c0e57fe5b906020019060200201818152505080600401548583815181101515610c2f57fe5b90602001906020020181815250508180600101925050610b63565b8888888888985098509850985098505050505091939590929450565b60026020528060005260406000206000915090508060000154908060010154908060020154908060030154908060040154908060050160009054906101000a900460ff16905086565b606080606080606080600080600080600560008c815260200190815260200160002093508380549050925082604051908082528060200260200182016040528015610d095781602001602082028038833980820191505090505b50995082604051908082528060200260200182016040528015610d3b5781602001602082028038833980820191505090505b50985082604051908082528060200260200182016040528015610d6d5781602001602082028038833980820191505090505b50975082604051908082528060200260200182016040528015610d9f5781602001602082028038833980820191505090505b50965082604051908082528060200260200182016040528015610dd15781602001602082028038833980820191505090505b50955082604051908082528060200260200182016040528015610e035781602001602082028038833980820191505090505b509450600091505b82821015610f2857600360008584815481101515610e2557fe5b90600052602060002001548152602001908152602001600020905080600001548a83815181101515610e5357fe5b906020019060200201818152505080600101548983815181101515610e7457fe5b906020019060200201818152505080600201548883815181101515610e9557fe5b906020019060200201818152505080600301548783815181101515610eb657fe5b90602001906020020181815250508060040160009054906101000a900460ff168683815181101515610ee457fe5b9060200190602002019060ff16908160ff168152505080600501548583815181101515610f0d57fe5b90602001906020020181815250508180600101925050610e0b565b8989898989899950995099509950995099505050505091939550919395565b60036020528060005260406000206000915090508060000154908060010154908060020154908060030154908060040160009054906101000a900460ff16908060050154908060060160009054906101000a900460ff16905087565b6003600083815260200190815260200160002060060160009054906101000a900460ff16151561103b57604051","7f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f4f7264657220646f6573206e6f7420657869737400000000000000000000000081525060200191505060405180910390fd5b60006003600084815260200190815260200160002060040160009054906101000a900460ff1660ff161415156110ff576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f4f72646572206973206e6f7420696e2070656e64696e67207061796d656e742081526020017f737461747573000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60016003600084815260200190815260200160002060040160006101000a81548160ff021916908360ff1602179055508060036000848152602001908152602001600020600501819055507f7c32ca00142334d942c919d338fa31921d5108c316aebd0347bcda69ee8167c98282604051808381526020018281526020019250505060405180910390a15050565b60005481565b60e06040519081016040528060008152602001600081526020016000815260200160008152602001600060ff168152602001600081526020016000151581525090565b60c060405190810160405280600081526020016000815260200160008152602001600081526020016000815260200160001515815250905600a165627a7a72305820936363cfc5b087704ea2ea8844aeb7e2dc6b09108c7c8825c8ddaf62e5cab9a80029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"userOrders\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"},{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"itemBidRecords\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"nextOrderId\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_itemId\",\"type\":\"uint256\"},{\"name\":\"_buyerId\",\"type\":\"uint256\"},{\"name\":\"_dealPrice\",\"type\":\"uint256\"},{\"name\":\"_updateTime\",\"type\":\"uint256\"}],\"name\":\"recordOrder\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_itemId\",\"type\":\"uint256\"},{\"name\":\"_bidderId\",\"type\":\"uint256\"},{\"name\":\"_bidPrice\",\"type\":\"uint256\"},{\"name\":\"_bidTime\",\"type\":\"uint256\"}],\"name\":\"recordBid\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_itemId\",\"type\":\"uint256\"}],\"name\":\"getBidRecordsByItem\",\"outputs\":[{\"name\":\"ids\",\"type\":\"uint256[]\"},{\"name\":\"itemIds\",\"type\":\"uint256[]\"},{\"name\":\"bidderIds\",\"type\":\"uint256[]\"},{\"name\":\"bidPrices\",\"type\":\"uint256[]\"},{\"name\":\"bidTimes\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"bidRecords\",\"outputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"itemId\",\"type\":\"uint256\"},{\"name\":\"bidderId\",\"type\":\"uint256\"},{\"name\":\"bidPrice\",\"type\":\"uint256\"},{\"name\":\"bidTime\",\"type\":\"uint256\"},{\"name\":\"isExist\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_userId\",\"type\":\"uint256\"}],\"name\":\"getOrdersByUser\",\"outputs\":[{\"name\":\"ids\",\"type\":\"uint256[]\"},{\"name\":\"itemIds\",\"type\":\"uint256[]\"},{\"name\":\"buyerIds\",\"type\":\"uint256[]\"},{\"name\":\"dealPrices\",\"type\":\"uint256[]\"},{\"name\":\"statuses\",\"type\":\"uint8[]\"},{\"name\":\"updateTimes\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"orders\",\"outputs\":[{\"name\":\"id\",\"type\":\"uint256\"},{\"name\":\"itemId\",\"type\":\"uint256\"},{\"name\":\"buyerId\",\"type\":\"uint256\"},{\"name\":\"dealPrice\",\"type\":\"uint256\"},{\"name\":\"status\",\"type\":\"uint8\"},{\"name\":\"updateTime\",\"type\":\"uint256\"},{\"name\":\"isExist\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_orderId\",\"type\":\"uint256\"},{\"name\":\"_updateTime\",\"type\":\"uint256\"}],\"name\":\"payOrder\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"nextBidRecordId\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"recordId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"itemId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"bidderId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"price\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"bidTime\",\"type\":\"uint256\"}],\"name\":\"BidPlaced\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"orderId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"itemId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"buyerId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"dealPrice\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"updateTime\",\"type\":\"uint256\"}],\"name\":\"OrderCreated\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"orderId\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"updateTime\",\"type\":\"uint256\"}],\"name\":\"OrderPaid\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_USERORDERS = "userOrders";

    public static final String FUNC_ITEMBIDRECORDS = "itemBidRecords";

    public static final String FUNC_NEXTORDERID = "nextOrderId";

    public static final String FUNC_RECORDORDER = "recordOrder";

    public static final String FUNC_RECORDBID = "recordBid";

    public static final String FUNC_GETBIDRECORDSBYITEM = "getBidRecordsByItem";

    public static final String FUNC_BIDRECORDS = "bidRecords";

    public static final String FUNC_GETORDERSBYUSER = "getOrdersByUser";

    public static final String FUNC_ORDERS = "orders";

    public static final String FUNC_PAYORDER = "payOrder";

    public static final String FUNC_NEXTBIDRECORDID = "nextBidRecordId";

    public static final Event BIDPLACED_EVENT = new Event("BidPlaced", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ORDERCREATED_EVENT = new Event("OrderCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ORDERPAID_EVENT = new Event("OrderPaid", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
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

    public TransactionReceipt recordOrder(BigInteger _itemId, BigInteger _buyerId, BigInteger _dealPrice, BigInteger _updateTime) {
        final Function function = new Function(
                FUNC_RECORDORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_buyerId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dealPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void recordOrder(BigInteger _itemId, BigInteger _buyerId, BigInteger _dealPrice, BigInteger _updateTime, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_RECORDORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_buyerId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dealPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRecordOrder(BigInteger _itemId, BigInteger _buyerId, BigInteger _dealPrice, BigInteger _updateTime) {
        final Function function = new Function(
                FUNC_RECORDORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_itemId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_buyerId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_dealPrice), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<BigInteger, BigInteger, BigInteger, BigInteger> getRecordOrderInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_RECORDORDER, 
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

    public Tuple1<BigInteger> getRecordOrderOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_RECORDORDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
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

    public TransactionReceipt payOrder(BigInteger _orderId, BigInteger _updateTime) {
        final Function function = new Function(
                FUNC_PAYORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_orderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void payOrder(BigInteger _orderId, BigInteger _updateTime, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_PAYORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_orderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForPayOrder(BigInteger _orderId, BigInteger _updateTime) {
        final Function function = new Function(
                FUNC_PAYORDER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_orderId), 
                new org.fisco.bcos.sdk.abi.datatypes.generated.Uint256(_updateTime)), 
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<BigInteger, BigInteger> getPayOrderInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_PAYORDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
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

    public List<OrderPaidEventResponse> getOrderPaidEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ORDERPAID_EVENT, transactionReceipt);
        ArrayList<OrderPaidEventResponse> responses = new ArrayList<OrderPaidEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrderPaidEventResponse typedResponse = new OrderPaidEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.orderId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.updateTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeOrderPaidEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ORDERPAID_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeOrderPaidEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ORDERPAID_EVENT);
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

    public static class OrderPaidEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger orderId;

        public BigInteger updateTime;
    }
}

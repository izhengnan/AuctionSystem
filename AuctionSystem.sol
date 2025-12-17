pragma solidity ^0.4.25;

contract AuctionSystem {
    // ========== 结构体定义 ==========
    // 竞拍记录结构体
    struct BidRecord {
        uint256 id;            // 记录ID（自增）
        uint256 itemId;        // 关联拍品ID
        uint256 bidderId;      // 出价者ID
        uint256 bidPrice;      // 出价金额（wei）
        uint256 bidTime;       // 出价时间戳
        bool isExist;          // 是否存在
    }

    // 订单结构体
    struct Order {
        uint256 id;            // 订单ID（自增）
        uint256 itemId;        // 关联拍品ID
        uint256 buyerId;       // 买家ID
        uint256 dealPrice;     // 成交价格（wei）
        uint8 status;          // 订单状态：0-待付款，1-已完成，2-流拍
        uint256 updateTime;    // 更新时间戳
        bool isExist;          // 是否存在
    }

    // ========== 全局状态变量 ==========
    uint256 public nextBidRecordId; // 下一个竞拍记录ID
    uint256 public nextOrderId; // 下一个订单ID
    mapping(uint256 => BidRecord) public bidRecords; // 竞拍记录ID→记录
    mapping(uint256 => Order) public orders; // 订单ID→订单
    mapping(uint256 => uint256[]) public itemBidRecords; // 拍品ID→竞拍记录ID列表
    mapping(uint256 => uint256[]) public userOrders; // 用户ID→订单ID列表

    // ========== 事件定义（供前端/WeBASE监听） ==========
    event BidPlaced(uint256 recordId, uint256 itemId, uint256 bidderId, uint256 price, uint256 bidTime); // 出价事件
    event OrderCreated(uint256 orderId, uint256 itemId, uint256 buyerId, uint256 dealPrice, uint256 updateTime); // 订单创建事件
    event OrderPaid(uint256 orderId, uint256 updateTime); // 订单付款事件

    // ========== 构造函数（部署时执行） ==========
    constructor() public {
        nextBidRecordId = 1;
        nextOrderId = 1;
    }

    // ========== 竞拍记录上链功能 ==========
    /**
     * @dev 记录竞拍行为上链
     * @param _itemId 拍品ID
     * @param _bidderId 出价者ID
     * @param _bidPrice 出价金额
     * @param _bidTime 出价时间
     */
    function recordBid(uint256 _itemId, uint256 _bidderId, uint256 _bidPrice, uint256 _bidTime) public {
        BidRecord memory record = BidRecord({
            id: nextBidRecordId,
            itemId: _itemId,
            bidderId: _bidderId,
            bidPrice: _bidPrice,
            bidTime: _bidTime,
            isExist: true
        });
        bidRecords[nextBidRecordId] = record;
        itemBidRecords[_itemId].push(nextBidRecordId); // 关联拍品和竞拍记录
        emit BidPlaced(nextBidRecordId, _itemId, _bidderId, _bidPrice, _bidTime);
        nextBidRecordId++;
    }

    // ========== 拍卖成交结果上链功能 ==========
    /**
     * @dev 记录拍卖成交结果上链
     * @param _itemId 拍品ID
     * @param _buyerId 买家ID
     * @param _dealPrice 成交价格
     * @param _updateTime 更新时间戳
     */
    function recordOrder(uint256 _itemId, uint256 _buyerId, uint256 _dealPrice, uint256 _updateTime) public returns (uint256) {
        Order memory order = Order({
            id: nextOrderId,
            itemId: _itemId,
            buyerId: _buyerId,
            dealPrice: _dealPrice,
            status: 0, // 默认状态为待付款
            updateTime: _updateTime,
            isExist: true
        });
        orders[nextOrderId] = order;
        userOrders[_buyerId].push(nextOrderId); // 关联用户ID和订单
        emit OrderCreated(nextOrderId, _itemId, _buyerId, _dealPrice, _updateTime);
        uint256 orderId = nextOrderId;
        nextOrderId++;
        return orderId;
    }

    // ========== 订单付款功能 ==========
    /**
     * @dev 订单付款，更新订单状态为已完成
     * @param _orderId 订单ID
     * @param _updateTime 更新时间戳
     */
    function payOrder(uint256 _orderId, uint256 _updateTime) public {
        require(orders[_orderId].isExist, "Order does not exist");
        require(orders[_orderId].status == 0, "Order is not in pending payment status");
        
        orders[_orderId].status = 1; // 更新状态为已完成
        orders[_orderId].updateTime = _updateTime;
        emit OrderPaid(_orderId, _updateTime);
    }

    // ========== 查询功能 ==========
    /**
     * @dev 根据拍品ID查询所有竞拍记录
     * @param _itemId 拍品ID
     * @return 竞拍记录列表
     */
    function getBidRecordsByItem(uint256 _itemId) public view returns (
        uint256[] ids,
        uint256[] itemIds,
        uint256[] bidderIds,
        uint256[] bidPrices,
        uint256[] bidTimes
    ) {
        uint256[] storage recordIds = itemBidRecords[_itemId];
        uint256 length = recordIds.length;
        
        ids = new uint256[](length);
        itemIds = new uint256[](length);
        bidderIds = new uint256[](length);
        bidPrices = new uint256[](length);
        bidTimes = new uint256[](length);
        
        for (uint256 i = 0; i < length; i++) {
            BidRecord storage record = bidRecords[recordIds[i]];
            ids[i] = record.id;
            itemIds[i] = record.itemId;
            bidderIds[i] = record.bidderId;
            bidPrices[i] = record.bidPrice;
            bidTimes[i] = record.bidTime;
        }
        
        return (ids, itemIds, bidderIds, bidPrices, bidTimes);
    }

    /**
     * @dev 根据用户ID查询所有订单信息
     * @param _userId 用户ID
     * @return 订单信息列表
     */
    function getOrdersByUser(uint256 _userId) public view returns (
        uint256[] ids,
        uint256[] itemIds,
        uint256[] buyerIds,
        uint256[] dealPrices,
        uint8[] statuses,
        uint256[] updateTimes
    ) {
        uint256[] storage orderIds = userOrders[_userId];
        uint256 length = orderIds.length;
        
        ids = new uint256[](length);
        itemIds = new uint256[](length);
        buyerIds = new uint256[](length);
        dealPrices = new uint256[](length);
        statuses = new uint8[](length);
        updateTimes = new uint256[](length);
        
        for (uint256 i = 0; i < length; i++) {
            Order storage order = orders[orderIds[i]];
            ids[i] = order.id;
            itemIds[i] = order.itemId;
            buyerIds[i] = order.buyerId;
            dealPrices[i] = order.dealPrice;
            statuses[i] = order.status;
            updateTimes[i] = order.updateTime;
        }
        
        return (ids, itemIds, buyerIds, dealPrices, statuses, updateTimes);
    }
}
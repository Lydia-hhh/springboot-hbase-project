package cn.hbase.springboothbaseproject.mapper;

import cn.hbase.springboothbaseproject.bean.Order;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;

public class DetailMapper implements RowMapper<Order.Detail> {
    private static final byte[] DETAIL_FAMILY="detail".getBytes();
    private static final byte[] ITEM_SKU_ID="item_sku_id".getBytes();
    private static final byte[] ITEM_FIRST_CATE_NAME="item_first_cate_name".getBytes();
    private static final byte[] ITEM_SECOND_CATE_NAME="item_second_cate_name".getBytes();
    private static final byte[] ITEM_THIRD_CATE_NAME="item_third_cate_name".getBytes();
    private static final byte[] BEFORE_PREFR_UNIT_PRICE="before_prefr_unit_price".getBytes();
    private static final byte[] USER_ACTUAL_PAY_AMOUNT="user_actual_pay_amount".getBytes();
    private static final byte[] CHECK_ACCOUNT_TM="check_account_tm".getBytes();
    private static final byte[] TOTAL_OFFER_AMOUNT="total_offer_amount".getBytes();
    private static final byte[] REV_ADDR_PROVINCE_ID="rev_addr_province_id".getBytes();
    private static final byte[] REV_ADDR_CITY_ID="rev_addr_city_id".getBytes();
    @Override
    public Order.Detail mapRow(Result result, int rowNum) throws Exception {
        Order.Detail detail=new Order.Detail(
                new String(result.getValue(DETAIL_FAMILY,ITEM_SKU_ID)),
                new String(result.getValue(DETAIL_FAMILY,ITEM_FIRST_CATE_NAME)),
                new String(result.getValue(DETAIL_FAMILY,ITEM_SECOND_CATE_NAME)),
                new String(result.getValue(DETAIL_FAMILY,ITEM_THIRD_CATE_NAME)),
                new String(result.getValue(DETAIL_FAMILY,BEFORE_PREFR_UNIT_PRICE)),
                new String(result.getValue(DETAIL_FAMILY,USER_ACTUAL_PAY_AMOUNT)),
                new String(result.getValue(DETAIL_FAMILY,CHECK_ACCOUNT_TM)),
                new String(result.getValue(DETAIL_FAMILY,TOTAL_OFFER_AMOUNT)),
                new String(result.getValue(DETAIL_FAMILY,REV_ADDR_PROVINCE_ID)),
                new String(result.getValue(DETAIL_FAMILY,REV_ADDR_CITY_ID))

        );
        return detail;
    }
}

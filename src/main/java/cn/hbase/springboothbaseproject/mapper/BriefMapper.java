package cn.hbase.springboothbaseproject.mapper;

import cn.hbase.springboothbaseproject.bean.Order;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;

public class BriefMapper implements RowMapper<Order.Brief> {

    private static final byte[] BRIEF_FAMILY="brief".getBytes();
    private static final byte[] ITEM_NAME="item_name".getBytes();
    private static final byte[] BRANDNAME="brandname".getBytes();
    private static final byte[] SALE_QTTY="sale_qtty".getBytes();
    private static final byte[] SELF_ORD_FLAG="self_ord_flag".getBytes();
    private static final byte[] AFTER_PREFR_UNIT_PRICE="after_prefr_unit_price".getBytes();
    private static final byte[] CANCEL_FLAG="cancel_flag".getBytes();
    private static final byte[] FINISH_FLAG="finish_flag".getBytes();
    private static final byte[] DELETE_FLAG="delete_flag".getBytes();

    @Override
    public Order.Brief mapRow(Result result, int rowNum) throws Exception {
        Order.Brief brief=new Order.Brief(
                new String(result.getValue(BRIEF_FAMILY,ITEM_NAME)),
                new String(result.getValue(BRIEF_FAMILY,BRANDNAME)),
                new String(result.getValue(BRIEF_FAMILY,SALE_QTTY)),
                new String(result.getValue(BRIEF_FAMILY,SELF_ORD_FLAG)),
                new String(result.getValue(BRIEF_FAMILY,AFTER_PREFR_UNIT_PRICE)),
                new String(result.getValue(BRIEF_FAMILY,CANCEL_FLAG)),
                new String(result.getValue(BRIEF_FAMILY,FINISH_FLAG)),
                new String(result.getValue(BRIEF_FAMILY,DELETE_FLAG))
        );
        return brief;


    }
}

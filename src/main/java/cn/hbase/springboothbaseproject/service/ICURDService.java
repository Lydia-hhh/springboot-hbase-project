package cn.hbase.springboothbaseproject.service;

import cn.hbase.springboothbaseproject.bean.Order;

import java.util.List;

public interface ICURDService {
    Order.Detail findBy(String tableName,String rowKey);

    List<Order.Brief> findBriefByStartAndEnd(String tableName,String startRowKey,String endRowKey);
}

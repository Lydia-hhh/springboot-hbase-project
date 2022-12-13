package cn.hbase.springboothbaseproject.service.impl;

import cn.hbase.springboothbaseproject.bean.Order;
import cn.hbase.springboothbaseproject.mapper.BriefMapper;
import cn.hbase.springboothbaseproject.mapper.DetailMapper;
import cn.hbase.springboothbaseproject.mapper.OrderMapper;
import cn.hbase.springboothbaseproject.service.ICURDService;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
public class CURDServiceImpl implements ICURDService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public Order.Detail findBy(String tableName,String rowKey) {
        return hbaseTemplate.get(tableName,rowKey,new DetailMapper());
    }

    @Override
    public List<Order.Brief> findBriefByStartAndEnd(String tableName, String startRowKey, String endRowKey) {
        Scan scan=new Scan();
        Filter pageFilter=new PageFilter(10);
        Filter familyFilter=new FamilyFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("brief"));
        List<Filter> filters=new ArrayList<>();
        filters.add(pageFilter);
        filters.add(familyFilter);
        Filter filterList=new FilterList(filters);

        scan.setStartRow(Bytes.toBytes(startRowKey));
        scan.setStopRow(Bytes.toBytes(endRowKey));
        scan.setCaching(2000);
        scan.setFilter(filterList);
        return hbaseTemplate.find(tableName,scan,new BriefMapper());
    }
}

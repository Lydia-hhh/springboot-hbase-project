package cn.hbase.springboothbaseproject.service.impl;

import cn.hbase.springboothbaseproject.bean.Order;
import cn.hbase.springboothbaseproject.mapper.BriefMapper;
import cn.hbase.springboothbaseproject.mapper.DetailMapper;
import cn.hbase.springboothbaseproject.mapper.OrderMapper;
import cn.hbase.springboothbaseproject.service.ICURDService;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CURDServiceImpl implements ICURDService {
    private int pageSize = 10;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public Order.Detail findBy(String tableName,String rowKey) {
        return hbaseTemplate.get(tableName,rowKey,new DetailMapper());
    }

    @Override
    public List<Order.Brief> findBriefByStartAndEnd(String tableName, String startRowKey, String endRowKey) {
        Scan scan=new Scan();
        Filter pageFilter=new PageFilter(pageSize);
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

    @Override
    public List<Order.Brief> findBriefByKeyList(String tableName, List<String> keyList) {
        Connection connection=hbaseTemplate.getConnection();
        List<Get> getList=new ArrayList<>();
        List<Order.Brief> res=new ArrayList<>();
        try {
            Table table = connection.getTable( TableName.valueOf(tableName));
            for (String rowkey : keyList){//把rowkey加到get里，再把get装到list中
                Get get = new Get(Bytes.toBytes(rowkey));
                getList.add(get);
            }
            Result[] results = table.get(getList);
            for (Result result : results){//对返回的结果集进行操作
                List<String> paramList=new ArrayList<>();
                for (Cell kv : result.rawCells()) {
//                    System.out.println(Bytes.toString(CellUtil.cloneFamily(kv))+" : "
//                            +Bytes.toString(CellUtil.cloneQualifier(kv))+" : "+
//                            Bytes.toString(CellUtil.cloneValue(kv)));
                    if(Bytes.toString(CellUtil.cloneFamily(kv)).equalsIgnoreCase("brief")){
                        String value = Bytes.toString(CellUtil.cloneValue(kv));
                        paramList.add(value);
                    }
                }
                paramList.add(Bytes.toString(result.getRow()));
                Order.Brief brief=new Order.Brief(paramList);
                res.add(brief);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

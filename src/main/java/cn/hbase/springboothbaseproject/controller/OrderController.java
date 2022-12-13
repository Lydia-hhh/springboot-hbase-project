package cn.hbase.springboothbaseproject.controller;


import cn.hbase.springboothbaseproject.bean.Order;
import cn.hbase.springboothbaseproject.service.ICURDService;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private ICURDService icurdService;

    @RequestMapping(value = "/{user_log_acct}",method = RequestMethod.GET)
    public List<Order.Brief> getOrdersBriefByUsrAcct(@PathVariable String user_log_acct){
        String rowKeyPrefix=MD5(user_log_acct)+"+"+user_log_acct;
        String startRowKey=rowKeyPrefix;
        String endRowKey=rowKeyPrefix+"+"+Long.MAX_VALUE+"";
        System.out.println("startKey: "+startRowKey);
        System.out.println("endKey: "+endRowKey);
        return icurdService.findBriefByStartAndEnd("order",startRowKey,endRowKey);
    }
    @RequestMapping(value = "/brief/{startRowKey}",method = RequestMethod.GET)
    public List<Order.Brief> getOrdersBriefByRowKey(@PathVariable String startRowKey){
        String[] rowKeyParts=startRowKey.split("\\+");
        System.out.println("rowKeyParts: "+rowKeyParts.toString());
        String endRowKey=rowKeyParts[0]+"+"+rowKeyParts[1]+"+"+Long.MAX_VALUE+"";
        System.out.println("startKey: "+startRowKey);
        System.out.println("endKey: "+endRowKey);
        return icurdService.findBriefByStartAndEnd("order",startRowKey,endRowKey);
    }
    @RequestMapping(value = "/detail/{rowKey}",method = RequestMethod.GET)
    public Order.Detail getOrdersDetailByRowKey(@PathVariable String rowKey){
        return icurdService.findBy("order",rowKey);
    }




    static String MD5(String input){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(input.getBytes());// 计算md5函数
        /**
         * digest()最后确定返回md5 hash值，返回值为8位字符串。
         * 因为md5 hash值是16位的hex值，实际上就是8位的字符
         * BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
         * 一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
         */
        String s = new BigInteger(1, md.digest()).toString(16);
        return s.substring(3,7);// 16是表示转换为16进制数
    }
}

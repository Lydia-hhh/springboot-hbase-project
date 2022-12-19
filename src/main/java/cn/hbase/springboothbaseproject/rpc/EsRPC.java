package cn.hbase.springboothbaseproject.rpc;

import cn.hbase.springboothbaseproject.param.FlagSearchParam;
import cn.hbase.springboothbaseproject.param.KeySearchParam;
import cn.hbase.springboothbaseproject.param.RowKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class EsRPC {
    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://192.168.43.129:8080/es";

    public List<String> searchByName(KeySearchParam param) {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(url + "/keyword", param, List.class);
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }

    public List<String> searchFlag(FlagSearchParam param, String resource) {
        ResponseEntity<List> responseEntity = restTemplate.postForEntity(url+resource, param, List.class);
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }

    public int updateOrder(RowKey rowKey, String resource) {
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url + resource, rowKey, Integer.class);
        return responseEntity.getBody();
    }
}

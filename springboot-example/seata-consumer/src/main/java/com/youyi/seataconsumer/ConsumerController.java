package com.youyi.seataconsumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@RestController
public class ConsumerController {

    final AccountMapper accountMapper;

    final RestTemplate restTemplate;

    @Value("${provider.port}")
    String port;

    public ConsumerController(AccountMapper accountMapper, RestTemplate restTemplate) {
        this.accountMapper = accountMapper;
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings({"NumericOverflow", "divzero", "unused"})
    @GetMapping("consume")
    @Transactional
    public String consume(Long id) {
        accountMapper.update(Wrappers.<Account>lambdaUpdate().eq(Account::getId, id).setIncrBy(Account::getMoney, 200));
        ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:" + port + "/provide?id={id}", String.class, Map.of("id", id));
        // 模拟异常
        long i = 10 / 0;
        return "OK" + Objects.requireNonNullElse(result.getBody(), 0);
    }

}

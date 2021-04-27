package com.nvr.video.runner;

import static com.nvr.video.constant.CommonConstant.SWAGGER_SUFFIX;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @author geekerstar
 * @date 2020/7/7 11:43
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    @Value("${server.port:8080}")
    private String port;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("++++++++++++++++++++++++++++++++++++++++++++");
        InetAddress address = InetAddress.getLocalHost();
        String url = String.format("http://%s:%s%s", address.getHostAddress(), port, SWAGGER_SUFFIX);
        log.info("【办案区视频处理程序】启动成功!");
        log.info("【Swagger】{}", url);
        log.info("++++++++++++++++++++++++++++++++++++++++++++");
    }

}

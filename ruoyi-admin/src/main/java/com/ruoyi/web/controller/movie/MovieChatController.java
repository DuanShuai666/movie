package com.ruoyi.web.controller.movie;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/movie/chat")
public class MovieChatController extends BaseController {

    @Value("${movie.chat.api-url:http://localhost:5000}")
    private String chatApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public AjaxResult askQuestion(@RequestBody Map<String, String> request) {
        String question = request.get("question");

        if (question == null || question.trim().isEmpty()) {
            return AjaxResult.error("问题不能为空");
        }

        try {
            String url = chatApiUrl + "/api/ask";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("question", question);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return AjaxResult.success(response.getBody());
            } else {
                return AjaxResult.error("调用问答服务失败");
            }

        } catch (Exception e) {
            logger.error("问答服务调用异常", e);
            return AjaxResult.error("问答服务暂时不可用：" + e.getMessage());
        }
    }

    @GetMapping("/health")
    public AjaxResult healthCheck() {
        try {
            String url = chatApiUrl + "/api/health";

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return AjaxResult.success(response.getBody());
            } else {
                return AjaxResult.error("服务健康检查失败");
            }

        } catch (Exception e) {
            logger.error("健康检查异常", e);
            return AjaxResult.error("问答服务不可用");
        }
    }
}

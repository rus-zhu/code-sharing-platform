package platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import platform.dto.CodeRequest;
import platform.dto.CodeResponse;
import platform.dto.EmptyJsonResponse;
import platform.service.CodeService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ApiController {
    private final CodeService codeService;

    public ApiController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/code/{id}")
    public ModelAndView getHtml(HttpServletResponse response, @PathVariable(name = "id") String id) {
        return codeService.getHtmlById(response, id);
    }

    @GetMapping("/code/new")
    public ModelAndView getNewHtml(HttpServletResponse response) {
        return codeService.getNewHtml(response);
    }

    @GetMapping("/api/code/{id}")
    public CodeResponse getJson(HttpServletResponse response, @PathVariable String id) {
        response.addHeader("Content-Type", "application/json");
        return codeService.findById(id);
    }

    @PostMapping("/api/code/new")
    public ResponseEntity<?> setCode(@RequestBody CodeRequest inputSnippet) {
        EmptyJsonResponse id = codeService.addNewCodeSnippet(inputSnippet);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/api/code/latest")
    public List<CodeResponse> getTenLatest(){
        return codeService.getTenLastAsJson();
    }

    @GetMapping("/code/latest")
    public ModelAndView getTenLatestHtml(HttpServletResponse response) {
        return codeService.getTenLastAsHtml(response);
    }
}

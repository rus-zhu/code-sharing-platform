package platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import platform.dto.CodeRequest;
import platform.dto.CodeResponse;
import platform.dto.EmptyJsonResponse;
import platform.entity.Code;
import platform.exceptions.CodeNotFoundException;
import platform.repository.CodeRepo;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CodeService {
    CodeRepo codeRepo;

    public CodeService(CodeRepo codeRepo) {
        this.codeRepo = codeRepo;
    }

    public EmptyJsonResponse addNewCodeSnippet(CodeRequest inputSnippet) {
        Code code = new Code();
        code.setCode(inputSnippet.getCode());
        code.setDate(LocalDateTime.now());
        code.setViews(inputSnippet.getViews());
        code.setTime(inputSnippet.getTime());
        code.setTimeLimit(code.getTime() > 0);
        code.setViewsLimit(code.getViews() > 0);
        codeRepo.save(code);
        return new EmptyJsonResponse(code.getId().toString());
    }

    public List<CodeResponse> getTenLastAsJson() {
        List<Code> list = codeRepo.findAllByTimeAndViewsOrderByDate();
        List<CodeResponse> responseList = new ArrayList<>();
        list.forEach(
                code -> responseList.add(new CodeResponse(
                        code.getCode(),
                        code.getDate(),
                        code.getTime(),
                        code.getViews())
                ));
        return responseList;
    }

    public ModelAndView getTenLastAsHtml(HttpServletResponse response) {
        response.addHeader("Content-Type", "text/html");
        ModelAndView model = new ModelAndView();
        List<Code> list = codeRepo.findAllByTimeAndViewsOrderByDate();
        model.addObject("codeSnippets", list);
        model.addObject("LOCAL_DATE_TIME", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        model.setViewName("latest");
        return model;
    }

    public CodeResponse findById(String id) {
        Code code =  codeRepo.findById(UUID.fromString(id)).orElseThrow();
        if (code.isTimeLimit()) {
            updateTimeById(UUID.fromString(id));
        }
        if (code.isViewsLimit()) {
            updateViewsById(UUID.fromString(id));
        }
        Code updatedCode = getCodeFromRepository(UUID.fromString(id));
        return new CodeResponse(
                updatedCode.getCode(),
                updatedCode.getDate(),
                updatedCode.getTime(),
                updatedCode.getViews());
    }

    public ModelAndView getNewHtml(HttpServletResponse response) {
        response.addHeader("Content-Type", "text/html");
        List<Code> list = codeRepo.findAll();
        String codeSnippet = list.isEmpty() ? "" : list.get(list.size() - 1).getCode();
        String date = list.isEmpty() ? "" : list.get(list.size() - 1).getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String timeRestriction = list.isEmpty() ? "" : list.get(list.size() - 1).getTime().toString();
        String viewsRestriction = list.isEmpty() ? "" : list.get(list.size() - 1).getViews().toString();
        ModelAndView model = new ModelAndView();
        model.addObject("time_restriction", timeRestriction);
        model.addObject("views_restriction", viewsRestriction);
        model.addObject("codeSnippet", codeSnippet);
        model.addObject("date", date);
        model.setViewName("newCode");
        return model;
    }

    public ModelAndView getHtmlById(HttpServletResponse response, String id) {
        response.addHeader("Content-Type", "text/html");
        ModelAndView model = new ModelAndView();
        UUID uuid = UUID.fromString(id);
        Code code = getCodeFromRepository(uuid);
        if (code.isTimeLimit()) {
            updateTimeById(uuid);
        }
        if (code.isViewsLimit()) {
            updateViewsById(uuid);
        }
        Code updatedCode = getCodeFromRepository(uuid);
        if (!code.isTimeLimit() && !code.isViewsLimit()) {
            model.addObject("date", updatedCode.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            model.addObject("code", updatedCode.getCode());
            model.setViewName("index");
        } else {
            model.addObject("code", updatedCode.getCode());
            model.addObject("date", updatedCode.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            model.addObject("time", updatedCode.getTime());
            model.addObject("views", updatedCode.getViews());
            model.setViewName("codeUuid");
        }
        return model;
    }
    public Code getCodeFromRepository(UUID uuid) {
        log.debug("Request to getCodeFromRepository: {}", uuid);
        if (codeRepo.findById(uuid).isPresent()) {
            log.debug("Snippet from repository: {}", codeRepo.findById(uuid).get().toString());
            return codeRepo.findById(uuid).get();
        } else {
            throw new CodeNotFoundException();
        }
    }

    LocalTime getTimeOfCreation(Code code) {
        log.debug("Request to getTimeOfCreation: {}", code);
        return code.getDate().toLocalTime();
    }
    Long setTimeToSecretCode(Code code) {
        log.debug("Request to setTimeToSecretCode: {}", code);
        long result = 0;
        long timeToWatch = code.getTime();
        LocalTime timeOfCreation = getTimeOfCreation(code);
        LocalTime localTime = LocalTime.now();
        long difference = localTime.toSecondOfDay() - timeOfCreation.toSecondOfDay();
        if (difference > 0) {
            result = timeToWatch - difference;
        }
        return result;
    }

    public void updateTimeById(UUID uuid) {
        log.debug("Request to updateTimeById: {}", uuid);
        Code codeToUpdate = getCodeFromRepository(uuid);
        Long time = codeToUpdate.getTime();
        codeToUpdate.setTime(setTimeToSecretCode(codeToUpdate));
        log.debug("Snippet was updated, now time is {}", codeToUpdate.getTime());
        if (codeToUpdate.getTime() > 0) {
            codeRepo.save(codeToUpdate);
        } else {
            codeRepo.delete(codeToUpdate);
        }
    }

    public void updateViewsById(UUID uuid) {
        log.debug("Request to updateViewsById: {}", uuid);
        Code codeToUpdate = getCodeFromRepository(uuid);
        long views = codeToUpdate.getViews();
        views--;
        codeToUpdate.setViews(views);
        if (codeToUpdate.getViews() >= 0) {
            codeRepo.save(codeToUpdate);
        } else {
            codeRepo.delete(codeToUpdate);
        }
        log.debug("Snippet was updated, now views is {}", codeToUpdate.getViews());
    }
}

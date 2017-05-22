package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class HomeController {

    @RequestMapping("/")

    public ModelAndView helloWorld()
    {
        FBConnection fbConnection = new FBConnection();
        return new
                ModelAndView("welcome","message",fbConnection.getFBAuthUrl());

    }

    @RequestMapping(value = {"welcome", "welcome2"})

    public ModelAndView helloWorld2(@RequestParam("code") String code)
    {

        if (code == null || code.equals("")) {
            throw new RuntimeException("Error: Didn't get code parameter in callback.");
        }
        FBConnection fbConnection = new FBConnection();
        String accessToken = fbConnection.getAccessToken(code);

        // graph class processes the json
        FBGraph fbGraph = new FBGraph(accessToken);
        String graph = fbGraph.getFBGraph();
        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
        String out = "";
        out = out.concat("<h1>FB Login using Java</h1>");
        out = out.concat("<h1>Application Main Menu</h1>");
        out = out.concat("<div>Welcome " + fbProfileData.get("name"));
        out = out.concat("<div>Your Email: " + fbProfileData.get("email"));
        out = out.concat("<div>Your gender: " + fbProfileData.get("gender"));
        return new
                ModelAndView("welcome2","message",out);

    }

}

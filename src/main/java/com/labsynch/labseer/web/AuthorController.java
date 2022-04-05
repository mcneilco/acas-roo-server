package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Author;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Author.class)
@Controller
@RequestMapping("/authors")
@RooWebScaffold(path = "authors", formBackingObject = Author.class)
@RooWebFinder
public class AuthorController {
}

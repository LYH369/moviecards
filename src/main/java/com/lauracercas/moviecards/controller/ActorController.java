package com.lauracercas.moviecards.controller;

import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.service.actor.ActorService;
import com.lauracercas.moviecards.util.Messages;

import java.util.List;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@Controller
public class ActorController {

    @Autowired
    private RestTemplate restTemplate; // 注入 RestTemplate

    private static final String MOVIECARDS_SERVICE_URL = "https://moviecards-service-apellido.azurewebsites.net/"; // 更新为实际的服务 URL

    public ActorController(ActorService actorServiceMock) {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("actors")
    public String getActorsList(Model model) {
        // 调用 moviecards-service API 获取演员列表
        ResponseEntity<List> response = restTemplate.exchange(MOVIECARDS_SERVICE_URL + "actors", HttpMethod.GET, null, List.class);
        List<Actor> actors = response.getBody();
        model.addAttribute("actors", actors);
        return "actors/list";
    }

    @GetMapping("actors/new")
    public String newActor(Model model) {
        model.addAttribute("actor", new Actor());
        model.addAttribute("title", Messages.NEW_ACTOR_TITLE);
        return "actors/form";
    }

    @PostMapping("saveActor")
    public String saveActor(@ModelAttribute Actor actor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "actors/form";
        }
        // 调用 moviecards-service API 保存新演员
        Actor savedActor = restTemplate.postForObject(MOVIECARDS_SERVICE_URL + "actors", actor, Actor.class);
        model.addAttribute("message", Messages.SAVED_ACTOR_SUCCESS);
        model.addAttribute("actor", savedActor);
        model.addAttribute("title", Messages.EDIT_ACTOR_TITLE);
        return "actors/form";
    }

    @GetMapping("editActor/{actorId}")
    public String editActor(@PathVariable Integer actorId, Model model) {
        // 调用 moviecards-service API 获取演员详细信息
        ResponseEntity<Actor> response = restTemplate.exchange(MOVIECARDS_SERVICE_URL + "actors/{actorId}", HttpMethod.GET, null, Actor.class, actorId);
        Actor actor = response.getBody();
        model.addAttribute("actor", actor);
        model.addAttribute("title", Messages.EDIT_ACTOR_TITLE);
        return "actors/form";
    }
}

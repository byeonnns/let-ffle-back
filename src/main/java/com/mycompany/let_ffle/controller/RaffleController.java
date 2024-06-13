package com.mycompany.let_ffle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.let_ffle.service.RaffleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/raffle")

public class RaffleController {

	@Autowired
	private RaffleService raffleService;
}

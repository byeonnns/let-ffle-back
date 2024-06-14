package com.mycompany.let_ffle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.request.RaffleRequest;
import com.mycompany.let_ffle.service.RaffleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/raffle")

public class RaffleController {

	@Autowired
	private RaffleService raffleService;
	
	@PostMapping("/createRaffle")
	public Raffle createRaffle(RaffleRequest raffleRequest) {
		
		return null;
	}
	
	@GetMapping("/getRaffleList")
	public List<RaffleRequest> getRaffleList(){
		return null;
	}
	
	@GetMapping("/readRaffle")
	public RaffleRequest readRaffle(int rno) {
		return null;
	}
	
	@PutMapping("/updateRaffle")
	public RaffleRequest updateRaffle(RaffleRequest raffleRequest) {
		return null;
	}
	
	@PutMapping("/deleteRaffle")
	public Raffle deleteRaffle(int rno) {
		return null;
	}
}

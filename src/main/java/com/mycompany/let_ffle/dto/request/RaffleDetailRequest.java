package com.mycompany.let_ffle.dto.request;

import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.Winner;

import lombok.Data;

@Data
public class RaffleDetailRequest {
	public Raffle raffle;
	public RaffleDetail raffleDetail;
	public Winner winner;
	
}

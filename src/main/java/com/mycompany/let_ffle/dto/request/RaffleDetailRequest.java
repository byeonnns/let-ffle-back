package com.mycompany.let_ffle.dto.request;

import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.Winner;

import lombok.Data;

@Data
public class RaffleDetailRequest {
	public Raffle raffle;
	public RaffleDetail raffleDetail;
	public String probability; // 당첨 확률 저장용
	public String nowStatus; // 현재 상태 출력용
	public Winner winner;
}

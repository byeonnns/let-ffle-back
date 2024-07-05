package com.mycompany.let_ffle.dto.request;

import com.mycompany.let_ffle.dto.QuizMission;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleImage;
import com.mycompany.let_ffle.dto.TimeMission;

import lombok.Data;

@Data
public class RaffleRequest {
	public Raffle raffle;
	public RaffleImage raffleImage;
	public QuizMission quizMission;
	public TimeMission timeMission;
	public int raffleProcess;
}

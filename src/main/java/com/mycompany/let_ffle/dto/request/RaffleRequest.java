package com.mycompany.let_ffle.dto.request;

import com.mycompany.let_ffle.dto.QuizMission;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleImage;
import com.mycompany.let_ffle.dto.TimeMission;

import lombok.Data;

@Data
public class RaffleRequest {
	private Raffle raffle;
	private RaffleImage raffleImage;
	private QuizMission quizMission;
	private TimeMission timeMission;
}

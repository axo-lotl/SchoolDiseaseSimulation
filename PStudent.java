package mainpackage;

import java.util.Random;

//extension of Student for modeling pertussis

public class PStudent extends Student {

	// the last 4 statuses represent the different stages of pertussis
	private enum Status {
		HEALTHY, IMMUNE, LATENT, CATARRHAL, PAROXYSMAL, CONVALESCENT,
	}

	private boolean vaccinated;
	private Status status;
	private int statusTimer; // counts down time for disease stages
	public Random rng;

	public PStudent(boolean vaccinated, Random rng) {
		this.vaccinated = vaccinated;
		this.rng = rng;
		status = Status.HEALTHY;
		statusTimer = 0;
	}

	@Override
	public void advanceDay() {
		if (status == Status.HEALTHY || status == Status.IMMUNE) {
			// do nothing
		} else {
			statusTimer--;
			if (statusTimer == 0) {
				if (status == Status.LATENT) {
					status = Status.CATARRHAL;
					statusTimer = 7 + rng.nextInt(8); // 7-14 days, length of
														// catarrhal stage
				} else if (status == Status.CATARRHAL) {
					status = Status.PAROXYSMAL;
					statusTimer = 7 + rng.nextInt(36); // 7-42 days, length of
														// paroxysmal stage
				} else if (status == Status.PAROXYSMAL) {
					status = Status.CONVALESCENT;
					statusTimer = 14 + rng.nextInt(8); // 14-21 days, length of
														// convalescent stage
				} else if (status == Status.CONVALESCENT) {
					status = Status.IMMUNE;
				}
			}
		}

	}

	@Override
	public boolean canAttend() {
		return !(status == Status.PAROXYSMAL);
		// only if the disease is in the paroxysmal stage will it be visible
		// enough for the student to be disallowed
	}

	@Override
	public boolean isVaccinated() {
		return vaccinated;
	}

	@Override
	public void attemptToInfect() {
		if (status == Status.HEALTHY) {
			boolean infected = false;
			if (vaccinated) {
				if (Math.random() < 0.15) // chance of vaccine failure
					infected = true;
			} else {
				infected = true;
			}

			if (infected) {
				status = Status.LATENT;
				statusTimer = 7 + rng.nextInt(4); // 7-10 days, length of latent
													// stage
			}
		}
	}

	@Override
	public boolean canInfect() {
		if (!canAttend()) {
			return false;
		} else {
			if (status == Status.CATARRHAL || status == Status.CONVALESCENT
					|| status == Status.PAROXYSMAL) {
				return true;
			} else
				return false;
		}
	}

	@Override
	public boolean isSick() {
		if (status == Status.HEALTHY || status == Status.IMMUNE)
			return false;
		else
			return true;
	}

	@Override
	public boolean wasInfected() {
		return !(status == Status.HEALTHY);
	}

}

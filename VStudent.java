package mainpackage;

import java.util.Random;

// class for modeling the spread of varicella
public class VStudent extends Student {

	private final int ACTION_DAYS = 14;
	// when the student is sent home, this is how many days he stays

	private enum Status {
		HEALTHY, IMMUNE, LATENT, INVISIBLE, VISIBLE
	}

	private boolean vaccinated;
	private Status status;
	private int statusTimer; // counts down timers for disease stages
	private int prohibitionTimer; // if nonzero, indicates that the student is
									// being prohibited from going to school
	public Random rng;

	public VStudent(boolean vaccinated, Random rng) {
		this.vaccinated = vaccinated;
		this.rng = rng;
		status = Status.HEALTHY;
		statusTimer = 0;
		prohibitionTimer = 0;
	}

	@Override
	public boolean isVaccinated() {
		return vaccinated;
	}

	@Override
	public void advanceDay() {
		if (prohibitionTimer > 0) {
			prohibitionTimer--;
		}

		if (status == Status.HEALTHY || status == Status.IMMUNE) {
			// do nothing
		} else {
			statusTimer--;
			if (statusTimer == 0) {
				if (status == Status.LATENT) {
					status = Status.INVISIBLE;
					statusTimer = 1 + rng.nextInt(2); // 1-2 days, length of
														// "invisible" stage
				} else if (status == Status.INVISIBLE) {
					status = Status.VISIBLE;
					statusTimer = 5 + rng.nextInt(6); // 5-10 days, length of
														// "visible" stage
				} else if (status == Status.VISIBLE) {
					status = status.IMMUNE;
				}
			}
		}
	}

	@Override
	public boolean canAttend() {
		return !(status == Status.VISIBLE) & (prohibitionTimer == 0);
	}

	@Override
	public void attemptToInfect() {
		if (status == Status.HEALTHY) {
			boolean infected = false;
			if (vaccinated) {
				if (Math.random() < 0.07) // chance of vaccine failure
					infected = true;
			} else {
				infected = true;
			}

			if (infected) {
				status = Status.LATENT;
				statusTimer = 13 + rng.nextInt(2); // 13-14 days, length of
													// latent stage
			}
		}
	}

	@Override
	public boolean canInfect() {
		if (!canAttend()) {
			return false;
		} else {
			if (status == Status.INVISIBLE || status == Status.VISIBLE)
				return true;
			else
				return false;
		}
	}

	@Override
	public boolean isSick() {
		if (status == Status.HEALTHY || status == Status.IMMUNE) {
			return false;
		} else
			return true;
	}

	@Override
	public boolean wasInfected() {
		return !(status == Status.HEALTHY);
	};

	public void prohibit() {
		if (vaccinated) {
			// do nothing
		} else {
			prohibitionTimer = ACTION_DAYS + 1;
		}
	}

}

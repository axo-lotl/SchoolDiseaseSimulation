package mainpackage;

import java.util.Random;

public class HStudent extends Student {

	private enum Status {
		CHRONIC, HEALTHY, IMMUNE, LATENT, VISIBLE, INVISIBLE
	}

	private boolean vaccinated;
	private Status status;
	private int statusTimer;
	private Random rng;
	private boolean wasInfected;

	
	public HStudent(boolean vaccinated, boolean chronic, Random rng) {
		this.vaccinated = vaccinated;
		this.rng = rng;
		if (chronic)
			status = Status.CHRONIC;
		else
			status = Status.HEALTHY;
		wasInfected = false;
		statusTimer = 0;
	}

	@Override
	public boolean isVaccinated() {
		return vaccinated;
	}

	public boolean isChronic() {
		return status == Status.CHRONIC;
	}

	@Override
	public void advanceDay() {
		if (status == Status.CHRONIC || status == Status.HEALTHY
				|| status == Status.IMMUNE) {
			// do nothing
		} else {
			statusTimer--;
			if (statusTimer == 0) {
				if (status == Status.LATENT) {
					if (Math.random() < 0.31) {
						status = Status.VISIBLE;
						statusTimer = 30 + rng.nextInt(151); // 30-180 days
					} else {
						status = Status.INVISIBLE;
						statusTimer = 30 + rng.nextInt(151);
					}
				} else if (status == Status.VISIBLE
						|| status == Status.INVISIBLE) {
					if (Math.random() < 0.08)
						status = Status.CHRONIC;
					else
						status = Status.IMMUNE;
				}
			}
		}
	}

	@Override
	public boolean canAttend() {
		return !(status == Status.VISIBLE);
	}

	@Override
	public void attemptToInfect() {
		if (status == Status.HEALTHY) {
			boolean infected = false;
			if (vaccinated) {
				if (Math.random() < 0.05)
					infected = true;
			} else {
				infected = true;
			}

			if (infected) {
				wasInfected = true;
				status = Status.LATENT;
				if (Math.random() < 0.5)
					statusTimer = 60 + rng.nextInt(31); // 60-90 days
				else
					statusTimer = 90 + rng.nextInt(61); // 90-150 days
			}
		}

	}

	@Override
	public boolean canInfect() {
		return (status == Status.CHRONIC || status == Status.VISIBLE
				|| status == Status.INVISIBLE);
	}

	@Override
	public boolean isSick() {
		return !(status == Status.HEALTHY || status == Status.IMMUNE);
	}

	@Override
	public boolean wasInfected() {
		return wasInfected;
	}

}

package mainpackage;

import java.util.Random;

public class PCalibrationTrialNormal {

	private final double HOMEROOM_TIME_RATIO = 0.774; // percentage of time
														// spent in homeroom
	private final int CLASS_SIZE = 25;
	private final int CLASSES = 20;
	private final int THRESHOLD = 10; // number of cases before school closes
	private final int CLOSING_DAYS = 5;

	private PStudent[] students;
	private Random rng;
	private int closeUntil;

	private double S;
	private double I;
	private int unvStudents;

	public PCalibrationTrialNormal(double S, double I, int unvStudents) {
		rng = new Random();

		this.S = S;
		this.I = I;
		this.unvStudents = unvStudents;

		students = new PStudent[CLASS_SIZE * CLASSES];
		for (int i = 0; i < this.unvStudents; i++) {
			students[i] = new PStudent(false, rng);
		}
		for (int i = this.unvStudents; i < students.length; i++) {
			students[i] = new PStudent(true, rng);
		}
		shuffleStudents();
		closeUntil = -1;
	}

	// randomizes the student's positions in the array
	// I am told this is called the Durstenfeld shuffle
	private void shuffleStudents() {
		for (int i = students.length - 1; i > 0; i--) {
			int index = rng.nextInt(i + 1);
			PStudent a = students[index];
			students[index] = students[i];
			students[i] = a;
		}
	}

	// probability that an unvaccinated student gets infected?
	public double getUnvaccinatedRisk() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (PStudent s : students) {
			if (!s.isVaccinated() && s.wasInfected())
				counter++;
		}

		return (double) counter / (double) unvStudents;
	}

	// probability that a vaccinated student gets infected?
	public double getVaccinatedRisk() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (PStudent s : students) {
			if (s.isVaccinated() && s.isSick())
				counter++;
		}

		return (double) counter / (double) (CLASSES * CLASS_SIZE - unvStudents);
	}

	// returns the number of students that were infected in the whole year
	public int runYear() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (PStudent s : students) {
			if (s.wasInfected()) {
				counter++;
			}
		}
		return counter;
	}

	// passes 1 day in the simulation
	public void runDay(int day) {
		attemptExternalInfection();
		if (isSchoolOpen(day)) {
			spreadDiseaseInSchool();
			considerMassAction(day);
		}
		advanceAllStudents();
	}

	// attempts to infect each student independently from external sources
	private void attemptExternalInfection() {
		for (PStudent s : students) {
			if (Math.random() < S)
				s.attemptToInfect();
		}
	}

	// is school even going to open? If not, spreadDiseaseInSchool() and
	// considerMassAction() don't run
	private boolean isSchoolOpen(int i) {
		if (closeUntil == -1) {
			if (i % 7 == 4 || i % 7 == 5)
				return false; // weekends
			else if (i < 86)
				return false; // school hasn't started yet
			else if (i >= 165 && i <= 171)
				return false; // Thanksgiving break
			else if (i >= 186 && i <= 199)
				return false; // Christmas break
			else if (i >= 298 && i <= 304)
				return false; // Spring break
			else
				return true;
		} else {
			if (i < closeUntil) {
				return false;
			} else {
				closeUntil = -1;
				return true;
			}
		}
	}

	//student-to-student spread of disease
	private void spreadDiseaseInSchool() {
		for (int i = 0; i < students.length; i++) {
			PStudent thisStudent = students[i];
			if (thisStudent.canInfect()) {
				double counter = I;
				while (counter > 1) {
					attemptToInfectSomeoneElse(i);
					counter -= 1;
				}

				if (Math.random() < counter) {
					attemptToInfectSomeoneElse(i);
				}
			}
		}
	}

	// helper method; i is an array index
	private void attemptToInfectSomeoneElse(int i) {
		int classNumber = i / 25;
		if (Math.random() < HOMEROOM_TIME_RATIO) {
			//infect another student in the same homeroom class
			int studentNumber = i % 25;
			int other = rng.nextInt(24);
			if (other >= studentNumber)
				other++;
			PStudent otherStudent = students[25 * classNumber + other];
			if (otherStudent.canAttend())
				otherStudent.attemptToInfect();
		} else {
			//infect another student outside the homeroom class
			int otherClass = rng.nextInt(CLASSES - 1);
			if (otherClass >= classNumber)
				otherClass++;
			PStudent otherStudent = students[25 * otherClass + rng.nextInt(25)];
			if (otherStudent.canAttend())
				otherStudent.attemptToInfect();
		}
	}

	//school administration seeing if the threshold number of students has been reached yet
	private void considerMassAction(int i) {
		int counter = 0;
		for (PStudent s : students) {
			if (!s.canAttend())
				counter++;
		}
		if (counter >= THRESHOLD)
			closeUntil = i + CLOSING_DAYS + 1;
	}
	
	//advances the timer for all Students
	private void advanceAllStudents() {
		for (PStudent s : students)
			s.advanceDay();
	}
}

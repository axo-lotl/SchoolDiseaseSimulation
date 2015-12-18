package mainpackage;

import java.util.Random;

public class HCalibrationTrial {

	// runs like PCalibrationTrial, except that there is no external infection
	// and the school never closes

	private final double HOMEROOM_TIME_RATIO = 0.774;
	private final int CLASS_SIZE = 25;
	private final int CLASSES = 20;

	private final double VERTICAL_PROPORTION = 0.5;

	private HStudent[] students;
	private Random rng;

	private double I;
	private int unvStudents;

	public HCalibrationTrial(double I, int unvStudents) {
		rng = new Random();

		this.I = I;
		this.unvStudents = unvStudents;

		students = new HStudent[CLASS_SIZE * CLASSES];
		students[0] = new HStudent(true, true, rng); // vertically infected
														// student
		for (int i = 1; i < 1 + this.unvStudents; i++) {
			students[i] = new HStudent(false, false, rng); // unvaccinated
															// students;
		}
		for (int i = 1 + this.unvStudents; i < students.length; i++) {
			students[i] = new HStudent(true, false, rng); // vaccinated
															// students;
		}
		shuffleStudents();
	}

	public double getUnvaccinatedRisk() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (HStudent s : students) {
			if (!s.isVaccinated() && s.wasInfected())
				counter++;
		}

		return (double) counter / (double) unvStudents;
	}

	public double getVaccinatedRisk() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (HStudent s : students) {
			if (s.isVaccinated() && s.isSick() && !s.isChronic())
				counter++;
		}

		return (double) counter / (double) (CLASSES * CLASS_SIZE - unvStudents);
	}

	public int runYear() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (HStudent s : students) {
			if (s.wasInfected()) {
				counter++;
			}
		}
		return counter;
	}

	// randomizes the student's positions in the array
	// I am told this is called the Durstenfeld shuffle
	private void shuffleStudents() {
		for (int i = students.length - 1; i > 0; i--) {
			int index = rng.nextInt(i + 1);
			HStudent a = students[index];
			students[index] = students[i];
			students[i] = a;
		}
	}

	public void runDay(int day) {
		if (isSchoolOpen(day)) {
			spreadDiseaseInSchool();
		}
		advanceAllStudents();
	}

	private boolean isSchoolOpen(int i) {
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
	}

	private void spreadDiseaseInSchool() {
		for (int i = 0; i < students.length; i++) {
			HStudent thisStudent = students[i];
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

	private void attemptToInfectSomeoneElse(int i) {
		int classNumber = i / 25;
		if (Math.random() < HOMEROOM_TIME_RATIO) {
			int studentNumber = i % 25;
			int other = rng.nextInt(24);
			if (other >= studentNumber)
				other++;
			HStudent otherStudent = students[25 * classNumber + other];
			if (otherStudent.canAttend())
				otherStudent.attemptToInfect();
		} else {
			int otherClass = rng.nextInt(CLASSES - 1);
			if (otherClass >= classNumber)
				otherClass++;
			HStudent otherStudent = students[25 * otherClass + rng.nextInt(25)];
			if (otherStudent.canAttend())
				otherStudent.attemptToInfect();
		}
	}

	private void advanceAllStudents() {
		for (HStudent s : students)
			s.advanceDay();
	}

}

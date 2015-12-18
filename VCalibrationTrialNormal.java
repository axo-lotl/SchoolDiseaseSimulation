package mainpackage;

import java.util.Random;

public class VCalibrationTrialNormal {
	
	//runs functionally identically to PCalibrationTrialNormal

	private final double HOMEROOM_TIME_RATIO = 0.774;
	private final int CLASS_SIZE = 25;
	private final int CLASSES = 20;
	private final int THRESHOLD = 5; // simultaneous infections per class

	private VStudent[] students;
	private Random rng;

	private double S;
	private double I;
	private int unvStudents;

	public VCalibrationTrialNormal(double S, double I, int unvStudents) {
		rng = new Random();

		this.S = S;
		this.I = I;
		this.unvStudents = unvStudents;

		students = new VStudent[CLASS_SIZE * CLASSES];
		for (int i = 0; i < this.unvStudents; i++) {
			students[i] = new VStudent(false, rng);
		}
		for (int i = this.unvStudents; i < students.length; i++) {
			students[i] = new VStudent(true, rng);
		}
		shuffleStudents();
	}
	
	public double getUnvaccinatedRisk() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}
		
		int counter = 0;
		for (VStudent s : students) {
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
		for (VStudent s : students) {
			if (s.isVaccinated() && s.isSick())
				counter++;
		}
		
		return (double) counter / (double) (CLASSES * CLASS_SIZE - unvStudents);
	}
	
	public int runYear() {
		for (int i = 1; i <= 365; i++) {
			runDay(i);
		}

		int counter = 0;
		for (VStudent s : students) {
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
			VStudent a = students[index];
			students[index] = students[i];
			students[i] = a;
		}
	}
	
	public void runDay(int day) {
		attemptExternalInfection();
		if (isSchoolOpen(day)) {
			spreadDiseaseInSchool();
			considerMassAction();
		}
		advanceAllStudents();
	}
	
	private void attemptExternalInfection() {
		for (VStudent s : students) {
			if (Math.random() < S)
				s.attemptToInfect();
		}
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
			VStudent thisStudent = students[i];
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
	
	// i is an array index
		private void attemptToInfectSomeoneElse(int i) {
			int classNumber = i / 25;
			if (Math.random() < HOMEROOM_TIME_RATIO) {
				int studentNumber = i % 25;
				int other = rng.nextInt(24);
				if (other >= studentNumber)
					other++;
				VStudent otherStudent = students[25 * classNumber + other];
				if (otherStudent.canAttend())
					otherStudent.attemptToInfect();
			} else {
				int otherClass = rng.nextInt(CLASSES - 1);
				if (otherClass >= classNumber)
					otherClass++;
				VStudent otherStudent = students[25 * otherClass + rng.nextInt(25)];
				if (otherStudent.canAttend())
					otherStudent.attemptToInfect();
			}
		}
		
		private void considerMassAction() {
			boolean willAct = false;
			for (int i = 0; i < CLASSES; i++) {
				int sickStudents = 0;
				for (int j = i * CLASS_SIZE; j < (i + 1) * CLASS_SIZE; j++) {
					if (!students[j].canAttend())
						sickStudents++;
				}
				
				if (sickStudents >= THRESHOLD) {
					willAct = true;
					break;
				}
			}
			
			if (willAct) {
				for (VStudent s : students) {
					s.prohibit();
				}
			}
		}
		

		private void advanceAllStudents() {
			for (VStudent s : students)
				s.advanceDay();
		}
	
}

package core.dutytable.worker;

public class Worker {
	public int id;
	public String name;
	public Day[] days;;

	public Worker(int id) {
		this(id, "no_name");
	}

	public Worker(int id, String name) {
		this.id = id;
		this.name = name;
		days = new Day[5];
		for (int dayOfWeek = 1; dayOfWeek <= days.length; dayOfWeek++) {
			days[dayOfWeek - 1] = new Day(dayOfWeek);
		}
	}
}

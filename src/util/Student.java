package util;

import java.util.Comparator;

/**
 * Class for testing with objects in sorting algorithms.
 * @author joaquin
 */
public class Student implements Comparable<Student> {

	public static final Comparator<Student> BY_NAME = (o1, o2) -> o1.name.compareTo(o2.name); // using lambda expression
	public static final Comparator<Student> BY_SECTION = new Comparator<Student>(){
		@Override
		public int compare(Student o1, Student o2) {
			return o1.section - o2.section;
		}
	};

	final int id;
	final String name;
	final int section;

	public Student(int id, String name, int section) {
		this.id = id;
		this.name = name;
		this.section = section;
	}

	@Override
	public int compareTo(Student that) {
		return this.id - that.id; // both should be positive
	}

	@Override
	public String toString() {
		return this.id + "/" + this.name + "/" + this.section;
	}
}
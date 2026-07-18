import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GymClass {
    private String className;
    private String instructor;
    private int capacity;
    private ArrayList<Member> enrolledMembers;
    private Queue<Member> waitlist;

    public GymClass(String className, String instructor, int capacity) {
        this.className = className;
        this.instructor = instructor;
        this.capacity = capacity;
        this.enrolledMembers = new ArrayList<>();
        this.waitlist = new LinkedList<>();
    }

    public String getClassName() { return className; }
    public String getInstructor() { return instructor; }
    public int getCapacity() { return capacity; }
    public ArrayList<Member> getEnrolledMembers() { return enrolledMembers; }
    public Queue<Member> getWaitlist() { return waitlist; }

    public void setClassName(String className) { this.className = className; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setEnrolledMembers(ArrayList<Member> enrolledMembers) { this.enrolledMembers = enrolledMembers; }
    // setWaitlist නිවැරදිව Queue එකක් භාවිතා කරයි
    public void setWaitlist(Queue<Member> waitlist) { this.waitlist = waitlist; }

    public boolean enrollMember(Member m) {
        if (enrolledMembers.size() < capacity) {
            enrolledMembers.add(m);
            return true;
        } else {
            waitlist.offer(m);
            return false;
        }
    }

    public boolean cancelEnrollment(Member m) {
        if (enrolledMembers.remove(m)) {
            if (!waitlist.isEmpty()) {
                Member promoted = waitlist.poll();
                enrolledMembers.add(promoted);
                System.out.println("Promoted " + promoted.getName() + " from waitlist to enrolled.");
            }
            return true;
        }
        return false;
    }

    public boolean isEnrolled(Member m) {
        return enrolledMembers.contains(m);
    }

    public boolean isOnWaitlist(Member m) {
        return waitlist.contains(m);
    }

    @Override
    public String toString() {
        return String.format("Class: %s, Instructor: %s, Capacity: %d, Enrolled: %d, Waiting: %d",
                className, instructor, capacity, enrolledMembers.size(), waitlist.size());
    }
}
public class Member {
    private int id;
    private String name;
    private String phone;
    private String membershipType;

    public Member(int id, String name, String phone, String membershipType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.membershipType = membershipType;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Phone: %s, Type: %s",
                id, name, phone, membershipType);
    }
}
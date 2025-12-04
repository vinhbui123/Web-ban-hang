package vn.edu.hcmuaf.fit.Web_ban_hang.model;

public class Color {
    private int id;
    private String name;

    public Color(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String color) {
        this.name = color;
    }

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", color='" + name + '\'' +
                '}';
    }


}

package edu.brown.cs.final_project.timagotchi.pets;

public class Pet {
  private String id;
  private String name;
  private double xp;
  private int level;
  private String pathToSprite;

  public Pet(String i, String petName, String imagePath) {
    id = i;
    name = petName;
    xp = 0;
    level = 1;
    pathToSprite = imagePath;
  }

  public String getName() {
    return name;
  }

  public double getXp() {
    return xp;
  }

  public int getLevel() {
    return level;
  }

  public String getImage() {
    return pathToSprite;
  }

  public void addXp(double add) {
    xp = xp + add;
  }

  public void incrementLevel() {
    level = level + 1;
  }

  public void updateSprite(String imagePath) {
    pathToSprite = imagePath;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

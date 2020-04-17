package edu.brown.cs.final_project.timagochi.pets;

public class Pet {

  private String name;
  private double points;
  private int level;
  private String pathToSprite;

  public Pet(String petName, String imagePath) {
    name = petName;
    points = 0;
    level = 1;
    pathToSprite = imagePath;
  }

  public String getName() {
    return name;
  }

  public double getPoints() {
    return points;
  }

  public int getLevel() {
    return level;
  }

  public String getImage() {
    return pathToSprite;
  }

  public void setPoint(double add) {
    points = points + add;
  }

  public void incrementLevel() {
    level = level + 1;
  }

  public void updateSprite(String imagePath) {
    pathToSprite = imagePath;
  }

}

package edu.brown.cs.final_project.timagotchi.pets;

import java.util.Random;

/**
 * Pet that each student has.
 */
public class Pet {
  private final int numSprites = 7;
  private String id;
  private String name;
  private double xp;
  private int level;
  private String pathToSprite;
  private String[] randomSpeech;

  /**
   * Initializes the Pet class.
   *
   * @param i The id of the pet.
   * @param petName The name of the pet.
   */
  public Pet(String i, String petName) {
    id = i;
    name = petName;
    xp = 0;
    level = 1;
    Random rand = new Random();
    int randomElement = rand.nextInt(numSprites) + 1;
    pathToSprite = "../img/skin" + randomElement + ".png";
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

  /**
   * Adds a given amount of xp to the pet.
   *
   * @param add The amount of xp to be added.
   */
  public void addXp(double add) {
    xp = xp + add;
  }

  /**
   * Incrementer for the level of the pet.
   */
  public void incrementLevel() {
    level = level + 1;
  }

  public void setLevel(int l) {
    level = l;
  }

  /**
   * Setter for the path to the image of the pet.
   *
   * @param imagePath The path to the image.
   */
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

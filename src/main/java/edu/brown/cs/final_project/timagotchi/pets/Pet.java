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

  /**
   * Getter for the name of the pet.
   *
   * @return The name of the pet.
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for the xp of the pet.
   *
   * @return The xp of the pet.
   */
  public double getXp() {
    return xp;
  }

  /**
   * Getter for the level of the pet.
   *
   * @return The level of the pet.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Getter for the image path of the pet.
   *
   * @return The image path.
   */
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

  /**
   * Setter for the level of the pet.
   *
   * @param l The level of the pet.
   */
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

  /**
   * Getter for the id of the pet.
   *
   * @return The pet id.
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for the id of the pet.
   *
   * @param id The id of the pet.
   */
  public void setId(String id) {
    this.id = id;
  }
}

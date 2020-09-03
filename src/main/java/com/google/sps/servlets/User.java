// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

/**
 * Represents an User.
 */
public class User {
  private long id;
  private String name;
  private String email;
  private String bio;
  private String imgURL;

  public String getName() {
    return name;
  }

  public String getBio() {
    return bio;
  }

  public String getEmail() {
    return email;
  }

  public long getId() {
    return id;
  }

  /**
   * Returns a string with the url of the image.
   */
  public String getImage() {
    return imgURL;
  }

  public void setName(String givenName) {
    name = givenName;
  }

  public void setBio(String givenBio) {
    bio = givenBio;
  }

  public void setEmail(String givenEmail) {
    email = givenEmail;
  }

  public void setId(long givenId) {
    id = givenId;
  }

  /**
   * Set a string for the recipe's image URL.
   */
  public void setImage(String givenImage) {
    imgURL = givenImage;
  }
}

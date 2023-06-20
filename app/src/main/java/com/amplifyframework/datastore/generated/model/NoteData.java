package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the NoteData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "NoteData", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class NoteData implements Model {
  public static final QueryField ID = field("NoteData", "id");
  public static final QueryField NAME = field("NoteData", "name");
  public static final QueryField DEPARTMENT = field("NoteData", "department");
  public static final QueryField JOB_ROLE = field("NoteData", "jobRole");
  public static final QueryField DESCRIPTION = field("NoteData", "description");
  public static final QueryField HOBBIES = field("NoteData", "hobbies");
  public static final QueryField LOCATION = field("NoteData", "location");
  public static final QueryField POINTS = field("NoteData", "points");
  public static final QueryField BADGES = field("NoteData", "badges");
  public static final QueryField IMAGE = field("NoteData", "image");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String") String department;
  private final @ModelField(targetType="String") String jobRole;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="String") String hobbies;
  private final @ModelField(targetType="String") String location;
  private final @ModelField(targetType="Int") Integer points;
  private final @ModelField(targetType="Int") Integer badges;
  private final @ModelField(targetType="String") String image;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public String getDepartment() {
      return department;
  }
  
  public String getJobRole() {
      return jobRole;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getHobbies() {
      return hobbies;
  }
  
  public String getLocation() {
      return location;
  }
  
  public Integer getPoints() {
      return points;
  }
  
  public Integer getBadges() {
      return badges;
  }
  
  public String getImage() {
      return image;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private NoteData(String id, String name, String department, String jobRole, String description, String hobbies, String location, Integer points, Integer badges, String image) {
    this.id = id;
    this.name = name;
    this.department = department;
    this.jobRole = jobRole;
    this.description = description;
    this.hobbies = hobbies;
    this.location = location;
    this.points = points;
    this.badges = badges;
    this.image = image;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      NoteData noteData = (NoteData) obj;
      return ObjectsCompat.equals(getId(), noteData.getId()) &&
              ObjectsCompat.equals(getName(), noteData.getName()) &&
              ObjectsCompat.equals(getDepartment(), noteData.getDepartment()) &&
              ObjectsCompat.equals(getJobRole(), noteData.getJobRole()) &&
              ObjectsCompat.equals(getDescription(), noteData.getDescription()) &&
              ObjectsCompat.equals(getHobbies(), noteData.getHobbies()) &&
              ObjectsCompat.equals(getLocation(), noteData.getLocation()) &&
              ObjectsCompat.equals(getPoints(), noteData.getPoints()) &&
              ObjectsCompat.equals(getBadges(), noteData.getBadges()) &&
              ObjectsCompat.equals(getImage(), noteData.getImage()) &&
              ObjectsCompat.equals(getCreatedAt(), noteData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), noteData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getDepartment())
      .append(getJobRole())
      .append(getDescription())
      .append(getHobbies())
      .append(getLocation())
      .append(getPoints())
      .append(getBadges())
      .append(getImage())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("NoteData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("department=" + String.valueOf(getDepartment()) + ", ")
      .append("jobRole=" + String.valueOf(getJobRole()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("hobbies=" + String.valueOf(getHobbies()) + ", ")
      .append("location=" + String.valueOf(getLocation()) + ", ")
      .append("points=" + String.valueOf(getPoints()) + ", ")
      .append("badges=" + String.valueOf(getBadges()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static NoteData justId(String id) {
    return new NoteData(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      department,
      jobRole,
      description,
      hobbies,
      location,
      points,
      badges,
      image);
  }
  public interface NameStep {
    BuildStep name(String name);
  }
  

  public interface BuildStep {
    NoteData build();
    BuildStep id(String id);
    BuildStep department(String department);
    BuildStep jobRole(String jobRole);
    BuildStep description(String description);
    BuildStep hobbies(String hobbies);
    BuildStep location(String location);
    BuildStep points(Integer points);
    BuildStep badges(Integer badges);
    BuildStep image(String image);
  }
  

  public static class Builder implements NameStep, BuildStep {
    private String id;
    private String name;
    private String department;
    private String jobRole;
    private String description;
    private String hobbies;
    private String location;
    private Integer points;
    private Integer badges;
    private String image;
    @Override
     public NoteData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new NoteData(
          id,
          name,
          department,
          jobRole,
          description,
          hobbies,
          location,
          points,
          badges,
          image);
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep department(String department) {
        this.department = department;
        return this;
    }
    
    @Override
     public BuildStep jobRole(String jobRole) {
        this.jobRole = jobRole;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep hobbies(String hobbies) {
        this.hobbies = hobbies;
        return this;
    }
    
    @Override
     public BuildStep location(String location) {
        this.location = location;
        return this;
    }
    
    @Override
     public BuildStep points(Integer points) {
        this.points = points;
        return this;
    }
    
    @Override
     public BuildStep badges(Integer badges) {
        this.badges = badges;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        this.image = image;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, String department, String jobRole, String description, String hobbies, String location, Integer points, Integer badges, String image) {
      super.id(id);
      super.name(name)
        .department(department)
        .jobRole(jobRole)
        .description(description)
        .hobbies(hobbies)
        .location(location)
        .points(points)
        .badges(badges)
        .image(image);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder department(String department) {
      return (CopyOfBuilder) super.department(department);
    }
    
    @Override
     public CopyOfBuilder jobRole(String jobRole) {
      return (CopyOfBuilder) super.jobRole(jobRole);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder hobbies(String hobbies) {
      return (CopyOfBuilder) super.hobbies(hobbies);
    }
    
    @Override
     public CopyOfBuilder location(String location) {
      return (CopyOfBuilder) super.location(location);
    }
    
    @Override
     public CopyOfBuilder points(Integer points) {
      return (CopyOfBuilder) super.points(points);
    }
    
    @Override
     public CopyOfBuilder badges(Integer badges) {
      return (CopyOfBuilder) super.badges(badges);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
  }
  
}
